# language: pt

Funcionalidade: Gerenciamento de equipamentos
  Como usuário da API
  Quero gerenciar equipamentos monitorados
  Para que os registros sejam salvos corretamente no sistema

  Cenário: Cadastro bem-sucedido de equipamento
    Dado que eu tenha os seguintes dados do equipamento:
      | campo           | valor         |
      | nomeEquipamento | Compressor A1 |
      | setorId         | 1             |
      | dataInstalacao  | 2024-03-10    |
      | limiteKwh       | 150.0         |
    Quando eu enviar a requisição para o endpoint "/equipamentos" de cadastro de equipamentos
    Então o status code da resposta de equipamentos deve ser 201

  Cenário: Tentativa de cadastro com dados inválidos (nome ausente)
    Dado que eu tenha os seguintes dados do equipamento:
      | campo          | valor      |
      | setorId        | 1          |
      | dataInstalacao | 2024-03-10 |
      | limiteKwh      | 50.0       |
    Quando eu enviar a requisição para o endpoint "/equipamentos" de cadastro de equipamentos
    Então o status code da resposta de equipamentos deve ser 400

  Cenário: Listagem de todos os equipamentos
    Dado que existe pelo menos um equipamento cadastrado no sistema
    Quando eu enviar a requisição para o endpoint "/equipamentos" de listagem de equipamentos
    Então o status code da resposta de equipamentos deve ser 200

  Cenário: Atualização de dados de um equipamento existente
    Dado que existe um equipamento cadastrado no sistema
    E que eu tenha os seguintes dados atualizados do equipamento:
      | campo           | valor                    |
      | nomeEquipamento | Compressor A1 Atualizado |
      | limiteKwh       | 200.0                    |
      | ativo           | true                     |
    Quando eu enviar a requisição de atualização para o endpoint "/equipamentos/{id}" de equipamentos
    Então o status code da resposta de equipamentos deve ser 200

  Cenário: Tentativa de atualização de equipamento inexistente
    Quando eu enviar a requisição de atualização para o endpoint "/equipamentos/999999" de equipamento inexistente
    Então o status code da resposta de equipamentos deve ser 404
