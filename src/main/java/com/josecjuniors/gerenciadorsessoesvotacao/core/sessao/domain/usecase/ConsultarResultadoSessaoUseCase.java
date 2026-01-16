package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase;

import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.dto.ResultadoSessaoResponse;
import java.util.UUID;

public interface ConsultarResultadoSessaoUseCase {
    ResultadoSessaoResponse execute(UUID sessaoId);
}
