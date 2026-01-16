package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "voto", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"sessao_votacao_id", "cpf_associado"})
})
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sessao_votacao_id", nullable = false)
    private SessaoVotacao sessaoVotacao;

    @Column(name = "cpf_associado", length = 11, nullable = false)
    private String cpfAssociado;

    @Column(name = "data_hora_votacao", nullable = false)
    private LocalDateTime dataHoraVotacao;

    @Column(nullable = false)
    private Boolean voto; // TRUE = SIM, FALSE = NAO

    public Voto() {
    }

    public Voto(SessaoVotacao sessaoVotacao, String cpfAssociado, Boolean voto) {
        this.sessaoVotacao = sessaoVotacao;
        this.cpfAssociado = cpfAssociado;
        this.voto = voto;
        this.dataHoraVotacao = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public SessaoVotacao getSessaoVotacao() {
        return sessaoVotacao;
    }

    public String getCpfAssociado() {
        return cpfAssociado;
    }

    public LocalDateTime getDataHoraVotacao() {
        return dataHoraVotacao;
    }

    public Boolean getVoto() {
        return voto;
    }
}
