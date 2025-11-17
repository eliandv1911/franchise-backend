output "ec2_public_ip" {
  description = "IP pública de la instancia EC2"
  value       = aws_instance.franchise_ec2.public_ip
}

output "api_url" {
  description = "URL base de la API de franquicias"
  value       = "http://${aws_instance.franchise_ec2.public_ip}:8080/api/franchises"
}
