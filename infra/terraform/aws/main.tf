terraform {
  required_version = ">= 1.5.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

# VPC por defecto (para simplificar)
data "aws_vpc" "default" {
  default = true
}

# Obtener los subnets de la VPC por defecto
data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
}

# Security Group para la API
resource "aws_security_group" "franchise_sg" {
  name        = "franchise-api-sg"
  description = "Security group for franchise API"
  vpc_id      = data.aws_vpc.default.id

  # SSH solo desde tu IP
  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [var.my_ip_cidr]
  }

  # API HTTP 8080 abierta (para demo/prueba técnica)
  ingress {
    description = "API HTTP 8080"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Salida libre
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# EC2 con Docker + docker-compose + tu repo + docker compose up
resource "aws_instance" "franchise_ec2" {
  ami                    = var.ami_id
  instance_type          = var.instance_type
  subnet_id              = data.aws_subnets.default.ids[0]
  vpc_security_group_ids = [aws_security_group.franchise_sg.id]
  key_name               = var.key_name

  user_data = <<-EOF
              #!/bin/bash
              # Actualizar paquetes
              dnf update -y

              # Instalar Docker y git
              dnf install -y docker git

              systemctl enable docker
              systemctl start docker
              usermod -aG docker ec2-user

              # Instalar Docker Compose v2 (plugin)
              mkdir -p /usr/local/lib/docker/cli-plugins/
              curl -SL https://github.com/docker/compose/releases/download/v2.29.7/docker-compose-linux-x86_64 \
                -o /usr/local/lib/docker/cli-plugins/docker-compose
              chmod +x /usr/local/lib/docker/cli-plugins/docker-compose

              # Clonar tu repo y levantar la app
              cd /home/ec2-user
              git clone ${var.git_repo_url} app || (cd app && git pull)
              cd app

              # Levantar el stack (usa tu docker-compose.yml)
              docker compose up -d
              EOF

  tags = {
    Name = "franchise-api-ec2"
  }
}
