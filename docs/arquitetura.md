# Arquitetura do Sistema

## Camadas

- `controller`: recebe as requisições HTTP.
- `service`: concentra regras de negócio.
- `repository`: acesso ao banco de dados usando Spring Data JPA.
- `model`: entidades JPA.
- `dto`: objetos usados para entrada e saída da API.
- `security`: autenticação JWT.
- `exception`: tratamento global de erros.

## Modelo principal

- `AppUser`: usuário do sistema.
- `Household`: carteira financeira, podendo ser individual ou compartilhada.
- `HouseholdMember`: vínculo entre usuário e carteira.
- `Category`: categoria de receita ou despesa.
- `TransactionEntry`: receita ou despesa.
- `Debt`: dívida parcelada.
- `DebtInstallment`: parcela da dívida.
- `Goal`: meta financeira.
- `Notification`: lembrete de vencimento.

## Regras importantes

- Toda transação pertence a uma carteira.
- O usuário só acessa carteiras das quais é membro.
- Apenas o dono da carteira pode adicionar outro membro.
- A categoria precisa pertencer à mesma carteira da transação.
- Dívidas geram parcelas automaticamente.
- Relatórios mensais são calculados por período.
