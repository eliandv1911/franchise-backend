# terraform.tfvars.example
# Copia este archivo como "terraform.tfvars" y ajusta los valores
# antes de ejecutar `terraform apply`.

# IP pública desde la que te vas a conectar por SSH.
# Formato CIDR: X.X.X.X/32
my_ip_cidr = "XXX.XXX.XXX.XXX/32"

# Nombre del Key Pair existente en tu cuenta de AWS (EC2 → Key Pairs).
key_name   = "mi-keypair-aws"

# AMI de Amazon Linux 2023 (x86_64) en la región configurada (por defecto us-east-1).
# Puedes obtenerla desde la consola de EC2 al crear una instancia manual.
ami_id     = "ami-XXXXXXXXXXXXXXX"

# Opcional: sobreescribir el tipo de instancia (por defecto t2.micro)
# instance_type = "t2.micro"

# Opcional: sobreescribir la URL del repo (por defecto la del proyecto)
# git_repo_url  = "https://github.com/mi-usuario/franchise-backend.git"
