# language: pt

Funcionalidade: Gerenciamento de equipamentos
  Como usuário da API
  Quero gerenciar equipamentos
  Para que os registros sejam salvos corretamente no sistema

  Cenário: Cadastro bem-sucedido de equipamento
    Dado que eu tenha os seguintes dados do equipamento:
      | campo            | valor              |
      | nomeEquipamento  | Ar Condicionado    |
      | consumoKwh       | 15.5               |
      | ativo            | true               |
      | setorId          | 1                  |
    Quando eu enviar a requisição para o endpoint "/equipamentos" de cadastro de equipamentos
    Então o status code da resposta de equipamento deve ser 201

  Cenário: Tentativa de cadastro sem nome do equipamento
    Dado que eu tenha os seguintes dados do equipamento:
      | campo            | valor |
      | nomeEquipamento  |       |
      | consumoKwh       | 15.5  |
      | ativo            | true  |
      | setorId          | 1     |
    Quando eu enviar a requisição para o endpoint "/equipamentos" de cadastro de equipamentos
    Então o status code da resposta de equipamento deve ser 400

  Cenário: Listagem de equipamentos
    Dado que existe pelo menos um equipamento cadastrado no sistema
    Quando eu enviar a requisição para o endpoint "/equipamentos" de listagem de equipamentos
    Então o status code da resposta de equipamento deve ser 200

  Cenário: Atualização de equipamento existente
    Dado que existe pelo menos um equipamento cadastrado no sistema
    E que eu tenha os seguintes dados atualizados do equipamento:
      | campo            | valor            |
      | nomeEquipamento  | Impressora Nova  |
      | consumoKwh       | 8.0              |
      | ativo            | false            |
      | setorId          | 1                |
    Quando eu enviar a requisição de atualização para o endpoint "/equipamentos" de equipamentos
    Então o status code da resposta de equipamento deve ser 200