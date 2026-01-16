package com.josecjuniors.gerenciadorsessoesvotacao.adapters.in.pauta.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CriarPautaRequest(
    @NotBlank(message = "O título é obrigatório")
    String titulo,
    
    String descricao
) {
}
