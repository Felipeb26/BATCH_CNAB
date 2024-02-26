-- Create the 'arquivo' table
IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'arquivo')
BEGIN
    CREATE TABLE arquivo (
        id INT IDENTITY(1,1) PRIMARY KEY,
        nome VARCHAR(255),
        extension VARCHAR(255),
        fileSize VARCHAR(255),
        quantidade INT,
        situacao VARCHAR(255),
        observacao VARCHAR(100),
        valorTotal DECIMAL,
        arquivo VARBINARY(MAX),
        dataCadastro DATETIME,
        dataAtualizacao DATETIME
    );
END

-- Create the 'cnab_erro' table
IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'cnab_erro')
BEGIN
    CREATE TABLE cnab_erro (
        id INT IDENTITY(1,1) PRIMARY KEY,
        message VARCHAR(255),
        linha VARCHAR(255),
        lineNumber INT,
        erro VARCHAR(255),
        idArquivo INT,
        dataCadastro DATETIME,
        dataAtualizacao DATETIME,
        FOREIGN KEY (idArquivo) REFERENCES arquivo(id)
    );
END

-- Create the 'cnab' table
IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'cnab')
BEGIN
    CREATE TABLE cnab (
        id INT IDENTITY(1,1) PRIMARY KEY,
        identRegistro VARCHAR(255),
        agenciaDebito VARCHAR(255),
        digitoAgencia VARCHAR(255),
        razaoAgencia VARCHAR(255),
        contaCorrente VARCHAR(255),
        digitoConta VARCHAR(255),
        identBeneficiario VARCHAR(255),
        controleParticipante VARCHAR(255),
        codigoBanco VARCHAR(255),
        campoMulta INT,
        percentualMulta DECIMAL,
        nossoNumero INT,
        digitoConferenciaNumeroBanco VARCHAR(255),
        descontoDia INT,
        condicaoEmpissaoPapeladaCobranca INT,
        boletoDebitoAutomatico VARCHAR(255),
        identificacaoOcorrencia INT,
        numeroDocumento VARCHAR(255),
        dataVencimento DATE,
        valorTitulo DECIMAL,
        especieTitulo INT,
        dataEmissao DATE,
        primeiraInstrucao VARCHAR(255),
        segundaInstrucao VARCHAR(255),
        moraDia DECIMAL,
        dataLimiteDescontoConcessao DATE,
        valorDesconto DECIMAL,
        valorIOF DECIMAL,
        valorAbatimento VARCHAR(255),
        tipoPagador INT,
        nomePagador VARCHAR(255),
        endereco VARCHAR(255),
        primeiraMensagem VARCHAR(255),
        cep VARCHAR(255),
        sufixoCEP VARCHAR(255),
        segundaMensagem VARCHAR(255),
        sequencialRegistro VARCHAR(255),
        idArquivo INT,
        linha INT,
        situacao VARCHAR(100),
        dataCadastro DATETIME,
        dataAtualizacao DATETIME,
        FOREIGN KEY (idArquivo) REFERENCES arquivo(id)
    );
END

IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHER TABLE_NAME = 'boleto_alteracao')
BEGIN
    CREATE TABLE boleto_alteracao(
       id INT IDENTITY(1,1) PRIMARY KEY,
       camposALterados VARCHAR(255),
       camposAntigos VARCHAR(255),
       tipoDeAlteracao VARCHAR(255),
       boletoAlterado INT,
       idArquivo INT,
       dataCadastro DATETIME,
       dataAtualizacao DATETIME,
       FOREIGN KEY (boletoAlterado) REFERENCES cnab(id),
       FOREIGN KEY (idArquivo) REFERENCES arquivo(id)
    );
END