# language: pt

Funcionalidade: Gerenciamento de consumos
  Como usuário da API
  Quero gerenciar consumos
  Para que os registros sejam salvos corretamente no sistema

  Cenário: Cadastro bem-sucedido de consumo
    Dado que eu tenha os seguintes dados do consumo:
      | campo       | valor                          |
      | equipId     | 3                              |
      | dataConsumo | now |
      | kwhConsumo  | 200.0                          |
    Quando eu enviar a requisição para o endpoint "/consumos" de cadastro de consumos
    Então o status code da resposta de consumo deve ser 201

  Cenário: Tentativa de cadastro de consumo sem data
    Dado que eu tenha os seguintes dados do consumo:
      | campo       | valor |
      | equipId     | 3     |
      | dataConsumo |       |
      | kwhConsumo  | 200.0 |
    Quando eu enviar a requisição para o endpoint "/consumos" de cadastro de consumos
    Então o status code da resposta de consumo deve ser 400

  Cenário: Listagem de consumos por equipamento
    Dado que existe pelo menos um consumo cadastrado no sistema
    Quando eu enviar a requisição para o endpoint "/consumos" de listagem de consumos
    Então o status code da resposta de consumo deve ser 200