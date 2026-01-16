# Gerenciador de Sessões de Votação

Aplicação para gerenciamento de sessões de votação em pautas, desenvolvida utilizando **Java 17**, **Spring Boot 3**, arquitetura **Hexagonal** e princípios de **DDD**.

A solução contempla o ciclo completo de votação: cadastro de pautas, abertura de sessões (com tempo limite), registro de votos e apuração automática de resultados.

## 🚀 Tecnologias Utilizadas

*   **Java 17**
*   **Spring Boot 3.3** (Web, Data JPA, Validation, AMQP, Cache)
*   **PostgreSQL** (Banco de dados relacional)
*   **Flyway** (Versionamento de banco de dados)
*   **RabbitMQ** (Mensageria para fechamento assíncrono de sessões)
*   **Redis** (Cache distribuído para performance)
*   **Docker & Docker Compose** (Orquestração de containers)
*   **Swagger / OpenAPI** (Documentação da API)

---

## 🛠️ Como Executar

### Pré-requisitos
*   Docker e Docker Compose instalados.
*   JDK 17 instalado.
*   Maven (ou utilizar o wrapper `mvnw` incluso).

### 1. Subir a Infraestrutura
Na raiz do projeto, execute o comando abaixo para subir os containers do PostgreSQL, RabbitMQ e Redis:

```bash
docker-compose up -d
```

Aguarde alguns instantes até que todos os serviços estejam saudáveis.

### 2. Executar a Aplicação
Você pode rodar a aplicação via IDE ou linha de comando:

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

---

## 📚 Documentação da API (Swagger)

A documentação interativa completa pode ser acessada em:
👉 **http://localhost:8080/swagger-ui.html**

---

## 🔄 Fluxo de Utilização

Abaixo, o passo a passo para utilizar a API.

### 1. Criar uma Pauta
Cadastra uma nova pauta para ser votada.

*   **Endpoint:** `POST /v1/pautas`
*   **Body:**
    ```json
    {
      "titulo": "Aumento de Salário",
      "descricao": "Votação para aprovação do aumento anual."
    }
    ```
*   **Campos:**
    *   `titulo` (Obrigatório): Título da pauta.
    *   `descricao` (Opcional): Detalhes da pauta.

### 2. Abrir uma Sessão de Votação
Abre uma sessão para uma pauta existente. A sessão ficará aberta pelo tempo estipulado.

*   **Endpoint:** `POST /v1/sessoes`
*   **Body:**
    ```json
    {
      "pautaId": "uuid-da-pauta-criada",
      "tempoEmMinutos": 10
    }
    ```
*   **Campos:**
    *   `pautaId` (Obrigatório): ID da pauta criada no passo anterior.
    *   `tempoEmMinutos` (Opcional): Tempo de duração da sessão. Se não informado ou for 0, o padrão é **1 minuto**.

> **Nota:** O sistema agendará automaticamente o fechamento da sessão utilizando RabbitMQ.

### 3. Registrar Voto
Registra o voto de um associado em uma sessão aberta.

*   **Endpoint:** `POST /v1/sessoes/{sessaoId}/votos`
*   **Body:**
    ```json
    {
      "cpfAssociado": "12345678901",
      "voto": true
    }
    ```
*   **Campos:**
    *   `cpfAssociado` (Obrigatório): CPF com 11 dígitos (apenas números). Um CPF só pode votar uma vez por sessão.
    *   `voto` (Obrigatório): `true` para SIM, `false` para NÃO.

> **Regras:**
> *   Não é possível votar se a sessão estiver fechada ou expirada.
> *   Não é possível votar duas vezes com o mesmo CPF na mesma sessão.

### 4. Consultar Resultado
Após o fechamento da sessão, o resultado é apurado automaticamente e pode ser consultado.

*   **Endpoint:** `GET /v1/sessoes/{sessaoId}/resultado`
*   **Resposta:**
    ```json
    {
      "sessaoId": "uuid-da-sessao",
      "resultado": "APROVADA",
      "quantidadeSim": 10,
      "quantidadeNao": 5,
      "quantidadeVotos": 15
    }
    ```

> **Nota:** Se tentar consultar o resultado de uma sessão que ainda está aberta, a API retornará **404 Not Found**.

---

## 🏗️ Arquitetura

O projeto segue a **Arquitetura Hexagonal (Ports and Adapters)**:

*   **Core (Domínio)**: Contém as regras de negócio, entidades e interfaces (Portas). Não depende de frameworks externos.
*   **Adapters In**: Controladores REST (`web`) e Listeners AMQP (`amqp`).
*   **Adapters Out**: Implementações de persistência (`jpa`) e publicadores de mensagens (`amqp`).
