# Simple Kafka Example

Fluxo completo com dois servicos Spring Boot:

- `user-api` (porta `8081`): recebe HTTP e publica eventos Kafka.
- `persistence-api` (porta `8082`): consome eventos, persiste no PostgreSQL e responde `get-by-id` via Kafka.

Infra local via Docker Compose:

- Kafka: `localhost:9092`
- Kafka UI: `http://localhost:8080`
- PostgreSQL: `localhost:5432` (`user_db` / `root` / `1033`)

## Subir infraestrutura

```powershell
docker compose up -d
docker compose ps
```

## Subir APIs

Em dois terminais separados:

```powershell
cd user-api
.\mvnw.cmd spring-boot:run
```

```powershell
cd persistence-api
.\mvnw.cmd spring-boot:run
```

## Endpoints (`user-api`)

- `POST /users` -> publica `user-create-request`
- `PUT /users/{id}` -> publica `user-update-request`
- `DELETE /users/{id}` -> publica `user-delete-request`
- `GET /users/{id}` -> publica `user-get-by-id-request` e aguarda `user-get-by-id-result`

## Exemplos (Postman cURL import)

### Create

```bash
curl --location 'http://localhost:8081/users' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "Eduardo Melo",
  "email": "eduardo@email.com",
  "password": "123456",
  "phoneNumber": "11999998888",
  "documentNumber": "12345678900"
}'
```

### Get by id

```bash
curl --location 'http://localhost:8081/users/1'
```

### Update

```bash
curl --location --request PUT 'http://localhost:8081/users/1' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "Eduardo Melo Updated",
  "email": "eduardo.updated@email.com",
  "password": "123456",
  "phoneNumber": "11999998887",
  "documentNumber": "12345678900"
}'
```

### Delete

```bash
curl --location --request DELETE 'http://localhost:8081/users/1'
```

## Topicos

- `user-create-request`
- `user-update-request`
- `user-delete-request`
- `user-get-by-id-request`
- `user-get-by-id-result`

