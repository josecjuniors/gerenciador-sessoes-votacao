package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository;

import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.SessaoVotacao;
import java.util.Optional;
import java.util.UUID;

public interface SessaoVotacaoRepository {
    SessaoVotacao save(SessaoVotacao sessaoVotacao);
    Optional<SessaoVotacao> findById(UUID id);
}
