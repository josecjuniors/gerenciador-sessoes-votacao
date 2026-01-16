package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.application;

import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.SessaoVotacaoResultado;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.dto.ResultadoSessaoResponse;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.exception.ResultadoNaoEncontradoException;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository.SessaoVotacaoResultadoRepository;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.ConsultarResultadoSessaoUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ConsultarResultadoSessaoService implements ConsultarResultadoSessaoUseCase {

    private final SessaoVotacaoResultadoRepository sessaoVotacaoResultadoRepository;

    public ConsultarResultadoSessaoService(SessaoVotacaoResultadoRepository sessaoVotacaoResultadoRepository) {
        this.sessaoVotacaoResultadoRepository = sessaoVotacaoResultadoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ResultadoSessaoResponse execute(UUID sessaoId) {
        SessaoVotacaoResultado resultado = sessaoVotacaoResultadoRepository.findBySessaoVotacaoId(sessaoId)
                .orElseThrow(() -> new ResultadoNaoEncontradoException("Resultado não encontrado para a sessão: " + sessaoId + ". A sessão pode ainda estar aberta ou não existir."));

        return new ResultadoSessaoResponse(
                resultado.getSessaoVotacao().getId(),
                resultado.getResultado(),
                resultado.getQuantidadeSim(),
                resultado.getQuantidadeNao(),
                resultado.getQuantidadeVotos()
        );
    }
}
