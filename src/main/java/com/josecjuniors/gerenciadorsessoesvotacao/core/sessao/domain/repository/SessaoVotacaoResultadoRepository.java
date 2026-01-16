package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository;

import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.SessaoVotacaoResultado;
import java.util.Optional;
import java.util.UUID;

public interface SessaoVotacaoResultadoRepository {
    SessaoVotacaoResultado save(SessaoVotacaoResultado resultado);
    Optional<SessaoVotacaoResultado> findBySessaoVotacaoId(UUID sessaoId);
}
