## Prompt — Versão para Projeto de Portfólio no GitHub (Java + Angular + Bootstrap)

Crie um projeto open-source para portfólio chamado **“DataShield – Data Anonymization Platform (Portfolio Edition)”**. Ele deve ser **fácil de rodar localmente**, ter **boa documentação**, **prints/gifs**, e **qualidade de engenharia**. O foco é mostrar arquitetura, boas práticas e um MVP funcional.

### 1) Objetivo do projeto

Uma plataforma web que permite:

* Upload de datasets (CSV/JSON; Excel opcional)
* Profiling básico e sugestão de colunas PII
* Criação de políticas de anonimização por coluna
* Execução de anonimização como job assíncrono
* Visualização de resultados (preview controlado)
* Exportação do dataset anonimizado
* Auditoria (log de ações)

> Importante: o projeto deve deixar claro que é uma **edição de portfólio**, com limites e decisões documentadas.

---

### 2) Stack obrigatória

**Frontend**

* Angular 17+ (standalone components preferencial)
* Bootstrap 5 (e ng-bootstrap opcional)
* Angular Router + Guards
* Reactive Forms

**Backend**

* Java 21 (ou 17) + Spring Boot 3.x
* Spring Security com JWT (login simples local)
* Spring Data JPA + Hibernate
* PostgreSQL
* Flyway (migrações)
* Swagger/OpenAPI (springdoc)

**Jobs**

* Implementar job assíncrono simples e confiável:

  * Opção preferida: fila via DB + worker scheduler (Spring @Scheduled) para portfólio
  * Alternativa: Spring Batch (se não complicar o setup)
* Status do job: QUEUED/RUNNING/SUCCEEDED/FAILED

**Infra**

* Docker Compose (frontend, backend, postgres)
* Opcional: MinIO para storage (se for simples); senão, storage local com abstração

---

### 3) Funcionalidades (MVP bem demonstrável)

#### 3.1 Autenticação e Perfis

* Login simples com usuário/senha (seed no banco)
* Roles: `ADMIN`, `ANALYST`, `AUDITOR`
* Guards no Angular e autorização no backend por role

#### 3.2 Datasets

* Upload CSV/JSON
* Criar “Dataset” com metadados: nome, linhas, colunas, owner, criado em
* Profiling:

  * tipos inferidos
  * contagem de nulos
  * cardinalidade
* Detecção PII (heurística):

  * por nome de coluna (cpf, email, telefone etc.)
  * regex simples (email, cpf/phone)
  * marcar com “PII provável” + score

#### 3.3 Políticas de anonimização (Wizard)

* UI para mapear coluna → técnica + parâmetros
* Técnicas mínimas:

  * MASK (ex.: manter últimos 4)
  * HASH (SHA-256 com salt)
  * SUPPRESS (remove ou torna null)
  * GENERALIZE (ex.: idade por faixa)
* Salvar política com versionamento simples (v1, v2)

#### 3.4 Execução (Jobs)

* Criar job de anonimização: dataset + policy
* Worker processa em background e salva output
* Exibir progresso/contagem de linhas (mesmo que aproximado)

#### 3.5 Resultados

* Preview antes/depois em tabela (limitado a 50 linhas)
* Export CSV do dataset anonimizado

#### 3.6 Auditoria

* Registrar eventos:

  * LOGIN
  * DATASET_UPLOADED
  * POLICY_CREATED/UPDATED
  * JOB_STARTED/JOB_FINISHED
  * EXPORT_TRIGGERED
* Tela de auditoria com filtros

---

### 4) Arquitetura e organização (para impressionar no portfólio)

**Backend (camadas)**

* `controller` (REST)
* `service` (regras)
* `domain/model`
* `repository`
* `worker` (processamento assíncrono)
* `security` (JWT, roles)
* `audit` (registrador de eventos)
* Motor de anonimização plugável:

  * interface `AnonymizationStrategy`
  * `StrategyRegistry` / factory por tipo

**Frontend**

* `pages/` (views)
* `components/` (reutilizáveis)
* `services/` (API)
* `guards/`, `interceptors/`
* `models/`

---

### 5) UX (bem “demoável”)

Criar um fluxo guiado com steps:

1. Upload dataset
2. Profiling + PII suggestions
3. Criar policy
4. Rodar job
5. Ver resultado + export

Dashboard:

* cards de “Datasets”, “Jobs recentes”, “Alertas” (falhas)

---

### 6) Qualidade e documentação (essencial para GitHub)

Gerar os seguintes artefatos:

#### 6.1 README excelente

Incluir:

* O que é o DataShield (1 parágrafo)
* Features (checklist)
* Arquitetura (diagrama simples em ASCII ou Mermaid)
* Stack
* Como rodar (Docker Compose) em 3 passos
* Credenciais demo (admin/analyst/auditor)
* Fluxo de uso (passo a passo)
* Screenshots (colocar em `/docs/images`)
* Roadmap (próximos passos)
* Licença (MIT)

#### 6.2 CONTRIBUTING + CODE OF CONDUCT

Simples e padrão.

#### 6.3 Issue templates (opcional)

Template de bug e feature.

#### 6.4 Seeds e dados de exemplo

* 2 arquivos CSV pequenos em `/sample-data`
* Seed com 3 usuários e 1 policy pronta

#### 6.5 Testes mínimos

* Testes unitários para pelo menos 2 estratégias (MASK e HASH)
* Teste de API básico (ex.: upload/policy/job)

---

### 7) Regras importantes (para portfólio)

* Não guardar dados sensíveis “para sempre”: implementar configuração de retenção simples (ex.: apagar outputs após X dias — pode ser apenas config e endpoint).
* Logs estruturados no backend.
* Tratamento de erro padronizado (Problem Details / RFC 7807 ou padrão consistente).
* Não exagerar no escopo: entregar um MVP redondo e bem documentado.

---

### 8) Entregáveis finais

* Repositório pronto com:

  * `/backend`
  * `/frontend`
  * `/infra/docker-compose.yml`
  * `/docs` (arquitetura e imagens)
  * `/sample-data`
* Tudo rodando localmente com um comando:

  * `docker compose up --build`
* Swagger disponível em endpoint padrão
* App Angular acessível e funcional end-to-end

,
