CREATE DATABASE IF NOT EXISTS CNAB_CODES;

CREATE TABLE IF NOT EXISTS arquivo(
	id int PRIMARY KEY auto_increment,
	name varchar(255),
	extension varchar(255),
	fileSize varchar(255),
	quantidade int,
	situacao varchar(255),
	arquivo blob,
	valorTotal decimal,
	observacao varchar(255),
	dataCadastro datetime
);

CREATE TABLE IF NOT EXISTS cnab_erro(
  	id SERIAL PRIMARY KEY,
  	message varchar(255),
  	line varchar(255),
  	lineNumber int,
  	erro varchar(255),
  	idArquivo int,
	dataCadastro datetime,
    foreign key (idArquivo) references arquivo(id)
);

CREATE TABLE IF NOT EXISTS cnab(
    id SERIAL primary key,
    identRegistro varchar(255),
    agenciaDebito varchar(255),
    digitoAgencia varchar(255),
    razaoAgencia varchar(255),
    contaCorrente varchar(255),
    digitoConta varchar(255),
    identBeneficiario varchar(255),
    controleParticipante varchar(255),
    codigoBanco varchar(255),
    campoMulta int,
    percentualMulta decimal,
    nossoNumero int,
    digitoConferenciaNumeroBanco varchar(255),
    descontoDia int,
    condicaoEmpissaoPapeladaCobranca int,
    boletoDebitoAutomatico varchar(255),
    identificacaoOcorrencia int,
    numeroDocumento varchar(255),
    dataVencimento date,
    valorTitulo decimal,
    especieTitulo int,
    dataEmissao date,
    primeiraInstrucao varchar(255),
    segundaInstrucao varchar(255),
    moraDia decimal,
    dataLimiteDescontoConcessao date,
    valorDesconto decimal,
    valorIOF decimal,
    valorAbatimento varchar(255),
    tipoPagador int,
    nomePagador varchar(255),
    endereco varchar(255),
    primeiraMensagem varchar(255),
    cep varchar(255),
    sufixoCEP varchar(255),
    segundaMensagem varchar(255),
    sequencialRegistro varchar(255),
    idArquivo int,
    dataCadastro datetime,
    foreign key (idArquivo) references arquivo(id)
);