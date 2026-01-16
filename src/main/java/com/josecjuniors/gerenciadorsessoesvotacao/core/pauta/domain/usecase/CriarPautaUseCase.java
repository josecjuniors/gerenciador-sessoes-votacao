package com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.usecase;

import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.dto.PautaResponse;
import com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.usecase.command.CriarPautaCommand;

public interface CriarPautaUseCase {
    PautaResponse handle(CriarPautaCommand command);
}
