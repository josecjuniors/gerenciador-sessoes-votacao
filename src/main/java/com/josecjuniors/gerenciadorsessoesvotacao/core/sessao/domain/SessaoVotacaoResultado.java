package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "sessao_votacao_resultado")
public class SessaoVotacaoResultado {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "sessao_votacao_id", nullable = false, unique = true)
    private SessaoVotacao sessaoVotacao;

    @Enumerated(EnumType.STRING)
    private ResultadoVotacao resultado;

    private Integer quantidadeSim;

    private Integer quantidadeNao;

    private Integer quantidadeVotos;

    public SessaoVotacaoResultado() {
    }

    public SessaoVotacaoResultado(SessaoVotacao sessaoVotacao, Integer quantidadeSim, Integer quantidadeNao) {
        this.sessaoVotacao = sessaoVotacao;
        this.quantidadeSim = quantidadeSim;
        this.quantidadeNao = quantidadeNao;
        this.quantidadeVotos = quantidadeSim + quantidadeNao;
        this.resultado = calcularResultado();
    }

    private ResultadoVotacao calcularResultado() {
        if (quantidadeSim > quantidadeNao) {
            return ResultadoVotacao.APROVADA;
        } else if (quantidadeNao > quantidadeSim) {
            return ResultadoVotacao.REPROVADA;
        } else {
            return ResultadoVotacao.EMPATE;
        }
    }

    public UUID getId() {
        return id;
    }

    public SessaoVotacao getSessaoVotacao() {
        return sessaoVotacao;
    }

    public ResultadoVotacao getResultado() {
        return resultado;
    }

    public Integer getQuantidadeSim() {
        return quantidadeSim;
    }

    public Integer getQuantidadeNao() {
        return quantidadeNao;
    }

    public Integer getQuantidadeVotos() {
        return quantidadeVotos;
    }
}
