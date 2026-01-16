package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase;

import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.command.FecharSessaoCommand;

public interface FecharSessaoUseCase {
    void handle(FecharSessaoCommand command);
}
