package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.repository;

import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.Voto;
import java.util.UUID;

public interface VotoRepository {
    Voto save(Voto voto);
    boolean existsBySessaoVotacaoIdAndCpfAssociado(UUID sessaoId, String cpfAssociado);
}
