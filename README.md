MiMeli
=================================
MiMeli es un proyecto en Spring Boot que permite consultar la información de un país dada una IP. Los datos que se pueden consultar son los siguientes:
- Código ISO.
- Nombre del país.
- Código de moneda del país.
- Conversión o equivalencia al valor del dólar americano (USD).

### Instalación con Docker.
Para construir la aplicación y posteriormente desplegar en el contenedor DOCKER, se ejecutará el siguiente comando desde la raíz del proyecto:
```
docker-compose up
```

### Uso.
La aplicación contiene 3 EndPoints:
+ El primer EndPoint consulta la información del país:
  + http://localhost:8080/api/v1/country-info?ip=102.38.230.0
  ```
  {
    "code": "CO",
    "countryName": "Colombia",
    "localCurrency": "COP",
    "currentValue": 4057
  }
  ```
+ El segundo EndPoint registra en una lista negra y persiste en una base de datos las IP's para luego impedir consultar la información del primer EndPoint. (POST)
  + http://localhost:8080/api/v1/blacklist
  ```
  {
    "ip": "104.72.224.1",
    "message": "Successfully registered IP."
  }
  ```
+ El último EndPoint lista las IP's que se encuentran registradas en la lista negra. (GET)
  + http://localhost:8080/api/v1/blacklist
  ```
  [
    {
        "ip": "104.72.224.1"
    }
  ]
  ```
