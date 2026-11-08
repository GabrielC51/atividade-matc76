# API REST de Gerenciamento de Chamados (MATC76 – Trabalho 01)

API REST em Java + Spring Boot para gerenciar chamados de suporte técnico. Os dados ficam em memória (coleção Java), sem banco de dados, Service, Repository ou autenticação.

## Como executar

Requisitos: JDK compatível com o `java.version` do `pom.xml` (atualmente 25).

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.

## Modelo

`Chamado`: `id`, `titulo`, `solicitante`, `setor`, `prioridade` (`BAIXA`, `MEDIA`, `ALTA`), `status` (`ABERTO`, `EM_ANDAMENTO`, `RESOLVIDO`).
A aplicação inicia com 10 chamados fictícios em 6 setores (TI, Financeiro, Desenvolvimento, RH, Comercial, Marketing), com prioridades e status variados.

## Status da implementação

### Feito

- [x] Projeto Spring Boot (Maven)
- [x] Modelo `Chamado` e enums `Prioridade` e `Status`
- [x] 10 chamados fictícios em memória
- [x] `GET /chamados` – lista todos
- [x] `GET /chamados/{id}` – consulta por ID (404 se não existir)
- [x] `GET /chamados/setor?nome=financeiro` – filtro por setor (sem diferenciar maiúsculas/minúsculas)
- [x] `GET /chamados/prioridade?nivel=alta` – filtro por prioridade (sem diferenciar maiúsculas/minúsculas)
- [x] `GET /chamados/busca?texto=impressora` – busca no título (sem diferenciar maiúsculas/minúsculas)
- [x] `GET /chamados/criticos` – prioridade ALTA e status diferente de RESOLVIDO
- [x] Seção de análise técnica

### Falta fazer

- [ ] `GET /chamados/resumo` – quantidades por status (abertos, em andamento, resolvidos e total)
- [ ] `POST /chamados` – cadastro a partir de JSON, com ID único gerado e retorno HTTP 201
- [ ] Evidências de pelo menos 8 requisições (requisição, resposta HTTP/JSON e explicação), incluindo consultas, filtros, casos sem correspondência, cadastro e verificação do cadastro

## Testes realizados (parcial)

Exemplos já verificados com a aplicação em execução:

| Requisição | Resultado |
|---|---|
| `GET /chamados/busca?texto=IMPRESSORA` | Retorna os chamados 4 e 7, mostrando que a busca ignora maiúsculas/minúsculas |
| `GET /chamados/busca?texto=zzz` | Retorna `[]` (sem correspondência) |
| `GET /chamados/criticos` | Retorna os chamados 1 (ABERTO) e 2 (EM_ANDAMENTO); o chamado 3 é ALTA, mas está RESOLVIDO e fica de fora |

As demais evidências serão adicionadas aqui.

## Análise técnica

### 1. Como o Spring Boot recebe e processa requisições HTTP

Ao iniciar, o Spring Boot sobe um servidor web embutido (Tomcat), que fica escutando a porta 8080. Quando chega uma requisição, o Tomcat a entrega ao `DispatcherServlet`, o ponto de entrada do Spring MVC. Ele consulta os mapeamentos registrados pelas anotações (`@GetMapping`, `@PostMapping` etc.) e escolhe o método do controller que corresponde ao verbo HTTP e ao caminho. Depois, resolve os argumentos do método: `@PathVariable` e `@RequestParam` vêm da URL, e `@RequestBody` vem do corpo da requisição. Em seguida, invoca o método e usa o valor retornado para montar a resposta HTTP, com status, cabeçalhos e corpo.

Exemplo: `GET /chamados/setor?nome=financeiro` é encaminhado para `buscarPorSetor`, que recebe `"financeiro"` em `nome`.

### 2. Função do Controller

O Controller é a camada que expõe a API. Ele define quais URLs e verbos a aplicação atende, recebe os dados da requisição, executa a lógica e devolve a resposta. Neste projeto, `ChamadoController` é anotado com `@RestController` e `@RequestMapping("/chamados")`. Ele também guarda a lista de chamados e faz os filtros, as buscas e o cadastro, já que o enunciado dispensa Service e Repository. Usar `ResponseEntity` permite controlar o status HTTP, como o 404 em `/chamados/{id}` quando o chamado não existe.

### 3. Conversão entre objetos Java e JSON

A conversão é feita pela biblioteca Jackson, incluída pelo starter web do Spring Boot e acionada por um `HttpMessageConverter`. `@RestController` equivale a `@Controller` + `@ResponseBody`: o retorno do método não é tratado como nome de página, e sim serializado para o corpo da resposta. Assim, um `Chamado` ou uma `List<Chamado>` vira JSON, com os campos do `record` como chaves e os enums como texto (`"ALTA"`, `"ABERTO"`). No sentido inverso, com `@RequestBody`, o Jackson lê o JSON enviado no `POST` e cria um objeto `Chamado`, desde que os nomes dos campos coincidam e o cabeçalho `Content-Type` seja `application/json`.

### 4. Por que os chamados cadastrados não persistem após reiniciar

Os chamados ficam em uma `List` Java, um objeto na memória RAM da JVM, e não em um banco de dados ou arquivo. Um chamado cadastrado via `POST` é apenas adicionado a essa lista. Quando a aplicação é encerrada, o processo termina e a memória é liberada, então a lista deixa de existir. Ao reiniciar, o controller é criado de novo e a lista volta ao estado inicial, com os 10 chamados fictícios. Para persistir os dados seria necessário um banco (por exemplo, JPA com PostgreSQL) ou gravação em arquivo, o que o enunciado dispensa.

## Integrantes
Gabriel C., Nathalia M. e Thiago F.