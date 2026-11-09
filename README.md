# API REST de Gerenciamento de Chamados
**MATC76 – Trabalho 01**

## 1. Introdução

Este projeto consiste no desenvolvimento de uma API REST utilizando Java e Spring Boot para o gerenciamento de chamados de suporte técnico.

A aplicação permite cadastrar, consultar, listar, filtrar e pesquisar chamados, utilizando requisições HTTP e respostas no formato JSON.

Os dados são armazenados exclusivamente em memória, por meio de uma coleção Java, sem utilização de banco de dados, camadas Service ou Repository e mecanismos de autenticação, conforme as especificações do trabalho.

## 2. Como executar

### Requisitos
- JDK compatível com a versão definida no `pom.xml` (atualmente Java 25).
- Maven Wrapper incluído no projeto.

### Inicialização

Na pasta raiz do projeto, execute:

```bash
./mvnw spring-boot:run
```

Após a inicialização, a API estará disponível em:

```text
http://localhost:8080
```

## 3. Modelo de dados

A aplicação utiliza a classe `Chamado`, composta pelos seguintes atributos:

| Atributo | Descrição |
|---|---|
| `id` | Identificador único do chamado |
| `titulo` | Descrição resumida do problema |
| `solicitante` | Nome de quem abriu o chamado |
| `setor` | Departamento relacionado ao chamado |
| `prioridade` | Nível de prioridade |
| `status` | Situação atual do chamado |

### 3.1. Enum Prioridade

Representa o nível de prioridade de cada chamado:

- `BAIXA`
- `MEDIA`
- `ALTA`

### 3.2. Enum Status

Representa a situação atual do chamado:

- `ABERTO`
- `EM_ANDAMENTO`
- `RESOLVIDO`

### 3.3. Dados iniciais

A aplicação é inicializada com 10 chamados fictícios, distribuídos entre seis setores:

- TI
- Financeiro
- Desenvolvimento
- RH
- Comercial
- Marketing

Os registros apresentam diferentes níveis de prioridade e situações, permitindo testar os filtros e as consultas implementadas.

## 4. Endpoints implementados

| Método | Endpoint | Funcionalidade |
|---|---|---|
| GET | `/chamados` | Lista todos os chamados |
| GET | `/chamados/{id}` | Consulta um chamado pelo ID |
| GET | `/chamados/setor?nome=financeiro` | Filtra chamados por setor |
| GET | `/chamados/prioridade?nivel=alta` | Filtra chamados por prioridade |
| GET | `/chamados/busca?texto=impressora` | Pesquisa pelo título |
| GET | `/chamados/criticos` | Lista chamados de prioridade alta que não foram resolvidos |
| GET | `/chamados/resumo` | Apresenta a quantidade de chamados por status |
| POST | `/chamados` | Cadastra um novo chamado |

As pesquisas por setor, prioridade e título ignoram diferenças entre letras maiúsculas e minúsculas.

## 5. Evidências de execução

Todas as requisições apresentadas nesta seção foram executadas com a aplicação em funcionamento, utilizando o comando:

```bash
./mvnw spring-boot:run
```

O endereço utilizado foi `http://localhost:8080`, com o auxílio do comando `curl -i`, que permite visualizar o status HTTP, os cabeçalhos e o corpo das respostas.

### 5.1. GET /chamados — Listar todos os chamados

**Requisição:**

```http
GET http://localhost:8080/chamados
```

**Resposta:**

```json
[
  {"id":1,"titulo":"Servidor de banco de dados inacessível","solicitante":"Bruno Cardoso","setor":"TI","prioridade":"ALTA","status":"ABERTO"},
  {"id":2,"titulo":"Queda do link de internet do escritório","solicitante":"Camila Ribeiro","setor":"TI","prioridade":"ALTA","status":"EM_ANDAMENTO"},
  {"id":3,"titulo":"Conta de administrador possivelmente comprometida","solicitante":"Diego Martins","setor":"TI","prioridade":"ALTA","status":"RESOLVIDO"},
  {"id":4,"titulo":"Impressora fiscal da recepção sem papel","solicitante":"Elisa Prado","setor":"Financeiro","prioridade":"MEDIA","status":"ABERTO"},
  {"id":5,"titulo":"Notebook com superaquecimento e desligamentos","solicitante":"Felipe Andrade","setor":"Desenvolvimento","prioridade":"MEDIA","status":"EM_ANDAMENTO"},
  {"id":6,"titulo":"Redefinição de senha do ERP financeiro","solicitante":"Gabriela Costa","setor":"Financeiro","prioridade":"MEDIA","status":"RESOLVIDO"},
  {"id":7,"titulo":"Troca de toner da impressora do RH","solicitante":"Henrique Lopes","setor":"RH","prioridade":"BAIXA","status":"ABERTO"},
  {"id":8,"titulo":"Wi-Fi instável na sala de reuniões","solicitante":"Isabela Freitas","setor":"Comercial","prioridade":"BAIXA","status":"EM_ANDAMENTO"},
  {"id":9,"titulo":"Instalação de segundo monitor","solicitante":"Jonas Vieira","setor":"Marketing","prioridade":"BAIXA","status":"RESOLVIDO"},
  {"id":10,"titulo":"Solicitação de acesso à VPN corporativa","solicitante":"Karina Duarte","setor":"Desenvolvimento","prioridade":"MEDIA","status":"ABERTO"}
]
```

**Explicação:** A requisição retorna todos os chamados cadastrados inicialmente na aplicação, apresentando seus identificadores, títulos, solicitantes, setores, prioridades e status.

### 5.2. GET /chamados/4 — Consultar chamado pelo ID

**Requisição:**

```http
GET http://localhost:8080/chamados/4
```

**Resposta:**

```json
{
  "id": 4,
  "titulo": "Impressora fiscal da recepção sem papel",
  "solicitante": "Elisa Prado",
  "setor": "Financeiro",
  "prioridade": "MEDIA",
  "status": "ABERTO"
}
```

**Explicação:** A API localiza e retorna os dados do chamado cujo identificador é 4.

### 5.3. GET /chamados/999 — Consultar chamado inexistente

**Requisição:**

```http
GET http://localhost:8080/chamados/999
```

**Resposta:**

```json
{
  "timestamp": "2026-11-09T19:19:09.045Z",
  "status": 404,
  "error": "Not Found",
  "path": "/chamados/999"
}
```

**Explicação:** Como não existe nenhum chamado com o ID informado, a aplicação retorna o código HTTP 404 (Not Found), indicando que o recurso solicitado não foi encontrado.

### 5.4. GET /chamados/setor?nome=financeiro — Filtrar por setor

**Requisição:**

```http
GET http://localhost:8080/chamados/setor?nome=financeiro
```

**Resposta:**

```json
[
  {
    "id": 4,
    "titulo": "Impressora fiscal da recepção sem papel",
    "solicitante": "Elisa Prado",
    "setor": "Financeiro",
    "prioridade": "MEDIA",
    "status": "ABERTO"
  },
  {
    "id": 6,
    "titulo": "Redefinição de senha do ERP financeiro",
    "solicitante": "Gabriela Costa",
    "setor": "Financeiro",
    "prioridade": "MEDIA",
    "status": "RESOLVIDO"
  }
]
```

**Explicação:** A API retorna somente os chamados pertencentes ao setor Financeiro. A pesquisa não diferencia letras maiúsculas e minúsculas, permitindo informar `financeiro` mesmo que o setor esteja cadastrado como `Financeiro`.

### 5.5. GET /chamados/prioridade?nivel=alta — Filtrar por prioridade

**Requisição:**

```http
GET http://localhost:8080/chamados/prioridade?nivel=alta
```

**Resposta:**

```json
[
  {
    "id": 1,
    "titulo": "Servidor de banco de dados inacessível",
    "solicitante": "Bruno Cardoso",
    "setor": "TI",
    "prioridade": "ALTA",
    "status": "ABERTO"
  },
  {
    "id": 2,
    "titulo": "Queda do link de internet do escritório",
    "solicitante": "Camila Ribeiro",
    "setor": "TI",
    "prioridade": "ALTA",
    "status": "EM_ANDAMENTO"
  },
  {
    "id": 3,
    "titulo": "Conta de administrador possivelmente comprometida",
    "solicitante": "Diego Martins",
    "setor": "TI",
    "prioridade": "ALTA",
    "status": "RESOLVIDO"
  }
]
```

**Explicação:** São retornados todos os chamados classificados com prioridade ALTA, independentemente de seu status atual.

### 5.6. GET /chamados/busca?texto=IMPRESSORA — Pesquisar pelo título

**Requisição:**

```http
GET http://localhost:8080/chamados/busca?texto=IMPRESSORA
```

**Resposta:**

```json
[
  {
    "id": 4,
    "titulo": "Impressora fiscal da recepção sem papel",
    "solicitante": "Elisa Prado",
    "setor": "Financeiro",
    "prioridade": "MEDIA",
    "status": "ABERTO"
  },
  {
    "id": 7,
    "titulo": "Troca de toner da impressora do RH",
    "solicitante": "Henrique Lopes",
    "setor": "RH",
    "prioridade": "BAIXA",
    "status": "ABERTO"
  }
]
```

**Explicação:** A pesquisa retorna os chamados cujos títulos contêm o termo informado. O resultado demonstra que a busca ignora diferenças entre letras maiúsculas e minúsculas.

### 5.7. GET /chamados/busca?texto=zzz — Pesquisa sem correspondência

**Requisição:**

```http
GET http://localhost:8080/chamados/busca?texto=zzz
```

**Resposta:**

```json
[]
```

**Explicação:** Como nenhum título contém o termo pesquisado, a API retorna uma lista vazia. Isso demonstra o tratamento de consultas sem resultados, sem provocar erros na aplicação.

### 5.8. GET /chamados/criticos — Consultar chamados críticos

**Requisição:**

```http
GET http://localhost:8080/chamados/criticos
```

**Resposta:**

```json
[
  {
    "id": 1,
    "titulo": "Servidor de banco de dados inacessível",
    "solicitante": "Bruno Cardoso",
    "setor": "TI",
    "prioridade": "ALTA",
    "status": "ABERTO"
  },
  {
    "id": 2,
    "titulo": "Queda do link de internet do escritório",
    "solicitante": "Camila Ribeiro",
    "setor": "TI",
    "prioridade": "ALTA",
    "status": "EM_ANDAMENTO"
  }
]
```

**Explicação:** O endpoint retorna apenas chamados com prioridade ALTA e status diferente de RESOLVIDO. Por esse motivo, o chamado de ID 3, apesar de possuir prioridade ALTA, não aparece no resultado, pois já foi resolvido.

### 5.9. GET /chamados/resumo — Consultar resumo dos chamados

**Requisição:**

```http
GET http://localhost:8080/chamados/resumo
```

**Resposta:**

```json
{
  "abertos": 4,
  "emAndamento": 3,
  "resolvidos": 3,
  "total": 10
}
```

**Explicação:** O endpoint apresenta um resumo quantitativo dos chamados, agrupando-os conforme seus respectivos status. Também informa o total de registros existentes na aplicação.

### 5.10. POST /chamados — Cadastrar novo chamado

**Requisição:**

```http
POST http://localhost:8080/chamados
Content-Type: application/json
```

**Corpo enviado:**

```json
{
  "titulo": "Impressora do RH travando",
  "solicitante": "Maria Silva",
  "setor": "RH",
  "prioridade": "MEDIA"
}
```

**Resposta:**

```json
{
  "id": 11,
  "titulo": "Impressora do RH travando",
  "solicitante": "Maria Silva",
  "setor": "RH",
  "prioridade": "MEDIA",
  "status": "ABERTO"
}
```

**Explicação:** A requisição cadastra um novo chamado a partir dos dados enviados em JSON. A aplicação gera automaticamente um identificador único e define o status inicial como ABERTO. A resposta utiliza o código HTTP 201 (Created), indicando que o recurso foi criado com sucesso.

O campo `id` não precisa ser informado pelo cliente, pois é gerado pela aplicação. Da mesma forma, o status inicial é definido automaticamente.

## 6. Análise técnica

### 6.1. Como o Spring Boot recebe e processa requisições HTTP

Ao iniciar, o Spring Boot sobe um servidor web embutido (Tomcat), que fica escutando a porta 8080. Quando chega uma requisição, o Tomcat a entrega ao `DispatcherServlet`, o ponto de entrada do Spring MVC.

O `DispatcherServlet` consulta os mapeamentos registrados pelas anotações (`@GetMapping`, `@PostMapping` etc.) e escolhe o método do controller que corresponde ao verbo HTTP e ao caminho solicitado.

Depois, resolve os argumentos do método: `@PathVariable` e `@RequestParam` vêm da URL, enquanto `@RequestBody` recebe os dados enviados no corpo da requisição.

Em seguida, o método correspondente é executado e seu retorno é utilizado para montar a resposta HTTP, incluindo o status, os cabeçalhos e o corpo.

Exemplo: `GET /chamados/setor?nome=financeiro` é encaminhado para o método `buscarPorSetor`, que recebe o valor `"financeiro"` no parâmetro `nome`.

### 6.2. Função do Controller

O Controller é a camada responsável por expor a API. Ele define quais URLs e verbos HTTP a aplicação atende, recebe os dados das requisições, executa a lógica necessária e devolve as respostas.

Neste projeto, a classe `ChamadoController` é anotada com `@RestController` e `@RequestMapping("/chamados")`.

Ela também armazena a lista de chamados e realiza os filtros, as buscas e os cadastros, uma vez que o enunciado dispensa a utilização das camadas Service e Repository.

O uso de `ResponseEntity` permite controlar o status HTTP, como o 404 em `/chamados/{id}` quando o chamado não existe e o 201 em `POST /chamados`, acompanhado do cabeçalho `Location`, que indica o endereço do recurso criado.

Os atributos `id` e `status` foram declarados utilizando os tipos de referência `Long` e `Status`, respectivamente. Isso permite que recebam valores `null` quando o cliente não os informa no JSON.

Se o atributo `id` fosse declarado como `long` primitivo, o Jackson não conseguiria representar a ausência desse valor durante a desserialização do corpo da requisição, podendo provocar um erro HTTP 400 (Bad Request) no cadastro.

### 6.3. Conversão entre objetos Java e JSON

A conversão entre objetos Java e JSON é realizada pela biblioteca Jackson, incluída pelo Spring Boot Starter Web e acionada por um `HttpMessageConverter`.

A anotação `@RestController` equivale à combinação de `@Controller` e `@ResponseBody`. Isso significa que o retorno dos métodos não é interpretado como o nome de uma página, mas sim serializado para o corpo da resposta HTTP.

Assim, um objeto `Chamado` ou uma `List<Chamado>` é convertido automaticamente para JSON, utilizando os campos do `record` como chaves e os enums como valores textuais, por exemplo `"ALTA"` e `"ABERTO"`.

No sentido inverso, quando utilizamos `@RequestBody`, o Jackson interpreta o JSON enviado pelo cliente e cria um objeto Java `Chamado`, desde que os nomes e os tipos dos campos sejam compatíveis.

Para isso, a requisição deve informar o cabeçalho `Content-Type: application/json`.

### 6.4. Por que os chamados cadastrados não persistem após reiniciar a aplicação

Os chamados ficam armazenados em uma coleção `List` Java, que existe na memória RAM da JVM, e não em um banco de dados ou arquivo externo.

Quando um novo chamado é cadastrado por meio do método POST, ele é simplesmente adicionado a essa lista.

Ao encerrar a aplicação, o processo da JVM termina e os dados armazenados exclusivamente na memória são perdidos.

Quando a aplicação é iniciada novamente, o controller é criado e a lista retorna ao seu estado inicial, contendo os 10 chamados fictícios.

Para manter os dados após reinicializações, seria necessário utilizar algum mecanismo de persistência, como um banco de dados com JPA e PostgreSQL ou a gravação em arquivos. Entretanto, essas alternativas não fazem parte dos requisitos deste trabalho.

## 7. Considerações finais

O desenvolvimento da API permitiu aplicar conceitos fundamentais de desenvolvimento de aplicações web utilizando Java e Spring Boot.

Foram implementadas operações de consulta, pesquisa, filtragem e cadastro de chamados, utilizando os métodos HTTP GET e POST e retornando dados no formato JSON.

Também foram explorados conceitos importantes do protocolo HTTP, como códigos de status, parâmetros de requisição, identificação de recursos e conversão entre objetos Java e JSON.

Por fim, a implementação demonstrou como o Spring MVC organiza o processamento das requisições e como uma aplicação pode disponibilizar funcionalidades REST sem depender de banco de dados ou de camadas adicionais de arquitetura.

## 8. Integrantes

- Gabriel C.
- Nathalia M.
- Thiago F.