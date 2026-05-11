# language: pt

Funcionalidade: Gerenciamento de setores
  Como usuário da API
  Quero gerenciar setores
  Para que os registros sejam salvos corretamente no sistema

  Cenário: Cadastro bem-sucedido de setor
    Dado que eu tenha os seguintes dados do setor:
      | campo | valor        |
      | nomeSetor  | Tecnologia   |
    Quando eu enviar a requisição para o endpoint "/setores" de cadastro de setores
    Então o status code da resposta de setor deve ser 201

  Cenário: Tentativa de cadastro de setor sem nome
    Dado que eu tenha os seguintes dados do setor:
      | campo | valor |
      | nomeSetor  |       |
    Quando eu enviar a requisição para o endpoint "/setores" de cadastro de setores
    Então o status code da resposta de setor deve ser 400

  Cenário: Listagem de todos os setores
    Dado que existe pelo menos um setor cadastrado no sistema
    Quando eu enviar a requisição para o endpoint "/setores" de listagem de setores
    Então o status code da resposta de setor deve ser 200