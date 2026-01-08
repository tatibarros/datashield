# Troubleshooting & FAQ

## 🆘 Problemas Comuns

### Docker Compose não inicia

**Erro:** `docker-compose: command not found`

```bash
# Solução: Instalar Docker Desktop que inclui Compose
# https://www.docker.com/products/docker-desktop

# Ou instalar Compose separadamente
curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
```

### Porta já em uso

**Erro:** `Bind for 0.0.0.0:8080 failed: port is already allocated`

```bash
# Solução 1: Parar outros containers
docker stop $(docker ps -q)

# Solução 2: Usar porta diferente
# Editar docker-compose.yml:
# ports:
#   - "8081:8080"  # Mudar para 8081
```

### Falha ao conectar no banco de dados

**Erro:** `connection refused localhost:5432`

```bash
# Verificar se PostgreSQL está rodando
docker-compose logs postgres

# Reiniciar banco de dados
docker-compose restart postgres

# Aguardar health check passar (pode levar 10 segundos)
docker-compose ps  # Verificar status
```

### Frontend mostra erro de CORS

**Erro:** `Access to XMLHttpRequest blocked by CORS policy`

```yaml
# Adicionar no docker-compose.yml (backend):
environment:
  CORS_ORIGINS: "http://localhost:4200,http://localhost:3000"
```

---

## 🔍 Debugging

### Verificar logs do backend

```bash
# Logs em tempo real
docker-compose logs -f backend

# Logs do banco
docker-compose logs -f postgres

# Logs do frontend
docker-compose logs -f frontend
```

### Acessar container em execução

```bash
# Backend (Java)
docker exec -it datashield-backend sh

# Frontend
docker exec -it datashield-frontend sh

# Banco de dados
docker exec -it datashield-postgres psql -U datashield -d datashield
```

### Testar API manualmente

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Response: token JWT

# Usar token
curl http://localhost:8080/api/datasets \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 📦 Limpeza & Reset

### Remover todos os containers e volumes

```bash
# CUIDADO: Isso deleta todos os dados!

docker-compose down -v

# Ou se quiser manter o volume:
docker-compose down
```

### Reconstruir tudo do zero

```bash
docker-compose build --no-cache
docker-compose up
```

### Limpar espaço em disco

```bash
# Remover imagens não usadas
docker image prune

# Remover containers parados
docker container prune

# Remover volumes não usados
docker volume prune

# Limpeza completa
docker system prune -a
```

---

## 🧪 Testes

### Executar testes do backend

```bash
# Todos os testes
cd backend
mvn test

# Teste específico
mvn test -Dtest=HashAnonymizationStrategyTest

# Com relatório de cobertura
mvn test jacoco:report
```

### Executar testes do frontend

```bash
cd frontend
npm test

# Modo watch
npm test -- --watch

# Com cobertura
npm test -- --code-coverage
```

---

## 🔐 Segurança

### Trocar senha dos usuários demo

```bash
# Generate bcrypt hash (online: https://bcrypt-generator.com/)

# Editar V2__Insert_Seed_Data.sql
INSERT INTO users (username, password, email, role, active) VALUES
('admin', '$2a$10$NEW_BCRYPT_HASH_HERE', 'admin@datashield.com', 'ADMIN', true);
```

### Trocar JWT secret

```bash
# Editar .env ou docker-compose.yml
JWT_SECRET=your-very-long-secret-key-with-32-chars-minimum-here

# Deve ter pelo menos 32 caracteres
```

### Habilitar HTTPS em produção

```yaml
# docker-compose.yml
environment:
  HTTPS_ENABLED: "true"
  SSL_CERT_PATH: "/app/certs/cert.pem"
  SSL_KEY_PATH: "/app/certs/key.pem"

volumes:
  - ./certs:/app/certs  # Adicionar certificados SSL
```

---

## 🚀 Performance

### Aumentar limites de memória

```yaml
# docker-compose.yml
services:
  backend:
    environment:
      JAVA_OPTS: "-Xms512m -Xmx1024m"  # 512MB-1GB heap
```

### Otimizar banco de dados

```sql
-- Executar no PostgreSQL
ANALYZE;
REINDEX;

-- Ver tamanho das tabelas
SELECT schemaname, tablename, pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename))
FROM pg_tables
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;
```

---

## 📱 Mobile/Responsividade

O frontend já é responsivo (Bootstrap 5), mas para testar:

```bash
# Chrome DevTools
F12 → Toggle device toolbar → Ctrl+Shift+M

# Ou usar terminal
curl -X GET http://localhost:4200 \
  -H "User-Agent: Mobile"
```

---

## 🌐 Proxies & Firewalls

Se atrás de proxy corporativo:

```bash
# Maven (backend)
# ~/.m2/settings.xml
<proxy>
  <id>corporateProxy</id>
  <active>true</active>
  <protocol>http</protocol>
  <host>proxy.company.com</host>
  <port>8080</port>
</proxy>

# NPM (frontend)
npm config set proxy http://proxy.company.com:8080
npm config set https-proxy http://proxy.company.com:8080
```

---

## 📞 Suporte

### Se algo não funcionar:

1. **Verificar Docker:**
   ```bash
   docker --version
   docker-compose --version
   docker-compose ps
   ```

2. **Verificar logs:**
   ```bash
   docker-compose logs backend
   docker-compose logs postgres
   ```

3. **Restartar serviços:**
   ```bash
   docker-compose restart
   ```

4. **Limpar e rebuildar:**
   ```bash
   docker-compose down
   docker-compose up --build
   ```

5. **Abrir issue no GitHub:**
   - Descrever o erro
   - Incluir logs
   - Mencionar SO e versão do Docker

---

## ✅ Checklist de Deploy

- [ ] JWT_SECRET alterado para valor seguro
- [ ] Senhas dos usuários alteradas
- [ ] Logs configurados para produção
- [ ] CORS_ORIGINS atualizado com domínios corretos
- [ ] Certificados SSL configurados
- [ ] Backups do banco de dados configurados
- [ ] Monitoramento ativado
- [ ] Rate limiting implementado

---

**Happy troubleshooting! 🛠️**
