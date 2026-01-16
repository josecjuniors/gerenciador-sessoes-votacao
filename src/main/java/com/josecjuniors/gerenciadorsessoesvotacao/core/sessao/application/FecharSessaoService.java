package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.application;

import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.SessaoVotacao;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.StatusSessao;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository.SessaoVotacaoRepository;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.FecharSessaoUseCase;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.command.FecharSessaoCommand;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FecharSessaoService implements FecharSessaoUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FecharSessaoService.class);
    private final SessaoVotacaoRepository sessaoVotacaoRepository;

    public FecharSessaoService(SessaoVotacaoRepository sessaoVotacaoRepository) {
        this.sessaoVotacaoRepository = sessaoVotacaoRepository;
    }

    @Override
    @Transactional
    @CacheEvict(value = "sessoes", key = "#command.sessaoId")
    public void handle(FecharSessaoCommand command) {
        logger.info("Iniciando fechamento da sessão: {}", command.sessaoId());

        SessaoVotacao sessao = sessaoVotacaoRepository.findById(command.sessaoId())
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada: " + command.sessaoId()));

        if (sessao.getStatus() == StatusSessao.FECHADA) {
            logger.warn("Sessão {} já está fechada.", command.sessaoId());
            return;
        }

        sessao.fecharSessao();
        sessaoVotacaoRepository.save(sessao);
        
        logger.info("Sessão {} fechada com sucesso.", command.sessaoId());
        
        // TODO: Aqui poderíamos disparar um evento de domínio "SessaoEncerrada" 
        // para iniciar a contagem de votos ou notificar interessados.
    }
}
