CREATE TABLE sessao_votacao_resultado (
    id UUID PRIMARY KEY,
    sessao_votacao_id UUID NOT NULL UNIQUE,
    resultado VARCHAR(20) NOT NULL,
    quantidade_sim INTEGER NOT NULL,
    quantidade_nao INTEGER NOT NULL,
    quantidade_votos INTEGER NOT NULL,
    CONSTRAINT fk_resultado_sessao FOREIGN KEY (sessao_votacao_id) REFERENCES sessao_votacao(id)
);
