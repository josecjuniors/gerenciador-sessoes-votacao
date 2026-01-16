package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase;

import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.SessaoVotacao;

public interface RegistrarResultadoSessaoUseCase {
    void execute(SessaoVotacao sessaoVotacao);
}
