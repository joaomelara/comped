# language: pt
Funcionalidade: Gerenciamento de usuários
  Como usuário da API
  Quero gerenciar usuários
  Para que os registros sejam salvos corretamente no sistema

  Cenário: Cadastro bem-sucedido de usuário
    Dado que eu tenha os seguintes dados do usuário:
      | campo         | valor              |
      | nomeUsuario   | João Silva         |
      | emailUsuario  | joao@email.com     |
      | senhaUsuario  | Senha@123          |
      | role          | USER               |
    Quando eu enviar a requisição para o endpoint "/api/usuarios" de cadastro de usuários
    Então o status code da resposta deve ser 201

  Cenário: Tentativa de cadastro com email inválido
    Dado que eu tenha os seguintes dados do usuário:
      | campo         | valor              |
      | nomeUsuario   | João Silva         |
      | emailUsuario  | email-invalido      |
      | senhaUsuario  | Senha@123          |
      | role          | USER               |
    Quando eu enviar a requisição para o endpoint "/api/usuarios" de cadastro de usuários
    Então o status code da resposta deve ser 400

  Cenário: Busca de usuário por ID inexistente
    Dado que não existe usuário com ID 9999
    Quando eu enviar a requisição de busca para o endpoint "/api/usuarios/9999"
    Então o status code da resposta deve ser 404

  Cenário: Listagem de todos os usuários
    Dado que existe pelo menos um usuário cadastrado no sistema
    Quando eu enviar a requisição para o endpoint "/api/usuarios" de listagem de usuários
    Então o status code da resposta deve ser 200

  Cenário: Atualização de dados de um usuário existente
    Dado que existe um usuário cadastrado no sistema
    E que eu tenha os seguintes dados atualizados:
      | campo         | valor                   |
      | nomeUsuario   | João Atualizado         |
      | emailUsuario  | joao_novo@email.com     |
      | senhaUsuario  | Senha@123               |
      | role          | USER                    |
    Quando eu enviar a requisição de atualização para o endpoint "/api/usuarios/{id}"
    Então o status code da resposta deve ser 200

  Cenário: Exclusão de usuário existente
    Dado que existe um usuário cadastrado no sistema
    Quando eu enviar a requisição de exclusão para o endpoint "/api/usuarios/{id}"
    Então o status code da resposta deve ser 204