CREATE TABLE sessao_votacao (
    id UUID PRIMARY KEY,
    pauta_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    data_hora_abertura TIMESTAMP NOT NULL,
    data_hora_fechamento TIMESTAMP NOT NULL,
    CONSTRAINT fk_sessao_pauta FOREIGN KEY (pauta_id) REFERENCES pauta(id)
);
