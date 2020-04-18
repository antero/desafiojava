# Desafio Java

A API Restful desenvolvida está sendo servida pelo Heroku e pode ser [acessada aqui](https://concrete-java-challenge.herokuapp.com/ "Desafio Java").

Ela fornece quatro _endpoints_, a saber:

* `GET /usuarios` - Retorna uma lista de todos os usuários cadastrados. Este foi adicionado por questões de conveniência;
* `POST /cadastro` - Cadastra um novo usuário e retorna-o em caso de sucesso;
* `POST /login` - Autentica usuário e retorna-o em caso de sucesso; e
* `GET /perfil/{id}` - Retorna informações do usuário cujo id seja {id} (um UUID) após verificar a validade do token JWT enviado no _Request Body_.

### `POST /cadastro`

Esse endpoint recebe aceita uma requisição no seguinte formato:

```json
{
  "name": "João da Silva",
  "email": "joao@silva.org",
  "password": "hunter2",
  "phones": [
    {
      "number": "987654321",
      "ddd": "21"
    }
  ]
}
```

Valida se os campos `name`, `email` e `password` são não vazios e também se o `email` está no formato apropriado.
Caso essa validação não tenha sucesso, retorna o _status code_ `400 Bad Request` com detalhes de erro referentes aos problemas de validação encontrados.

Além da validação, verifica também se já existe algum cadastro que use o `email` fornecido. Caso já exista, retorna o _status code_ `403 Forbidden` com a mensagem apropriada.

### `POST /login`

Espera uma requisição com o _request body_ no seguinte formato:

```json
{
  "email": "joao2@silva.org",
  "password": "hunter2"
}
```

Caso o `email` não seja encontrado na base de dados, retorna o _status code_ `404 Not Found`.

Caso o `password` fornecido seja incorreto, retorna o _status code_ `401 Unauthorized`.

Em caso de sucesso, retorn o usário com o campo `lastLogin` atualizado para o momento atual.

### `GET /perfil/{id}`

A variável `id` deve ser um UUID. Além disso, espera que o corpo da requisição forneça a seguinte informação:

```json
{
	"token": "xxxxx.yyyyy.zzzzz"
}
```

Onde `token` é um token JWT.

Retorna o _status code_ `401 Unauthorized` em três situações:

* Caso o token não seja fornecido;
* Caso não seja válido, após comparação com o token ṕresente no registro do usuário; ou
* Caso o tempo de sessão tenha sido excedido (configurado para 30 minutos).

Também retorna o _status code_ `404 Not Found`, caso o usuário não exista, ou as informações do usuários, caso as informações fornecidas sejam válidas.

















