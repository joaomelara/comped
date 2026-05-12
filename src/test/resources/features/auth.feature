# language: pt

Funcionalidade: Autenticação de usuários
  Como usuário da API
  Quero me autenticar e registrar
  Para que eu possa acessar os recursos protegidos do sistema

  Cenário: Login bem-sucedido
    Dado que eu tenha os seguintes dados de autenticação:
      | campo        | valor         |
      | emailUsuario | ZZ@email.com  |
      | senhaUsuario | 123456        |
    Quando eu enviar a requisição para o endpoint "/auth/login" de login
    Então o status code da resposta de auth deve ser 200
    E a resposta deve conter um token

  Cenário: Tentativa de login com credenciais inválidas
    Dado que eu tenha os seguintes dados de autenticação:
      | campo        | valor              |
      | emailUsuario | inexistente@email.com |
      | senhaUsuario | senhaErrada        |
    Quando eu enviar a requisição para o endpoint "/auth/login" de login
    Então o status code da resposta de auth deve ser 401

  Cenário: Registro bem-sucedido de usuário
    Dado que eu tenha os seguintes dados de autenticação:
      | campo        | valor           |
      | nomeUsuario  | Usuário Teste   |
      | emailUsuario | teste@email.com |
      | senhaUsuario | Senha@123       |
      | role         | USER            |
    Quando eu enviar a requisição para o endpoint "/auth/register" de registro
    Então o status code da resposta de auth deve ser 200

  Cenário: Tentativa de registro com email inválido
    Dado que eu tenha os seguintes dados de autenticação:
      | campo        | valor         |
      | nomeUsuario  | Usuário Teste |
      | emailUsuario | email-invalido |
      | senhaUsuario | Senha@123     |
      | role         | USER          |
    Quando eu enviar a requisição para o endpoint "/auth/register" de registro
    Então o status code da resposta de auth deve ser 400
    E o corpo de resposta de erro da api deve retornar a mensagem "Email deve ser um endereço de email válido"

  Cenário: Tentativa de registro sem nome
    Dado que eu tenha os seguintes dados de autenticação:
      | campo        | valor           |
      | emailUsuario | teste@email.com |
      | senhaUsuario | Senha@123       |
      | role         | USER            |
    Quando eu enviar a requisição para o endpoint "/auth/register" de registro
    Então o status code da resposta de auth deve ser 400
