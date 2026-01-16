package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.dto;

import com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.ResultadoVotacao;
import java.util.UUID;

public record ResultadoSessaoResponse(
    UUID sessaoId,
    ResultadoVotacao resultado,
    Integer quantidadeSim,
    Integer quantidadeNao,
    Integer quantidadeVotos
) {
}
