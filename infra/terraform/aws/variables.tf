variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

variable "my_ip_cidr" {
  description = "Tu IP pública en formato CIDR para permitir SSH (ej: 181.51.40.123/32)"
  type        = string
}

variable "key_name" {
  description = "Nombre del key pair existente en AWS para SSH"
  type        = string
}

variable "git_repo_url" {
  description = "URL de tu repositorio Git con el backend"
  type        = string
  default     = "https://github.com/eliandv1911/franchise-backend.git"
}

variable "instance_type" {
  description = "Tipo de instancia EC2 (t3.micro entra en free tier)"
  type        = string
  default     = "t3.micro"
}

variable "ami_id" {
  description = "AMI de Amazon Linux 2023 en la región seleccionada (x86_64)"
  type        = string
}
