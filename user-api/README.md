# User API

Este proyecto es una API REST para el registro de usuarios, implementada en Java con Spring Boot 3 y Maven. Utiliza una arquitectura hexagonal (puertos y adaptadores) y almacena los datos en una base de datos H2 en memoria.

## Requisitos

- Java 17
- Maven
- Spring Boot 3
- JPA
- Validación
- SpringDoc OpenAPI
- JWT (Json Web Token)
- Bcrypt para el hash de contraseñas

## Estructura del Proyecto

```
user-api
├── mvnw
├── mvnw.cmd
├── pom.xml
├── Dockerfile
├── .devcontainer
│   ├── devcontainer.json
│   └── Dockerfile
├── .gitignore
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── userapi
│   │   │               ├── UserApiApplication.java
│   │   │               ├── config
│   │   │               │   ├── OpenApiConfig.java
│   │   │               │   └── JwtConfig.java
│   │   │               ├── domain
│   │   │               │   └── model
│   │   │               │       ├── User.java
│   │   │               │       └── Phone.java
│   │   │               ├── application
│   │   │               │   ├── dto
│   │   │               │   │   ├── UserRequest.java
│   │   │               │   │   └── UserResponse.java
│   │   │               │   ├── ports
│   │   │               │   │   ├── in
│   │   │               │   │   │   └── RegisterUserUseCase.java
│   │   │               │   │   └── out
│   │   │               │   │       └── UserRepositoryPort.java
│   │   │               │   └── service
│   │   │               │       └── RegisterUserService.java
│   │   │               ├── adapters
│   │   │               │   ├── in
│   │   │               │   │   └── web
│   │   │               │   │       └── UserController.java
│   │   │               │   └── out
│   │   │               │       └── persistence
│   │   │               │           ├── entity
│   │   │               │           │   ├── UserEntity.java
│   │   │               │           │   └── PhoneEntity.java
│   │   │               │           ├── UserJpaRepository.java
│   │   │               │           └── UserRepositoryAdapter.java
│   │   │               └── shared
│   │   │                   ├── security
│   │   │                   │   ├── JwtUtil.java
│   │   │                   │   └── SecurityConfig.java
│   │   │                   └── util
│   │   │                       └── ValidationProperties.java
│   │   └── resources
│   │       ├── application.yml
│   │       ├── application-test.yml
│   │       └── data.sql
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── userapi
│                       ├── application
│                       │   └── RegisterUserServiceTest.java
│                       ├── adapters
│                       │   └── in
│                       │       └── web
│                       │           └── UserControllerIntegrationTest.java
│                       └── shared
│                           ├── JwtUtilTest.java
│                           └── PasswordEncoderTest.java
└── .github
    └── workflows
        └── maven.yml
```

## Configuración

### application.yml

Las expresiones regulares para validar el email y la contraseña se pueden configurar en el archivo `src/main/resources/application.yml`.

```yaml
validation:
  email: "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"
  password: "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"
```

### Comandos para ejecutar el proyecto

Para ejecutar la aplicación, utiliza el siguiente comando:

```bash
./mvnw spring-boot:run
```

### Pruebas

Para ejecutar las pruebas, utiliza el siguiente comando:

```bash
./mvnw clean test
```

## Docker

Para construir la imagen Docker, utiliza el siguiente comando:

```bash
docker build -t user-api .
```

## Notas

- La API permite registrar usuarios con los campos: `name`, `email`, `password`, y `phones` (que incluye `number`, `citycode`, y `countrycode`).
- Se valida si el email ya existe y se devuelve un error 409 si es así.
- Se valida el formato del email y la contraseña, devolviendo un error 400 si son incorrectos.
- En caso de éxito, se retorna un objeto que incluye `id`, `created`, `modified`, `lastLogin`, `token`, y `isActive`.

Este proyecto está diseñado para ser fácil de extender y mantener, siguiendo los principios de la arquitectura hexagonal.