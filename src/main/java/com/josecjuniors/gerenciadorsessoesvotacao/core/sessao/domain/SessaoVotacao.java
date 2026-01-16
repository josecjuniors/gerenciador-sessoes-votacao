package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain;

import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.Pauta;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessao_votacao")
public class SessaoVotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;

    @Enumerated(EnumType.STRING)
    private StatusSessao status;

    private LocalDateTime dataHoraAbertura;

    private LocalDateTime dataHoraFechamento;

    public SessaoVotacao() {
    }

    public SessaoVotacao(Pauta pauta, Integer tempoAberturaEmMinutos) {
        this.pauta = pauta;
        this.status = StatusSessao.ABERTA;
        this.dataHoraAbertura = LocalDateTime.now();
        this.dataHoraFechamento = this.dataHoraAbertura.plusMinutes(
            tempoAberturaEmMinutos != null && tempoAberturaEmMinutos > 0 ? tempoAberturaEmMinutos : 1
        );
    }

    public UUID getId() {
        return id;
    }

    public Pauta getPauta() {
        return pauta;
    }

    public StatusSessao getStatus() {
        return status;
    }

    public LocalDateTime getDataHoraAbertura() {
        return dataHoraAbertura;
    }

    public LocalDateTime getDataHoraFechamento() {
        return dataHoraFechamento;
    }

    public void fecharSessao() {
        this.status = StatusSessao.FECHADA;
    }
}
