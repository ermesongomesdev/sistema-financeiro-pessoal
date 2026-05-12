# Sistema Financeiro Pessoal

Sistema financeiro pessoal/familiar desenvolvido com **Java Spring Boot** no backend e **React + Vite** no frontend.

O projeto permite controlar carteiras, receitas, despesas, categorias, dívidas, metas financeiras, relatórios mensais e exportações.

---

## Visão geral

Este projeto é dividido em duas partes:

- **Backend:** API REST em Java com Spring Boot.
- **Frontend:** Interface visual em React com Vite.

O backend expõe as rotas da aplicação e se conecta ao PostgreSQL.  
O frontend consome a API e apresenta uma interface visual com dashboard, navegação lateral, gráficos e tela de configurações.

---

## Tecnologias utilizadas

### Backend

- Java
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- PostgreSQL
- Maven
- Swagger/OpenAPI

### Frontend

- React
- Vite
- JavaScript
- Tailwind CSS
- Axios
- React Router DOM
- Recharts
- Lucide React

---

## Funcionalidades

- Cadastro e login de usuários
- Autenticação com JWT
- Criação de carteira financeira
- Suporte para carteira compartilhada/familiar
- Cadastro de categorias
- Cadastro de receitas e despesas
- Controle de transações pagas e pendentes
- Controle de dívidas parceladas
- Controle de metas financeiras
- Relatórios mensais
- Exportação de relatório em PDF
- Exportação de relatório em Excel
- Notificações de vencimentos
- Interface visual em React
- Dashboard com cards e gráficos
- Área de configurações
- Alteração visual da cor principal do painel
- Salvamento da cor escolhida no navegador usando `localStorage`

---

## Estrutura do projeto

```text
sistema-financeiro-pessoal/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── br/com/veltrium/finance/
│       │       ├── config/
│       │       ├── controller/
│       │       ├── dto/
│       │       ├── model/
│       │       ├── repository/
│       │       ├── security/
│       │       └── service/
│       │
│       └── resources/
│           └── application.yml
│
├── sistema-financeiro-frontend/
│   ├── src/
│   │   ├── App.jsx
│   │   ├── index.css
│   │   └── main.jsx
│   │
│   ├── package.json
│   └── vite.config.js
│
├── pom.xml
├── README.md
└── .gitignore
```

---

## Pré-requisitos

Antes de rodar o projeto, instale:

- Java
- Maven
- PostgreSQL
- Node.js
- npm
- Git

---

## Configuração do banco de dados

Crie um banco no PostgreSQL com o nome:

```sql
CREATE DATABASE finance_db;
```

Configuração esperada no backend:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/finance_db
    username: postgres
    password: postgres
```

Caso sua senha do PostgreSQL seja diferente, altere o arquivo:

```text
src/main/resources/application.yml
```

Ou rode passando a senha por variável de ambiente no PowerShell:

```powershell
$env:DB_PASSWORD="SUA_SENHA"
```

---

## Como rodar o backend

Entre na pasta principal do projeto:

```powershell
cd "$env:USERPROFILE\Downloads\sistema-financeiro-pessoal"
```

Rode o backend na porta `8081`:

```powershell
mvn clean spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
```

Quando aparecer algo parecido com:

```text
Started FinanceApplication
Tomcat started on port 8081
```

o backend estará rodando.

---

## Acessar o Swagger

Com o backend ligado, abra:

```text
http://localhost:8081/swagger-ui/index.html
```

O Swagger permite testar as rotas da API, como login, cadastro, carteiras, categorias, transações, relatórios, dívidas e metas.

---

## Como rodar o frontend

Abra outro terminal e entre na pasta do frontend:

```powershell
cd "$env:USERPROFILE\Downloads\sistema-financeiro-pessoal\sistema-financeiro-frontend"
```

Instale as dependências:

```powershell
npm install
```

Rode o frontend:

```powershell
npm run dev
```

Depois acesse:

```text
http://localhost:5173
```

---

## Rodando backend e frontend juntos

Você precisa deixar dois terminais abertos.

### Terminal 1 — Backend

```powershell
cd "$env:USERPROFILE\Downloads\sistema-financeiro-pessoal"
mvn clean spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
```

### Terminal 2 — Frontend

```powershell
cd "$env:USERPROFILE\Downloads\sistema-financeiro-pessoal\sistema-financeiro-frontend"
npm run dev
```

Depois abra:

```text
http://localhost:5173
```

---

## Portas utilizadas

```text
Backend:  http://localhost:8081
Frontend: http://localhost:5173
Swagger:  http://localhost:8081/swagger-ui/index.html
Banco:    PostgreSQL na porta 5432
```

---

## Como testar a API

### 1. Criar usuário

Rota:

```text
POST /api/auth/register
```

Exemplo:

```json
{
  "name": "Seu Nome",
  "email": "seu.email@example.com",
  "password": "12345678"
}
```

---

### 2. Fazer login

Rota:

```text
POST /api/auth/login
```

Exemplo:

```json
{
  "email": "seu.email@example.com",
  "password": "12345678"
}
```

A resposta retorna um token JWT.

---

### 3. Usar o token JWT

Nas rotas protegidas, envie o token no header:

```text
Authorization: Bearer SEU_TOKEN_AQUI
```

Exemplo usando PowerShell:

```powershell
$token = "SEU_TOKEN_AQUI"
```

---

### 4. Criar carteira

Rota:

```text
POST /api/familias
```

Exemplo:

```json
{
  "name": "Minha Carteira"
}
```

Exemplo com PowerShell:

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8081/api/familias" `
  -Method POST `
  -Headers @{ Authorization = "Bearer $token" } `
  -ContentType "application/json" `
  -Body '{"name":"Minha Carteira"}'
```

---

### 5. Listar carteiras

Rota:

```text
GET /api/familias
```

Exemplo com PowerShell:

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8081/api/familias" `
  -Method GET `
  -Headers @{ Authorization = "Bearer $token" }
```

---

### 6. Criar categoria

Rota:

```text
POST /api/categories
```

Exemplo de receita:

```json
{
  "householdId": 2,
  "name": "Salario",
  "type": "INCOME",
  "color": "#22c55e"
}
```

Exemplo de despesa:

```json
{
  "householdId": 2,
  "name": "Alimentacao",
  "type": "EXPENSE",
  "color": "#ef4444"
}
```

---

### 7. Criar transação

Rota:

```text
POST /api/transactions
```

Exemplo de receita:

```json
{
  "householdId": 2,
  "categoryId": 1,
  "description": "Salario do mes",
  "amount": 2500,
  "type": "INCOME",
  "dueDate": "2026-05-11",
  "paid": true
}
```

Exemplo de despesa:

```json
{
  "householdId": 2,
  "categoryId": 2,
  "description": "Compra no mercado",
  "amount": 350,
  "type": "EXPENSE",
  "dueDate": "2026-05-11",
  "paid": true
}
```

---

### 8. Ver relatório mensal

Rota:

```text
GET /api/reports/monthly
```

Parâmetros:

```text
householdId: 2
year: 2026
month: 5
```

---

### 9. Criar dívida parcelada

Rota:

```text
POST /api/debts
```

Exemplo:

```json
{
  "householdId": 2,
  "description": "Celular parcelado",
  "creditor": "Loja",
  "totalAmount": 1200,
  "installments": 6,
  "firstDueDate": "2026-05-15"
}
```

---

### 10. Criar meta financeira

Rota:

```text
POST /api/goals
```

Exemplo:

```json
{
  "householdId": 2,
  "name": "Comprar notebook",
  "targetAmount": 4000,
  "currentAmount": 500,
  "deadline": "2026-12-31"
}
```

---

### 11. Exportar relatório em PDF

Rota:

```text
GET /api/exports/monthly/pdf
```

Parâmetros:

```text
householdId: 2
year: 2026
month: 5
```

---

### 12. Exportar relatório em Excel

Rota:

```text
GET /api/exports/monthly/excel
```

Parâmetros:

```text
householdId: 2
year: 2026
month: 5
```

---

## Configuração visual do frontend

O frontend possui:

- Sidebar lateral
- Dashboard financeiro
- Cards de resumo
- Gráficos com Recharts
- Tabela de últimas transações
- Tela de configurações
- Escolha de cor principal do sistema
- Salvamento da cor escolhida no navegador via `localStorage`

---

## Segurança e versionamento

Este projeto não deve versionar arquivos sensíveis.

Não envie para o GitHub:

```text
.env
.env.*
node_modules/
target/
dist/
*.log
hs_err_pid*.log
replay_pid*.log
*.key
*.pem
*.p12
*.jks
```

Arquivos de log como:

```text
hs_err_pid34140.log
```

são logs de erro da JVM e não fazem parte do código.

---

## Sugestão de `.gitignore`

```gitignore
# Java / Spring Boot
target/
*.class
*.jar
*.war
*.ear

# JVM crash logs
hs_err_pid*.log
replay_pid*.log

# Logs
*.log
logs/

# Maven
.mvn/wrapper/maven-wrapper.jar

# Frontend
node_modules/
dist/
build/
.vite/

# Ambiente / dados sensíveis
.env
.env.*
!.env.example

# Configurações locais
application-local.yml
application-dev.yml
application-prod.yml
src/main/resources/application-local.yml
src/main/resources/application-dev.yml
src/main/resources/application-prod.yml

# Chaves e certificados
*.pem
*.key
*.p12
*.jks
secrets/
credentials/

# IDEs
.idea/
.vscode/
*.iml

# Sistema operacional
.DS_Store
Thumbs.db
```

---

## Como enviar para o GitHub

Na raiz do projeto:

```powershell
git init
git add .
git commit -m "Initial commit: sistema financeiro pessoal"
```

Depois crie um repositório no GitHub e conecte:

```powershell
git branch -M main
git remote add origin https://github.com/SEU_USUARIO/sistema-financeiro-pessoal.git
git push -u origin main
```

Antes do commit, confira o que será enviado:

```powershell
git status
git diff --cached --name-only
```

Não envie arquivos como `node_modules`, `target`, `dist`, `.env` ou logs da JVM.

---

## Status do projeto

O backend já possui estrutura funcional com Spring Boot, PostgreSQL e JWT.

O frontend já possui layout inicial em React com dashboard, navegação lateral, gráficos e configurações visuais.

---

## Próximas melhorias possíveis

- Conectar todas as telas do frontend ao backend
- Implementar login real no frontend
- Salvar token JWT no navegador
- Criar telas funcionais de categorias, transações, dívidas e metas
- Criar filtros de mês e ano nos relatórios
- Melhorar responsividade mobile
- Criar deploy do backend e frontend
