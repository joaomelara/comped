# language: pt
Funcionalidade: Gerenciamento de consumos
  Como usuário da API
  Quero gerenciar consumos
  Para que os registros sejam salvos corretamente no sistema

  Cenário: Cadastro bem-sucedido de consumo
    Dado que eu tenha os seguintes dados do consumo:
      | campo          | valor                            |
      | equipamentoId  | 1                                |
      | dataConsumo    | 2025-12-27T04:50:49.438530490Z   |
      | kwhConsumo     | 200                              |
    Quando eu enviar a requisição para o endpoint "/consumos" de cadastro de consumos
    Então o status code da resposta de consumo deve ser 201

  Cenário: Tentativa de cadastro de consumo sem nome
    Dado que eu tenha os seguintes dados do consumo:
      | campo          | valor        |
      | equipamentoId  | 1            |
      | dataConsumo    |              |
      | kwhConsumo     | 200          |
    Quando eu enviar a requisição para o endpoint "/consumos" de cadastro de consumos
    Então o status code da resposta de consumo deve ser 400

  Cenário: Listagem de todos os consumos
    Dado que existe pelo menos um consumo cadastrado no sistema
    Quando eu enviar a requisição para o endpoint "/consumos" de listagem de consumos
    Então o status code da resposta de consumo deve ser 200