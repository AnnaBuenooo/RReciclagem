#!/usr/bin/env bash
set -euo pipefail

if [[ $# -lt 1 ]]; then
  echo "Uso: ./deploy/deploy.sh <staging|prod>"
  exit 1
fi

ENVIRONMENT="$1"
if [[ "$ENVIRONMENT" != "staging" && "$ENVIRONMENT" != "prod" ]]; then
  echo "Ambiente invalido: $ENVIRONMENT"
  exit 1
fi

COMPOSE_FILES="-f docker-compose.yml"
if [[ "$ENVIRONMENT" == "staging" ]]; then
  COMPOSE_FILES="$COMPOSE_FILES -f docker-compose.staging.yml"
else
  COMPOSE_FILES="$COMPOSE_FILES -f docker-compose.prod.yml"
fi

if [[ ! -f .env ]]; then
  echo "Arquivo .env nao encontrado. Copie .env.example para .env e preencha os valores."
  exit 1
fi

echo "[deploy] Ambiente: $ENVIRONMENT"

# shellcheck disable=SC2086
docker compose $COMPOSE_FILES pull
# shellcheck disable=SC2086
docker compose $COMPOSE_FILES up -d --remove-orphans
# shellcheck disable=SC2086
docker compose $COMPOSE_FILES ps
