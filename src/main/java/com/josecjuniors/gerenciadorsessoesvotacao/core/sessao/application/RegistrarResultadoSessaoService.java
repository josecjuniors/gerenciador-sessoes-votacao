package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.application;

import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.SessaoVotacao;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.SessaoVotacaoResultado;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository.SessaoVotacaoResultadoRepository;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository.VotoRepository;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.RegistrarResultadoSessaoUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrarResultadoSessaoService implements RegistrarResultadoSessaoUseCase {

    private static final Logger logger = LoggerFactory.getLogger(RegistrarResultadoSessaoService.class);
    private final VotoRepository votoRepository;
    private final SessaoVotacaoResultadoRepository sessaoVotacaoResultadoRepository;

    public RegistrarResultadoSessaoService(VotoRepository votoRepository,
                                           SessaoVotacaoResultadoRepository sessaoVotacaoResultadoRepository) {
        this.votoRepository = votoRepository;
        this.sessaoVotacaoResultadoRepository = sessaoVotacaoResultadoRepository;
    }

    @Override
    @Transactional
    public void execute(SessaoVotacao sessaoVotacao) {
        logger.info("Iniciando apuração de votos para a sessão: {}", sessaoVotacao.getId());

        long votosSim = votoRepository.countBySessaoVotacaoIdAndVoto(sessaoVotacao.getId(), true);
        long votosNao = votoRepository.countBySessaoVotacaoIdAndVoto(sessaoVotacao.getId(), false);

        SessaoVotacaoResultado resultado = new SessaoVotacaoResultado(
                sessaoVotacao,
                (int) votosSim,
                (int) votosNao
        );

        sessaoVotacaoResultadoRepository.save(resultado);
        
        logger.info("Resultado registrado para a sessão {}: {} (Sim: {}, Não: {})", 
                sessaoVotacao.getId(), resultado.getResultado(), votosSim, votosNao);
    }
}
