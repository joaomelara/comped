# language: pt
Funcionalidade: Gerenciamento de setores
  Como usuário da API
  Quero gerenciar setores
  Para que os registros de setores sejam salvos corretamente no sistema

  Cenário: Cadastro bem-sucedido de setor
    Dado que eu tenha os seguintes dados do setor:
      | campo      | valor            |
      | nomeSetor  | Administrativo   |
    Quando eu enviar a requisição para o endpoint "/setores" de cadastro de setores
    Então o status code da resposta deve ser 201

  Cenário: Tentativa de cadastro com nome duplicado
    Dado que existe um setor cadastrado no sistema
    E que eu tenha os seguintes dados do setor:
      | campo      | valor           |
      | nomeSetor  | Administrativo  |
    Quando eu enviar a requisição para o endpoint "/setores" de cadastro de setores
    Então o status code da resposta deve ser 409

  Cenário: Tentativa de cadastro com nome em branco
    Dado que eu tenha os seguintes dados do setor:
      | campo      | valor  |
      | nomeSetor  |        |
    Quando eu enviar a requisição para o endpoint "/setores" de cadastro de setores
    Então o status code da resposta deve ser 400

  Cenário: Listagem de todos os setores
    Dado que existe pelo menos um setor cadastrado no sistema
    Quando eu enviar a requisição para o endpoint "/setores" de listagem de setores
    Então o status code da resposta deve ser 200

  Cenário: Listagem de setores com paginação
    Dado que existe pelo menos um setor cadastrado no sistema
    Quando eu enviar a requisição para o endpoint "/setores?page=0&size=10" de listagem de setores com paginação
    Então o status code da resposta deve ser 200

  Cenário: Listagem de setores por filtro de nome
    Dado que existe um setor com nome "Financeiro" cadastrado no sistema
    Quando eu enviar a requisição para o endpoint "/setores?nome=Financeiro" de listagem de setores com filtro
    Então o status code da resposta deve ser 200

