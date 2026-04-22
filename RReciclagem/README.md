# Projeto - Cidades ESGInteligentes

Aplicação backend Spring Boot para gestão de reciclagem, adaptada para simular um ambiente real de produção com práticas completas de DevOps.

## Como executar localmente com Docker

1. Copie o arquivo de exemplo de variáveis:

```bash
cp .env.example .env
```

2. Ajuste os valores do `.env` (principalmente `JWT_SECRET` e senha do banco).

3. Suba o ambiente local (staging):

```bash
docker compose -f docker-compose.yml -f docker-compose.staging.yml up -d --build
```

4. Verifique containers e saúde da API:

```bash
docker compose ps
curl http://localhost:8080/actuator/health
```

5. Para desligar:

```bash
docker compose -f docker-compose.yml -f docker-compose.staging.yml down
```

## Pipeline CI/CD

Pipeline implementado com **GitHub Actions** no arquivo `.github/workflows/ci-cd.yml`.

### Etapas

1. **Build automático**
- Checkout do código.
- Setup do Java 17.
- Build Maven (`clean package`).

2. **Testes automatizados**
- Execução de testes com `./mvnw -B clean test`.
- Os testes usam perfil `test` com banco H2 em memória.

3. **Publicação de imagem Docker**
- Build da imagem com `docker/build-push-action`.
- Push para GHCR (`ghcr.io/<owner>/<repo>`).

4. **Deploy automatizado em staging**
- Trigger em push para `develop`.
- Deploy remoto por SSH com `./deploy/deploy.sh staging`.

5. **Deploy automatizado em produção**
- Trigger em push para `main` ou tag `v*`.
- Deploy remoto por SSH com `./deploy/deploy.sh prod`.

### Segredos necessários no GitHub

- `STAGING_HOST`
- `STAGING_USER`
- `STAGING_SSH_KEY`
- `STAGING_APP_PATH`
- `REGISTRY_USERNAME`
- `REGISTRY_TOKEN`
- `PRODUCTION_HOST`
- `PRODUCTION_USER`
- `PRODUCTION_SSH_KEY`
- `PRODUCTION_APP_PATH`

Dica: configure `environments` (`staging` e `production`) no GitHub para aprovação manual antes do deploy em produção.

## Containerização

A aplicação foi containerizada com **multi-stage build** para gerar imagem menor e mais segura.

### Dockerfile (resumo)

```dockerfile
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -q -DskipTests dependency:go-offline
COPY src src
RUN ./mvnw -q clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
RUN addgroup --system spring && adduser --system spring --ingroup spring
COPY --from=builder /build/target/RReciclagem-0.0.1-SNAPSHOT.jar app.jar
USER spring
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

### Estratégias adotadas

- Multi-stage build para reduzir tamanho da imagem final.
- Execução com usuário não-root.
- `HEALTHCHECK` para observabilidade de readiness.
- Configuração externa por variáveis de ambiente.
- Perfis de execução (`staging` e `prod`) separados via Compose override.

## Orquestração

Arquivos utilizados:

- `docker-compose.yml`: base da infraestrutura (app + PostgreSQL + volume + rede).
- `docker-compose.staging.yml`: ajustes para staging (debug/limites).
- `docker-compose.prod.yml`: ajustes de produção (recursos/restart/JVM).

Recursos de orquestração implementados:

- Rede dedicada (`rreciclagem-net`).
- Volume persistente (`postgres_data`).
- Variáveis de ambiente para app e banco.
- Dependência com `healthcheck` do banco.

## Prints do funcionamento

Adicione os prints nesta pasta:

- `docs/evidencias/01-ci-build-test.png`
- `docs/evidencias/02-image-push-ghcr.png`
- `docs/evidencias/03-deploy-staging.png`
- `docs/evidencias/04-deploy-production.png`
- `docs/evidencias/05-api-health-local.png`

Opcionalmente, registre também os links dos runs do Actions em `docs/evidencias/links.txt`.

## Tecnologias utilizadas

- Java 17
- Spring Boot 3.2.5
- Spring Security + JWT
- Spring Data JPA
- Flyway
- PostgreSQL
- Docker
- Docker Compose
- GitHub Actions (CI/CD)
