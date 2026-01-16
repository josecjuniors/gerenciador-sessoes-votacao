package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase;

import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.command.RegistrarVotoCommand;

public interface RegistrarVotoUseCase {
    void handle(RegistrarVotoCommand command);
}
