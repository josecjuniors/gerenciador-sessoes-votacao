package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.application;

import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.Pauta;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.exception.PautaNaoEncontradaException;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.repository.PautaRepository;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.SessaoVotacao;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.dto.SessaoResponse;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.port.AgendarFechamentoSessaoPort;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository.SessaoVotacaoRepository;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.AbrirSessaoUseCase;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.command.AbrirSessaoCommand;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
public class AbrirSessaoService implements AbrirSessaoUseCase {

    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final PautaRepository pautaRepository;
    private final AgendarFechamentoSessaoPort agendarFechamentoSessaoPort;

    public AbrirSessaoService(SessaoVotacaoRepository sessaoVotacaoRepository,
                              PautaRepository pautaRepository,
                              AgendarFechamentoSessaoPort agendarFechamentoSessaoPort) {
        this.sessaoVotacaoRepository = sessaoVotacaoRepository;
        this.pautaRepository = pautaRepository;
        this.agendarFechamentoSessaoPort = agendarFechamentoSessaoPort;
    }

    @Override
    @Transactional
    @CachePut(value = "sessoes", key = "#result.id")
    public SessaoResponse handle(AbrirSessaoCommand command) {
        Pauta pauta = pautaRepository.findById(command.pautaId())
                .orElseThrow(() -> new PautaNaoEncontradaException("Pauta não encontrada com ID: " + command.pautaId()));

        SessaoVotacao sessao = new SessaoVotacao(pauta, command.tempoEmMinutos());
        SessaoVotacao sessaoSalva = sessaoVotacaoRepository.save(sessao);

        // Calcula a duração real para agendar o fechamento
        Duration duracao = Duration.between(sessaoSalva.getDataHoraAbertura(), sessaoSalva.getDataHoraFechamento());
        agendarFechamentoSessaoPort.agendar(sessaoSalva.getId(), duracao);

        return new SessaoResponse(
                sessaoSalva.getId(),
                sessaoSalva.getPauta().getId(),
                sessaoSalva.getStatus().name(),
                sessaoSalva.getDataHoraAbertura(),
                sessaoSalva.getDataHoraFechamento()
        );
    }
}
