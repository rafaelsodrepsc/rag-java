# RAG Java

- API de perguntas e respostas baseada em documentos, usando Java 21, Spring Boot e LangChain4j.

## Como funciona

1. Usuário envia um documento (PDF ou TXT)
2. O documento é dividido em chunks com overlap
3. Cada chunk vira um embedding gerado pela OpenAI
4. Os embeddings são salvos no pgvector
5. Usuário faz uma pergunta
6. A pergunta vira um embedding e é comparada com os chunks salvos
7. Os chunks mais similares são enviados ao GPT como contexto
8. O GPT responde baseado apenas no conteúdo do documento

## Stack

- Java 21
- Spring Boot 3.5.1
- LangChain4j 0.36.2
- OpenAI (text-embedding-3-small + gpt-4o-mini)
- PostgreSQL + pgvector

## Pré-requisitos

- Java 21
- Docker
- Chave de API da OpenAI

## Como rodar

**1. Suba o banco de dados:**
```bash
docker run --name pgvector -e POSTGRES_DB=ragdb -e POSTGRES_USER=raguser -e POSTGRES_PASSWORD=ragpass -p 5432:5432 -v pgvector_data:/var/lib/postgresql/data -d pgvector/pgvector:pg16
```

**2. Configure a variável de ambiente no IntelliJ:**

- OPENAI_API_KEY=sua-chave-aqui

**3. Rode o projeto pelo IntelliJ**

## Endpoints

**Enviar documento**
```bash
curl -X POST http://localhost:8080/documents/upload -F "file=@seu-arquivo.txt"
```

**Fazer pergunta**
```bash
curl -X POST http://localhost:8080/chat \
  -H "Content-Type: application/json" \
  -d "{\"question\":\"sua pergunta aqui\"}"
```

## Estrutura do projeto
```
src/main/java/com/rafael/rag_java/
├── config/          # Configurações do LangChain4j e tratamento de erros
├── ingestion/       # Upload e processamento de documentos
└── retrieval/       # Chat e busca por similaridade
```