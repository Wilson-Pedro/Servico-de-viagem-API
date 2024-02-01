# Servico-de-viagem-API

O Servico-de-viagem-API é uma interface de programação de aplicativos que oferece funcionalidades como solicitações de corridas, acesso a dados de motoristas e veículos, e gestão de pagamentos. A API tem por objetivo facilitar a criação de aplicativos que ampliam as funcionalidades da empresa de transporte, tornando-a acessível a uma variedade de desenvolvedores e casos de uso. 

# Tecnologias utilizadas
## Back end
- Java
- Spring Boot
- JPA / Hibernate
- Maven

## Banco de dados
-H2 


## Modelo conceitual
![Modelo Conceitual](https://github.com/Wilson-Pedro/images/blob/main/servico-de-viagem/Uber%20(2).png)


# EndPoints:
## Usuario
### GET
```
http://localhost:8080/usuarios
```
```
http://localhost:8080/usuarios/1
```
```
http://localhost:8080/usuarios/1/viagens
```
```
http://localhost:8080/usuarios/pages
```


### POST
```
http://localhost:8080/usuarios
```
```
http://localhost:8080/usuarios/solicitacarViagem
```

### DELETE
```
http://localhost:8080/usuarios/1
```
```
http://localhost:8080/usuarios/1/cancelarViagem
```

### PATCH
```
http://localhost:8080/usuarios/1/desativar
```
```
http://localhost:8080/usuarios/1/ativar
```

### PUT
```
http://localhost:8080/usuarios/1
```

## Status do Projeto
- Completo v1


## Autor

- [Wilson Pedro](https://github.com/Wilson-Pedro)

## Linkedin
- [Wilson Pedro](https://www.linkedin.com/in/wilson-pedro-976333226/)
