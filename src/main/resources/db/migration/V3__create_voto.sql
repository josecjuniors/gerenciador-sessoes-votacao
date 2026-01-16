CREATE TABLE voto (
    id UUID PRIMARY KEY,
    sessao_votacao_id UUID NOT NULL,
    cpf_associado VARCHAR(11) NOT NULL,
    data_hora_votacao TIMESTAMP NOT NULL,
    voto BOOLEAN NOT NULL,
    CONSTRAINT fk_voto_sessao FOREIGN KEY (sessao_votacao_id) REFERENCES sessao_votacao(id),
    CONSTRAINT uk_voto_sessao_cpf UNIQUE (sessao_votacao_id, cpf_associado)
);
