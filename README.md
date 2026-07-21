# EduControl: Sistema de Gestión Académica

Proyecto final de la asignatura de **Programación Orientada a Objetos** para la Escuela de Formación de Tecnólogos de la Escuela Politécnica Nacional.

## Descripción del Proyecto
**EduControl** es una aplicación de escritorio diseñada para administrar la información académica de instituciones educativas. El sistema facilita el registro, consulta, modificación y eliminación (CRUD) de datos de estudiantes, profesores y asignaciones.

La aplicación destaca por su arquitectura robusta, modular y segura, implementando principios sólidos de ingeniería de software y programación orientada a objetos.

## Características Principales
* **Seguridad y Autenticación**: Sistema de inicio de sesión con control de acceso basado en roles (Administrador, Profesor, Estudiante). Las contraseñas están protegidas mediante cifrado **BCrypt**[cite: 9].
* **Arquitectura en Capas**: Estructura organizada por paquetes (app, controller, dao, db, model, util) para separar responsabilidades entre la lógica de negocio, interfaz y acceso a datos[cite: 9].
* **Principios de POO**: Implementación estricta de **encapsulamiento**, **herencia** (clase `Persona` base), **abstracción** y **polimorfismo** (interfaz `ICRUD` para operaciones de base de datos)[cite: 9].
* **Persistencia de Datos**: Conexión robusta a base de datos **MySQL** utilizando JDBC, con uso de `PreparedStatement` para garantizar la seguridad frente a inyecciones SQL[cite: 9].
* **Validaciones**: Verificación integral de datos de entrada (campos obligatorios, formatos de correo, restricciones numéricas)[cite: 9].

## Tecnologías Utilizadas
* **Lenguaje**: Java
* **Interfaz Gráfica**: JavaFX (FXML)
* **Base de Datos**: MySQL
* **Gestión de Conexiones**: JDBC
* **Arquitectura**: MVC / DAO Pattern

##  Estructura del Proyecto

## Estructura del proyecto

```text
Proyecto_POO/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── proyecto_poo/
│                   ├── app/         # Clase principal (MainApp)
│                   ├── controller/  # Controladores de interfaz (FXML)
│                   ├── dao/         # Capa de acceso a datos (ICRUD)
│                   ├── db/          # Conexión a base de datos
│                   ├── model/       # Entidades del sistema
│                   └── util/        # Clases auxiliares (Sesión)
├── resources/
│   ├── view/        # Archivos FXML
│   ├── css/         # Estilos visuales
│   └── database/    # script_bd_proyecto_poo.sql
└── docs/            # Manual de usuario e informe técnico
```

# 📄 Documentación
Dentro del proyecto se incluyen:

- Informe del proyecto. docs/
- Manual de usuario. docs/
- Script SQL. databae/
- Video demostrativo. video/
- Ejecutable. dist/

##Integrantes
-Alessandro Pillajo
-Alex Lascano


# 📜 Licencia

Proyecto desarrollado con fines académicos para la asignatura **Programación Orientada a Objetos** de la **Escuela Politécnica Nacional**.
