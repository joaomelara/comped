# Projeto - Comped (Cidades ESG Inteligentes)

**Aplicação de Gestão de Energia com Pipeline CI/CD Completo**

[![Java](https://img.shields.io/badge/Java-21-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Latest-blue)](https://www.docker.com/)
[![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-CI%2FCD-blue)](https://github.com/features/actions)

---

## 📋 Como executar localmente com Docker

### Pré-requisitos

- Docker e Docker Compose instalados
- Java 21 (opcional, para build local)
- Maven 3.8+ (opcional, para build local)

### Passos para Execução

#### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/comped.git
cd comped
```

#### 2. Build da aplicação (gera JAR)

```bash
# Com Maven instalado
mvn clean package

# Ou com Maven Wrapper (Linux/Mac)
./mvnw clean package

# Ou com Maven Wrapper (Windows)
mvnw.cmd clean package
```

#### 3. Inicie os containers

```bash
# Subir aplicação + banco de dados
docker-compose up -d

# Verificar status
docker-compose ps

# Ver logs em tempo real
docker-compose logs -f app
```

#### 4. Acesse a aplicação

```bash
# Health check
curl http://localhost:8080/health

# Listar usuários
curl http://localhost:8080/api/usuarios

# Criar usuário (POST)
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome":"João","email":"joao@example.com","senha":"123456"}'
```

#### 5. Parar a aplicação

```bash
# Parar containers
docker-compose down

# Parar e remover volumes (CUIDADO!)
docker-compose down -v
```

### Variáveis de Ambiente Locais

O arquivo `docker-compose.yml` utiliza as seguintes variáveis:

```env
SPRING_DATASOURCE_URL=jdbc:oracle:thin:@oracle:1521:xe
SPRING_DATASOURCE_USERNAME=system
SPRING_DATASOURCE_PASSWORD=oracle
SPRING_DATASOURCE_DRIVER_CLASS_NAME=oracle.jdbc.OracleDriver
SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.Oracle10gDialect
SPRING_JPA_HIBERNATE_DDL_AUTO=create-drop
```

---

## 🔄 Pipeline CI/CD

### Ferramenta Utilizada

**GitHub Actions** - Plataforma de CI/CD nativa do GitHub

Arquivo de configuração: `.github/workflows/java-ci.yml`

### Etapas do Pipeline

```
┌─────────────────────────────────────────────────────────────┐
│ GITHUB ACTIONS - JAVA SPRING BOOT CI/CD                     │
└─────────────────────────────────────────────────────────────┘

TRIGGERS:
  ✅ Push para main
  ✅ Push para feat/build-implementation
  ✅ Pull Requests para main
  ✅ Workflow dispatch (manual)

┌────────────────────────────────────────────────────────────┐
│ JOB 1: BUILD (Sempre executa)                              │
├────────────────────────────────────────────────────────────┤
│ 1. Checkout Code                       (0.5 min)           │
│ 2. Setup Java 21                       (0.2 min)           │
│ 3. Run Tests (21 testes)               (2 min)             │
│ 4. Build with Maven                    (1 min)             │
│ 5. Set up Docker Buildx                (0.1 min)           │
│ 6. Login to Container Registry         (0.1 min)           │
│ 7. Build and Push Docker Image         (2 min)             │
│                                                             │
│ ✅ Output: Docker image em ghcr.io                         │
│ ⏱️ Duração total: ~6 min                                    │
└────────────────┬────────────────────────────────────────────┘
                 │
        ┌────────┴────────┐
        │                 │
       ❌                ✅
    (Falhou)         (Sucesso)
        │                 │
        │                 ▼
        │    ┌────────────────────────────────┐
        │    │ JOB 2: DEPLOY-STAGING           │
        │    ├────────────────────────────────┤
        │    │ Dispara AUTOMATICAMENTE em main │
        │    │                                │
        │    │ 1. SSH Connect ao servidor      │
        │    │ 2. Docker Pull image            │
        │    │ 3. Docker Compose Up            │
        │    │ 4. Health Check                 │
        │    │                                │
        │    │ ✅ Deploy em staging            │
        │    └────────────────────────────────┘
        │
        └─────────────────────────────────────────┐
                                                  │
                                    ┌─────────────┴────────────┐
                                    │                          │
                                    ▼                          ▼
                        ┌──────────────────────┐  ┌─────────────────────┐
                        │ JOB 3: DEPLOY-PROD   │  │ Sucesso: Staging    │
                        ├──────────────────────┤  │ Online              │
                        │ Dispara MANUAL ou    │  │ http://staging:8081 │
                        │ em push de tag (v*)  │  └─────────────────────┘
                        │                      │
                        │ 1. SSH Connect       │
                        │ 2. Backup versão     │
                        │ 3. Docker Pull       │
                        │ 4. Docker Compose Up │
                        │ 5. Health Check      │
                        │ 6. Rollback se fail  │
                        │                      │
                        │ ✅ Deploy em produção│
                        └──────────────────────┘
```

### Funcionamento Detalhado

#### 1️⃣ **Build Job** (Sempre)

Quando houver push para main ou pull request:

```yaml
- Checkout do código
- Setup Java 21 + Maven cache
- Executa 21 testes (3 classes)
  ├── ControllerTests (5 testes)
  ├── ServiceTests (8 testes)
  └── RepositoryTests (8 testes)
- Maven clean package (gera JAR)
- Build Docker image
- Push para GitHub Container Registry (GHCR)
```

**Falha se**: Testes falhem ou build falhar

#### 2️⃣ **Deploy Staging Job** (Automático)

Dispara automaticamente após build bem-sucedido em main:

```yaml
- Conecta via SSH ao servidor staging
- Pull da imagem Docker mais recente
- Executa: docker-compose -f docker-compose.staging.yml up -d
- Aguarda 10 segundos
- Health check: GET /health
- Se falhar: Rollback automático
```

**Ambiente**:
- Banco: Porta 1522 (diferente da produção)
- App: Porta 8081
- Profile: staging
- Dados: Isolados em volume separado

#### 3️⃣ **Deploy Production Job** (Manual)

Dispara por:
1. Workflow dispatch manual (GitHub Actions button)
2. Push de tag (ex: `git tag v1.0.0 && git push origin v1.0.0`)

```yaml
- Conecta via SSH ao servidor produção
- Backup: Salva versão anterior
- Pull da imagem Docker mais recente
- Executa: docker-compose -f docker-compose.prod.yml up -d
- Aguarda 10 segundos
- Health check rigoroso: GET /health
- Se falhar: Rollback automático para versão anterior
```

**Ambiente**:
- Banco: Porta 1521 (padrão)
- App: Porta 8080
- Profile: prod
- Dados: Persistentes + backup automático

### Logs e Monitoramento

Todos os logs estão disponíveis em:

```
GitHub → Repositório → Actions → Nome do Workflow → Clique para detalhes
```

Cada step mostra:
- ⏱️ Tempo de execução
- 📝 Saída completa
- ❌ Erros (se houver)

---

## 🐳 Containerização

### Dockerfile

```dockerfile
FROM eclipse-temurin:21-alpine
VOLUME /tmp
EXPOSE 8080
ADD target/comped-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Estratégias Adotadas

#### 1. **Base Image Otimizada**
- **Imagem**: `eclipse-temurin:21-alpine`
- **Tamanho**: ~100MB (vs 500MB+ com ubuntu)
- **Vantagem**: Menor tamanho, mais rápido pull/push

#### 2. **Layer Caching**
```dockerfile
ADD target/comped-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```
- Copia apenas JAR necessário (evita copiar todo o projeto)
- Aproveita cache do Docker para builds rápidos

#### 3. **Temporary Volume**
```dockerfile
VOLUME /tmp
```
- Melhora performance de escrita temporária
- Isolado do container filesystem

#### 4. **Health Check** (em docker-compose)
```yaml
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:8080/health"]
  interval: 30s
  timeout: 10s
  retries: 5
  start_period: 60s
```

### Build Multi-Estágio (Recomendação Futura)

Para ainda mais otimização:

```dockerfile
# Stage 1: Build
FROM eclipse-temurin:21 as builder
COPY . /app
WORKDIR /app
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-alpine
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Docker Compose Files

#### 📄 `docker-compose.yml` (Local)

```yaml
version: '3.9'
services:
  oracle:
    image: gvenzl/oracle-xe:21-slim-faststart
    ports: ["1521:1521"]
    environment:
      ORACLE_PASSWORD: oracle
    volumes: [oracle-data:/opt/oracle/oradata]
    
  app:
    build: .
    ports: ["8080:8080"]
    environment:
      SPRING_DATASOURCE_URL: jdbc:oracle:thin:@oracle:1521:xe
      SPRING_DATASOURCE_USERNAME: system
      SPRING_DATASOURCE_PASSWORD: oracle
    depends_on:
      oracle:
        condition: service_healthy
```

#### 📄 `docker-compose.staging.yml` (Staging)

```yaml
version: '3.9'
services:
  oracle-staging:
    image: gvenzl/oracle-xe:21-slim-faststart
    ports: ["1522:1521"]  # Porta diferente
    environment:
      ORACLE_PASSWORD: oracle123
    volumes: [oracle-staging-data:/opt/oracle/oradata]
    
  app-staging:
    image: ${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}
    ports: ["8081:8080"]  # Porta diferente
    environment:
      SPRING_PROFILES_ACTIVE: staging
      SPRING_DATASOURCE_URL: jdbc:oracle:thin:@oracle-staging:1521:xe
```

#### 📄 `docker-compose.prod.yml` (Produção)

```yaml
version: '3.9'
services:
  oracle-prod:
    image: gvenzl/oracle-xe:21-slim-faststart
    ports: ["1521:1521"]  # Porta padrão
    environment:
      ORACLE_PASSWORD: ${PROD_DB_PASSWORD}
    volumes: [oracle-prod-data:/opt/oracle/oradata]
    restart: always
    
  app-prod:
    image: ${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}
    ports: ["8080:8080"]  # Porta padrão
    environment:
      SPRING_PROFILES_ACTIVE: prod
      SPRING_DATASOURCE_PASSWORD: ${PROD_DB_PASSWORD}
    restart: always
```

### Orquestração

**Volumes**:
- `oracle-data` (Local)
- `oracle-staging-data` (Staging)
- `oracle-prod-data` (Produção)

**Redes**:
- `comped_default` (Local) - Isolada
- `comped-staging` (Staging) - Isolada
- `comped-prod` (Produção) - Isolada

**Health Checks**:
- Database: `sqlplus -L sys/pass@localhost/xe`
- Application: `curl -f http://localhost:8080/health`

---

## 📸 Evidências do Funcionamento

### Execução
https://github.com/joaomelara/comped/actions/runs/24753938439/job/72422991110

### Deploy Staging
https://github.com/joaomelara/comped/actions/runs/24753938439/job/72423134377

### Deploy Prod
https://github.com/joaomelara/comped/actions/runs/24753938439/job/72423189261

---

## 🛠️ Tecnologias Utilizadas

### Backend

| Categoria | Tecnologia | Versão |
|-----------|-----------|---------|
| **Linguagem** | Java | 21 |
| **Framework** | Spring Boot | 3.x |
| **Web** | Spring Web | 3.x |
| **ORM** | Spring Data JPA | 3.x |
| **Banco de Dados** | Oracle XE | 21c |
| **Build** | Maven | 3.8+ |
| **Testing** | JUnit 5 | 5.x |
| **Migrations** | Flyway | 9.x |

### DevOps & Containers

| Categoria | Tecnologia | Função |
|-----------|-----------|--------|
| **Containerização** | Docker | Empacotamento |
| **Orquestração** | Docker Compose | Múltiplos serviços |
| **CI/CD** | GitHub Actions | Automação |
| **Registry** | GitHub Container Registry (GHCR) | Armazenamento de imagens |
| **SCM** | Git/GitHub | Controle de versão |
| **Deploy** | SSH | Acesso remoto seguro |

### Stack Completo

```
Frontend (Não incluído neste projeto)
    ↓
REST API (Spring Boot)
    ↓
ORM (Spring Data JPA)
    ↓
Banco de Dados (Oracle XE)
    ↓
Container (Docker)
    ↓
Orquestração (Docker Compose)
    ↓
CI/CD (GitHub Actions)
    ↓
Registry (GHCR)
    ↓
Deployment (SSH)
```

### Dependências do Maven

```xml
<!-- Spring Boot Web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Oracle JDBC -->
<dependency>
    <groupId>com.oracle.database.jdbc</groupId>
    <artifactId>ojdbc11</artifactId>
</dependency>

<!-- JUnit 5 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

---

## 📊 Endpoints da API

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| GET | `/health` | Health check | ✅ |
| POST | `/api/usuarios` | Criar usuário | ✅ |
| GET | `/api/usuarios` | Listar todos | ✅ |
| GET | `/api/usuarios/{id}` | Obter por ID | ✅ |
| PUT | `/api/usuarios/{id}` | Atualizar | ✅ |
| DELETE | `/api/usuarios/{id}` | Deletar | ✅ |

---

## 📚 Documentação Complementar

| Arquivo | Descrição |
|---------|-----------|
| [`QUICKSTART.md`](QUICKSTART.md) | Início rápido em 5 minutos |
| [`DEPLOYMENT.md`](DEPLOYMENT.md) | Guia completo de deployment |
| [`SETUP_GITHUB_ACTIONS.md`](SETUP_GITHUB_ACTIONS.md) | Setup GitHub Actions passo a passo |
| [`ARCHITECTURE.md`](ARCHITECTURE.md) | Diagrama de arquitetura detalhado |
| [`DEVOPS_SUMMARY.md`](DEVOPS_SUMMARY.md) | Resumo executivo |

---

## ✅ Requisitos Atendidos

- ✅ Pipeline CI/CD com GitHub Actions
- ✅ Build automático (Maven)
- ✅ Testes automatizados (21 testes)
- ✅ Deploy em Staging (automático)
- ✅ Deploy em Produção (manual/tag)
- ✅ Dockerfile otimizado
- ✅ Docker Compose (3 ambientes)
- ✅ Orquestração completa
- ✅ Health checks
- ✅ Documentação técnica

---

**Data**: 22/04/2026 | **Status**: ✅ PRONTO PARA PRODUÇÃO

