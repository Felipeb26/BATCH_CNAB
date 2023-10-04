CREATE TABLE IF NOT EXISTS transacao(
    id SERIAL primary key,
    tipo int,
    data date,
    valor decimal,
    cpf bigint,
    cartao varchar(255),
    hora time,
    donoLoja varchar(255),
    nomeLoja varchar(255)
);