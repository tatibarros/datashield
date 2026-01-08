# Guia de Desenvolvimento Local

## Pré-requisitos

- Java 21+
- Node.js 20+
- npm ou yarn
- PostgreSQL 15+ (ou use Docker)
- Git

## Setup Inicial

### Opção 1: Com Docker Compose (Recomendado)

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/datashield.git
cd datashield

# Inicie todos os serviços
docker-compose up --build

# A aplicação estará pronta em:
# Frontend: http://localhost:4200
# Backend: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
```

### Opção 2: Desenvolvimento Local

#### 1. Configure o Banco de Dados

```bash
# Inicie apenas o PostgreSQL
docker-compose up postgres

# Ou instale PostgreSQL localmente e crie o banco:
psql -U postgres
CREATE DATABASE datashield;
CREATE USER datashield WITH PASSWORD 'datashield123';
ALTER ROLE datashield SET client_encoding TO 'utf8';
GRANT ALL PRIVILEGES ON DATABASE datashield TO datashield;
```

#### 2. Inicie o Backend

```bash
cd backend

# Configure variáveis de ambiente (opcional)
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=datashield
export DB_USER=datashield
export DB_PASSWORD=datashield123

# Compile e rode
mvn clean install
mvn spring-boot:run

# Backend estará em: http://localhost:8080
```

#### 3. Inicie o Frontend

```bash
cd frontend

# Instale dependências
npm install

# Rode o servidor de desenvolvimento
ng serve --open

# Frontend estará em: http://localhost:4200
```

## Estrutura de Pastas para Desenvolvimento

```
datashield/
├── backend/
│   ├── src/main/java/com/datashield/
│   ├── src/test/java/com/datashield/
│   ├── pom.xml
│   └── target/  (ignorar)
├── frontend/
│   ├── src/app/
│   ├── src/assets/
│   ├── package.json
│   ├── angular.json
│   └── node_modules/  (ignorar)
├── docs/
├── sample-data/
└── docker-compose.yml
```

## Comandos Úteis

### Backend

```bash
# Build
mvn clean package

# Testes
mvn test

# Formato de código
mvn spotless:apply

# Verificar dependências
mvn dependency:tree

# Gerar Javadoc
mvn javadoc:javadoc
```

### Frontend

```bash
# Instalar dependências
npm install

# Desenvolvimento com hot reload
ng serve

# Build para produção
ng build --configuration production

# Testes
ng test

# Lint
ng lint
```

## Debug

### Backend (IntelliJ/Eclipse)

1. Adicione breakpoints no código
2. Execute em debug mode:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.arguments="--debug"
   ```
3. Conecte seu IDE na porta 5005

### Frontend (Chrome DevTools)

1. Abra http://localhost:4200
2. Pressione F12
3. Use a aba "Sources" para debug

## Fluxo de Desenvolvimento

### 1. Criar Nova Feature

```bash
# Crie uma branch
git checkout -b feature/minha-feature

# Desenvolva no frontend e/ou backend
# Escreva testes
# Commit com mensagens claras
git commit -m "feat: adiciona suporte a Excel"

# Push
git push origin feature/minha-feature

# Abra um Pull Request no GitHub
```

### 2. Testar Localmente

```bash
# Terminal 1: Backend
cd backend && mvn spring-boot:run

# Terminal 2: Frontend
cd frontend && ng serve

# Terminal 3: Testes (opcional)
cd backend && mvn test
cd frontend && ng test
```

### 3. Antes de Fazer Commit

```bash
# Backend
cd backend
mvn spotless:apply  # Formata código
mvn test            # Roda testes

# Frontend
cd frontend
ng lint             # Verifica lint
ng test             # Roda testes
```

## Ambiente de Variáveis

Crie um arquivo `.env` na raiz do projeto:

```
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=datashield
DB_USER=datashield
DB_PASSWORD=datashield123

# JWT
JWT_SECRET=sua-chave-secreta-aqui-minimo-32-chars

# CORS
CORS_ORIGINS=http://localhost:4200

# Storage
DATASTORAGE_PATH=./data
```

## Troubleshooting

### Backend não conecta ao banco
```bash
# Verifique se PostgreSQL está rodando
docker ps | grep postgres

# Ou localmente
psql -U datashield -d datashield
```

### Frontend não consegue chamar API
```bash
# Verifique CORS no backend
# application.yml deve ter:
# cors-allowed-origins: http://localhost:4200

# Limpe cache do navegador
# Ctrl+Shift+Del no Chrome
```

### Porta já em uso
```bash
# Backend na porta 8080
lsof -i :8080
kill -9 <PID>

# Frontend na porta 4200
lsof -i :4200
kill -9 <PID>
```

## Logs

### Backend Logs

```bash
# Logs em tempo real (se rodando com docker)
docker logs -f datashield-backend

# Ou verifique o arquivo
tail -f backend/logs/application.log
```

### Frontend Logs

```bash
# Console do navegador (F12)
# Ou veja o output do `ng serve`
```

## Next Steps

- Leia [ARCHITECTURE.md](ARCHITECTURE.md) para entender a arquitetura
- Confira [CONTRIBUTING.md](CONTRIBUTING.md) antes de submeter PR
- Veja o [README.md](../README.md) para documentação geral
