package com.josecjuniors.gerenciadorsessoesvotacao.adapters.in.sessao.web.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AbrirSessaoRequest(
    @NotNull(message = "O ID da pauta é obrigatório")
    UUID pautaId,
    
    Integer tempoEmMinutos
) {
}
