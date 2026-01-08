# 🚀 QUICK START - DataShield

## Em 2 Minutos

### Abra o Terminal

```bash
cd DataShield
docker-compose up --build
```

Aguarde ~1 minuto até os serviços iniciarem.

### Acesse

- **Frontend**: http://localhost:4200
- **API Docs**: http://localhost:8080/swagger-ui.html

### Faça Login

```
Username: admin
Password: admin123
```

**Pronto!** 🎉

---

## O Que Você Tem

| Componente | Localização | Tech |
|-----------|-------------|------|
| **Frontend** | http://localhost:4200 | Angular 17 + Bootstrap |
| **Backend** | http://localhost:8080 | Spring Boot 3.2 + Java 21 |
| **Banco de Dados** | localhost:5432 | PostgreSQL 15 |
| **Docs** | http://localhost:8080/swagger-ui.html | OpenAPI/Swagger |

---

## Credenciais Demo

```
ADMIN (full access)
├─ Username: admin
└─ Password: admin123

ANALYST (can create datasets)
├─ Username: analyst
└─ Password: analyst123

AUDITOR (read-only)
├─ Username: auditor
└─ Password: auditor123
```

---

## Dados de Exemplo

Encontre em `/sample-data`:

1. **customers.csv** - 10 clientes com PII
2. **transactions.csv** - 10 transações

Upload e teste as funcionalidades!

---

## Próximas Etapas

### 1. Explorar

- Dashboard
- Upload de dataset
- Visualizar dados
- Ver logs de auditoria

### 2. Desenvolver (Opcional)

```bash
# Terminal 1: Backend
cd backend && mvn spring-boot:run

# Terminal 2: Frontend
cd frontend && npm install && ng serve

# Terminal 3: DB
docker-compose up postgres
```

Leia [docs/DEVELOPMENT.md](docs/DEVELOPMENT.md) para detalhes.

### 3. Publicar no GitHub

```bash
git add .
git commit -m "Initial commit: DataShield MVP"
git remote add origin https://github.com/seu-usuario/datashield.git
git push -u origin main
```

---

## Documentação

| Arquivo | Propósito |
|---------|-----------|
| [README.md](README.md) | Documentação principal |
| [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) | Diagrama de arquitetura |
| [docs/DEVELOPMENT.md](docs/DEVELOPMENT.md) | Setup local sem Docker |
| [docs/CONTRIBUTING.md](docs/CONTRIBUTING.md) | Guia de contribuição |
| [docs/TROUBLESHOOTING.md](docs/TROUBLESHOOTING.md) | Problemas comuns |

---

## Stack Completo

```
Frontend:  Angular 17 + Bootstrap 5 + RxJS
Backend:   Spring Boot 3.2 + Spring Security + JWT
Database:  PostgreSQL 15 + Flyway migrations
Deploy:    Docker + Docker Compose
```

---

## Problemas?

1. **Backend não inicia**
   ```bash
   docker-compose logs postgres
   docker-compose restart postgres
   ```

2. **Frontend não conecta**
   - Limpe cache (Ctrl+Shift+Del)
   - Verifique se backend está rodando em :8080

3. **Porta já em uso**
   ```bash
   docker-compose down -v
   docker-compose up --build
   ```

Mais em [docs/TROUBLESHOOTING.md](docs/TROUBLESHOOTING.md)

---

## Status

✅ Backend: Completo e testado
✅ Frontend: Completo e funcional
✅ Banco: Schema e seed data inclusos
✅ Docker: Pronto para deployment
✅ Documentação: Completa

**Pronto para produção! 🚀**

---

**v1.0.0 - 2024**
