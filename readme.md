# <ins>Documentação Projeto Spring Batch<ins>

## URLS

- Application url: http://localhost:8080/

<br/>

| PATH                | PARAM-NAME | PARAM-TYPE    |
|---------------------|------------|---------------|
| /v1/arquivo/upload/ | file       | multipartfile |
|                     | type       | CNAB400       |

<hr>

| PATH                  | PARAMS  | PARAMS-TYPE |
|-----------------------|---------|-------------|
| /v1/arquivo/download/ | retorno | Boolean     |
|                       | arquivo | Long        |

<hr>

## Tabelas utlizadas

- arquivo
- cnab
- cnab_erro
- transacao

### Arquivo:

* Salva o arquivo com id, nome, extensão, tamanho do arquivo, quantidade de titulos "linhas" dentro, a situação atual, o
  valor total contendo nele, a data que foi cadastrado no banco e o arquivo em base64 comprimido.

### Cnab

* Salva todas as informações referente ao que estava dentro do cnab, adicionando o id do arquivo para ratreabilidade e a
  data de cadastro no sistema.

### Cnab_Erro

* Salva todos os erros que aconteceram no arquivo informando uma mensagem de erro padronizada do sistema que informa o
  erro e a quantidade de caracteres possuia na linha, a linha que ocorreu o erro, o numero da linha, o erro que saí pelo
  sistema, idArquivo para rastreabilidade e a data cadastrado para verificar quando foi incluido no sistema

### Transacao

* Tabela criada para testar fluxo de arquivos diferentes