package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase;

import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.dto.SessaoResponse;
import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.command.AbrirSessaoCommand;

public interface AbrirSessaoUseCase {
    SessaoResponse handle(AbrirSessaoCommand command);
}
