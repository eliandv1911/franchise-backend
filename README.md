# 🏪 Franchise API – Práctica Backend (Spring Boot Reactivo)

API REST para gestionar **franquicias**, **sucursales** y **productos**, desarrollada con **Spring Boot 3**, **programación reactiva (WebFlux)**, **MongoDB** y una estructura basada en **Clean Architecture**.

Incluye:

- Programación **reactiva** (Spring WebFlux + Reactive MongoDB)
- **Clean Architecture** (domain, application, infrastructure, web)
- **Pruebas unitarias** (JUnit 5, Reactor Test, WebTestClient)
- **Docker** y **Docker Compose** para contenerización (app + MongoDB)
- **Infrastructure as Code (IaC)** con Terraform (ejemplo de provisión de la aplicación spring boot y la BD Mongo)

---

## 📂 Arquitectura del proyecto

Estructura a alto nivel:

```txt
src/main/java/com/example/franchiseapi
 ├── FranchiseApiApplication.java
 ├── domain            # Modelos de dominio + puertos (interfaces)
 │    ├── model        # Franchise, Branch, Product
 │    └── port         # *RepositoryPort (interfaces)
 ├── application
 │    └── service      # Casos de uso (FranchiseService)
 └── infrastructure
      ├── persistence
      │    └── mongo   # Documents, ReactiveRepositories, Adapters
      └── web
           ├── dto     # DTOs de entrada/salida
           ├── controller
           └── error   # Manejo global de errores
```

Esta separación permite:

- **Independencia de frameworks** en la capa de dominio.
- **Reemplazar infraestructura** (BD, web, etc.) sin afectar el núcleo de negocio.
- Mejor **testabilidad** y **mantenimiento**.

---

# 🛠 Tecnologías Utilizadas

- Java 17
- Spring Boot 3.x
- Spring WebFlux (reactivo)
- Spring Data MongoDB Reactive
- MongoDB
- Lombok
- JUnit 5, Reactor Test, Mockito, WebTestClient
- Docker, Docker Compose
- Terraform (IaC)

---

# ✅ Requerimientos Previos

Para ejecutar la aplicación en local necesitas:

- **JDK 17+**
- **Maven 3.8+** (o usar el wrapper `./mvnw`)
- **Docker y Docker Compose** (opcional, pero altamente recomendado para la infraestructura)
- **Opcional:** una instancia local de **MongoDB** si decides no utilizar Docker para la base de datos.

---

# 🐳 Ejecución con Docker + Docker Compose (Recomendado)

Esta es la opción más sencilla para levantar tanto la aplicación como su base de datos **MongoDB**.

## Construir y Levantar los Contenedores

Desde la raíz del proyecto, ejecuta el siguiente comando:

```bash
docker compose up --build
```

Esto hará lo siguiente:

- Levantar un contenedor de **MongoDB** accesible internamente en `mongodb://mongo:27017`.
- **Construir la imagen** de la aplicación utilizando el `Dockerfile` del proyecto.
- **Levantar la aplicación** en el puerto externo `8080`.

> **Nota sobre la Configuración:** La variable de entorno `SPRING_DATA_MONGODB_URI` para la aplicación se configura en el archivo `docker-compose.yml`, apuntando al servicio de Mongo:
>
> ```yaml
> SPRING_DATA_MONGODB_URI: mongodb://mongo:27017/franchise_db
> ```
>
> Para detener y limpiar los servicios, ejecuta:

```bash
docker compose down
```

---

# 🧪 Pruebas Unitarias

El proyecto incluye pruebas unitarias para asegurar la calidad y el correcto funcionamiento:

- **Casos de uso** en la capa `application` (clase `FranchiseService`).
- **Controladores WebFlux** usando `WebTestClient` para pruebas de integración ligeras.

### Ejecutar los Tests

Puedes ejecutar todos los tests del proyecto usando el wrapper de Maven o Maven directamente:

```bash
./mvnw test
# o
mvn test
```

---

# 🌐 Endpoints Principales de la API

La **Base URL** para todos los endpoints es: `http://localhost:8080/api/franchises`

## 1. Crear una Franquicia

| Método   | Endpoint          | Descripción                           |
| :------- | :---------------- | :------------------------------------ |
| **POST** | `/api/franchises` | Crea un nuevo registro de franquicia. |

### Cuerpo de la Petición (Request Body)

Se debe enviar un objeto JSON con la estructura del recurso:

```json
{
  "name": "Franquicia Central"
}
```

## 2. Actualizar Nombre de una Franquicia (Plus)

| Método  | Endpoint                        | Descripción                                                        |
| :------ | :------------------------------ | :----------------------------------------------------------------- |
| **PUT** | `/api/franchises/{franchiseId}` | Actualiza el campo `name` de la franquicia especificada por su ID. |

### Path Parameter

- `{franchiseId}`: El ID único de la franquicia a actualizar.

### Cuerpo de la Petición (Request Body)

Solo se necesita enviar el campo que se desea modificar:

```json
{
  "name": "Nuevo Nombre Franquicia"
}
```

## 3. Agregar Sucursal a una Franquicia

Este endpoint demuestra una relación anidada (Subrecurso) dentro de la API.

| Método   | Endpoint                                 | Descripción                                                      |
| :------- | :--------------------------------------- | :--------------------------------------------------------------- |
| **POST** | `/api/franchises/{franchiseId}/branches` | Agrega una nueva sucursal (branch) a la franquicia especificada. |

### Path Parameter

- `{franchiseId}`: El ID único de la franquicia a la que se desea agregar la sucursal.

### Cuerpo de la Petición (Request Body)

Se envía el nombre de la nueva sucursal:

```json
{
  "name": "Sucursal Norte"
}
```

## 4. Actualizar Nombre de una Sucursal (Plus)

| Método  | Endpoint                              | Descripción                                           |
| :------ | :------------------------------------ | :---------------------------------------------------- |
| **PUT** | `/api/franchises/branches/{branchId}` | Actualiza el campo `name` de una sucursal específica. |

### Path Parameter

- `{branchId}`: El ID único de la sucursal (branch) a modificar.

### Cuerpo de la Petición (Request Body)

```json
{
  "name": "Sucursal Norte Actualizada"
}
```

## 5. Agregar Producto a una Sucursal

Este endpoint maneja la colección anidada de productos dentro de una sucursal.

| Método   | Endpoint                                       | Descripción                                                    |
| :------- | :--------------------------------------------- | :------------------------------------------------------------- |
| **POST** | `/api/franchises/branches/{branchId}/products` | Agrega un nuevo producto a la sucursal especificada por su ID. |

### Path Parameter

- `{branchId}`: El ID único de la sucursal (branch) a la que se desea agregar el producto.

### Cuerpo de la Petición (Request Body)

Se envía la información del nuevo producto:

```json
{
  "name": "Producto A",
  "stock": 50
}
```

## 6. Actualizar Nombre y Stock de un Producto (Plus)

| Método  | Endpoint                               | Descripción                                                 |
| :------ | :------------------------------------- | :---------------------------------------------------------- |
| **PUT** | `/api/franchises/products/{productId}` | Actualiza el nombre y/o el stock de un producto específico. |

### Path Parameter

- `{productId}`: El ID único del producto a modificar.

### Cuerpo de la Petición (Request Body)

Se envía la información con los nuevos valores para el producto:

```json
{
  "name": "Producto A Actualizado",
  "stock": 80
}
```

## 7. Eliminar Producto de una Sucursal

Este endpoint permite eliminar un recurso de una colección anidada.

| Método     | Endpoint                                                   | Descripción                                    |
| :--------- | :--------------------------------------------------------- | :--------------------------------------------- |
| **DELETE** | `/api/franchises/branches/{branchId}/products/{productId}` | Elimina el producto específico de la sucursal. |

### Path Parameters

- `{branchId}`: El ID único de la sucursal que contiene el producto.
- `{productId}`: El ID único del producto a eliminar.

### Cuerpo de la Petición

Esta petición **no requiere cuerpo** (No Body).

## 8. Actualizar Solo el Stock de un Producto

Este endpoint utiliza el método **PATCH** para una actualización parcial y pasa el valor a modificar como parámetro de consulta (`query parameter`).

| Método    | Endpoint                                               | Descripción                                                        |
| :-------- | :----------------------------------------------------- | :----------------------------------------------------------------- |
| **PATCH** | `/api/franchises/products/{productId}/stock?stock=100` | Actualiza únicamente el valor del stock de un producto específico. |

### Path Parameter

- `{productId}`: El ID único del producto cuyo stock se va a modificar.

### Query Parameter

- `stock`: El nuevo valor numérico del stock (`stock=100`).

### Cuerpo de la Petición

Esta petición **no requiere cuerpo** (No Body).

## 9. Obtener Producto(s) con Más Stock por Sucursal para una Franquicia

Este endpoint realiza una consulta que devuelve el producto (o productos, en caso de empate) con el stock más alto de _cada_ sucursal, dentro de la franquicia especificada.

| Método  | Endpoint                                               | Descripción                                                                   |
| :------ | :----------------------------------------------------- | :---------------------------------------------------------------------------- |
| **GET** | `/api/franchises/{franchiseId}/top-products-by-branch` | Devuelve el producto con más stock de cada sucursal asociada a la franquicia. |

### Path Parameter

- `{franchiseId}`: El ID único de la franquicia que se desea consultar.

### Respuesta de Ejemplo (Response Body)

El resultado es un array de objetos, donde cada objeto representa un "producto más stockeado" de una sucursal:

```json
[
  {
    "productId": "64f0c...",
    "productName": "Producto A",
    "stock": 80,
    "branchId": "64f0a..."
  },
  {
    "productId": "64f0d...",
    "productName": "Producto B",
    "stock": 75,
    "branchId": "64f0b..."
  }
]
```

---

## ☁️ Infraestructura como Código (IaC) con Terraform + AWS

La solución incluye un ejemplo de **Infraestructura como Código (IaC)** usando **Terraform** para desplegar toda la aplicación en **AWS**.

Al aplicar el módulo de Terraform se crea automáticamente:

- Una instancia **EC2** (Amazon Linux 2023, `t3.micro`, apta para free tier).
- Un **Security Group** con:
  - Puerto `22` abierto solo para tu IP (SSH).
  - Puerto `8080` abierto para consumir la API.
- `user_data` que:
  - Instala **Docker** y **Docker Compose**.
  - Instala **git**.
  - Clona este repositorio.
  - Ejecuta `docker compose up -d` para levantar:
    - el backend de Spring WebFlux (`franchise-api`)
    - MongoDB como base de datos.

Al finalizar, la API queda accesible desde Internet en el puerto `8080` sobre la IP pública de la EC2.

### 📂 Estructura de IaC

Los archivos de Terraform se encuentran en:

```text
infra/terraform/aws/
├── main.tf          # Recursos AWS (EC2, SG, user_data, outputs)
├── variables.tf     # Definición de variables
└── terraform.sample.tfvars  # Ejemplo de configuración
```

El archivo real terraform.tfvars no se versiona. Porque tiene datos sensibles.
Debes crearlo localmente a partir del ejemplo.

### ✅ Requisitos Previos para la Ejecución de Terraform

Antes de ejecutar los archivos de Terraform, asegúrate de cumplir con los siguientes requisitos:

#### - Herramientas y Servicios

- **Cuenta de AWS:** Necesitas una cuenta de Amazon Web Services con permisos suficientes para crear los siguientes recursos: **Instancias EC2** y **Security Groups**.
- **Terraform:** Debe estar **instalado** en tu máquina local.

#### - Credenciales y Acceso

- **Credenciales de AWS:** Deben estar configuradas localmente para que Terraform pueda interactuar con tu cuenta (por ejemplo, usando `aws configure` para configurar el perfil por defecto).
- **Par de Claves (Key Pair) de AWS:** Debes crear un par de claves en la región donde desplegarás la infraestructura. Este par es necesario para poder acceder por SSH a la instancia EC2 creada:
  - **Proceso:** Consola AWS → EC2 → Key pairs → **Create key pair** (e.g., nombrar `franchise-key`).

### - Configuración de Variables (`terraform.tfvars`)

El archivo `terraform.tfvars` no se versiona y debe crearse localmente.

#### 1. Copiar el Archivo de Ejemplo

Navega al directorio de infraestructura y copia el archivo de ejemplo:

```bash
cd infra/terraform/aws
cp terraform.sample.tfvars terraform.tfvars
```

#### 2. Editar y Ajustar Variables

Abre el nuevo archivo terraform.tfvars y ajusta los valores necesarios:

```bash
my_ip_cidr = "XXX.XXX.XXX.XXX/32"   # Tu IP pública (formato CIDR)
key_name   = "franchise-key"        # Nombre del Key Pair creado en AWS
ami_id     = "ami-XXXXXXXXXXXXXXX"  # AMI de Amazon Linux 2023 (x86_64) en tu región
# instance_type = "t2.micro"        # Opcional, por defecto ya es t2.micro
# git_repo_url  = "[https://github.com/eliandv1911/franchise-backend.git](https://github.com/eliandv1911/franchise-backend.git)"

```

- **`my_ip_cidr`**: Puedes obtener tu IP pública buscando en Google **"what is my ip"**. Recuerda agregar `/32` al final para el formato CIDR.
- **`ami_id`**: Puedes obtener el ID de la AMI desde la consola de EC2 al crear una instancia manual y seleccionar **Amazon Linux 2023** para tu región.

### 🚀 Despliegue en AWS con Terraform

Desde la carpeta infra/terraform/aws:

```bash
terraform init      # Inicializa el backend y descarga providers
terraform plan      # Muestra qué recursos se van a crear
terraform apply     # Crea la infraestructura en AWS
```

confirma con **yes** cuando Terraform lo pida.

Al terminar, verás una salida similar:

```txt
Outputs:

ec2_public_ip = "X.X.X.X"
api_url       = "http://X.X.X.X:8080/api/franchises"
```

Con eso ya puedes consumir la API desplegada en AWS

### 🧹 Destruir la infraestructura

Para evitar costos innecesarios, cuando termines de usar el entorno puedes destruir todo lo creado con:

```bash
terraform destroy
```

---

### 🔍 Ejemplo real de salida de Terraform

Al ejecutar `terraform apply` en mi entorno, Terraform creó la infraestructura en AWS y devolvió los siguientes **outputs reales**:

```text
ec2_public_ip = "44.200.163.8"
api_url       = "http://44.200.163.8:8080/api/franchises"
```

Con esta información, se puede probar la API desplegada en AWS directamente desde Postman o cualquier cliente HTTP
