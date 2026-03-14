AUTH API 🔐

API de autenticação com JWT, Refresh Token rotativo e controle de roles

API REST stateless desenvolvida com Java + Spring Boot focada em demonstrar boas práticas de segurança backend, arquitetura em camadas e testes automatizados.

O projeto implementa autenticação baseada em JWT, com refresh token rotativo, controle de acesso por roles e documentação automática via Swagger/OpenAPI.

Toda a infraestrutura de banco de dados é gerenciada com Docker e versionada com Flyway migrations.

👨‍💻 Autor

Werike Rodrigues Alves

GitHub
https://github.com/WerikeRodriguesA/auth-api

Março — 2026

🚀 Tecnologias Utilizadas
Categoria	Tecnologia
Linguagem	Java 25
Framework	Spring Boot 4.0.3
Segurança	Spring Security 7
JWT	JJWT 0.12.5
Banco de dados	PostgreSQL 16
ORM	Hibernate / Spring Data JPA
Migrations	Flyway
Documentação	SpringDoc OpenAPI (Swagger)
Build	Maven
Testes	JUnit 5 + Mockito
Infraestrutura	Docker
🏗 Arquitetura

A aplicação segue uma arquitetura em camadas, separando responsabilidades entre controladores, serviços, segurança e persistência.

src/main/java/com/projetoapi/auth_api/

config/          → Configurações de segurança e OpenAPI  
controller/      → Endpoints da API  
service/         → Lógica de negócio  
security/        → Filtros JWT e autenticação  
domain/          → Entidades do sistema  
repository/      → Camada de acesso ao banco  
dto/             → Objetos de entrada e saída da API  
exception/       → Tratamento global de erros


Essa organização facilita:

manutenção

testes

evolução do sistema

escalabilidade do projeto

🔐 Fluxo de Autenticação

O sistema é stateless, ou seja, o servidor não mantém sessões.

Cada requisição autenticada carrega seu próprio token JWT.

Login

Cliente envia email e password

O Spring Security autentica as credenciais

A aplicação gera:

Access Token (15 minutos)

Refresh Token (7 dias)

Ambos são retornados ao cliente

Requisição autenticada

O cliente envia o token no header

Authorization: Bearer {accessToken}

O JwtAuthFilter intercepta a requisição

O token é validado criptograficamente

O usuário é autenticado no SecurityContext

A requisição segue para o controller

📊 Modelo de Dados
Users
Campo	Tipo	Descrição
id	UUID	Identificador único
name	VARCHAR	Nome do usuário
email	VARCHAR	Email de autenticação
password	VARCHAR	Hash BCrypt
role	VARCHAR	USER ou ADMIN
created_at	TIMESTAMP	Data de criação
Refresh Tokens
Campo	Tipo	Descrição
id	UUID	Identificador
token	VARCHAR	Refresh token
user_id	UUID	Usuário dono
expires_at	TIMESTAMP	Data de expiração
revoked	BOOLEAN	Se foi revogado
created_at	TIMESTAMP	Data de criação
📡 Endpoints
Método	Rota	Autenticação	Descrição
POST	/auth/register	Pública	Cadastra usuário
POST	/auth/login	Pública	Autentica usuário
POST	/auth/refresh	Pública	Gera novo access token
POST	/auth/logout	Pública	Revoga refresh token
GET	/users/me	JWT	Dados do usuário autenticado
GET	/admin/users	ADMIN	Lista usuários
📥 Exemplo de Requisição
Registrar usuário
POST /auth/register

Request

{
  "name": "Werike Rodrigues",
  "email": "werike@email.com",
  "password": "12345678"
}

Response

{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "caf93cfd-9b6f-41ad...",
  "tokenType": "Bearer",
  "expiresIn": 604800000
}

Usuário autenticado
GET /users/me

Header

Authorization: Bearer {accessToken}

Response

{
  "id": "uuid",
  "name": "Werike Rodrigues",
  "email": "werike@email.com",
  "role": "USER"
}

⚙️ Componentes Principais
JwtService

Responsável por toda a manipulação de tokens JWT.

Principais responsabilidades:

gerar access token
validar tokens
extrair email do token
assinar tokens com HMAC-SHA512
JwtAuthFilter
Filtro executado antes dos controllers.

Responsável por:

interceptar requisições
extrair token do header
validar assinatura
autenticar usuário no contexto de segurança
AuthService
Centraliza toda a lógica de autenticação.

Responsável por:

registro de usuário
login
refresh token
logout
rotação de refresh token
SecurityConfig

Define toda a configuração de segurança da API.

Principais regras:

/auth/** público
/admin/** apenas ADMIN
demais rotas autenticadas
sessão desabilitada (STATELESS)

🧪 Testes Automatizados

O projeto possui testes unitários e de integração.

Testes unitários

Classe:

JwtServiceTest


Verifica:

geração de tokens
extração de email
validação de tokens
rejeição de tokens inválidos
Testes de integração

Classe:

AuthControllerIntegrationTest

Testa os endpoints reais da API:
registro de usuário
validação de campos
login
autenticação
rejeição de credenciais inválidas

Os testes utilizam:
@Transactional

para evitar poluição do banco de dados.

▶️ Como Rodar o Projeto
Pré-requisitos
Java 21+
Docker
Maven

1️⃣ Clonar o repositório
git clone https://github.com/WerikeRodriguesA/auth-api.git

2️⃣ Subir o banco de dados
docker run \
--name authdb \
-e POSTGRES_USER=admin \
-e POSTGRES_PASSWORD=admin123 \
-e POSTGRES_DB=authdb \
-p 5432:5432 \
-d postgres:16

3️⃣ Variáveis de ambiente
DB_USER=admin
DB_PASSWORD=admin123
JWT_SECRET=sua-chave-secreta-aqui

4️⃣ Rodar aplicação

Execute a aplicação pela IDE ou:
./mvnw spring-boot:run

5️⃣ Acessar Swagger
http://localhost:8080/swagger-ui.html

🧪 Rodar Testes
./mvnw test

🎯 Objetivo do Projeto

Este projeto foi desenvolvido para demonstrar domínio em:
autenticação stateless
segurança com Spring Security
JWT e refresh tokens
arquitetura backend em camadas

boas práticas de código

testes automatizados
