package com.josecjuniors.gerenciadorsessoesvotacao.adapters.in.sessao.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrarVotoRequest(
    @NotBlank(message = "O CPF é obrigatório")
    @Size(min = 11, max = 11, message = "O CPF deve ter exatamente 11 caracteres")
    String cpfAssociado,

    @NotNull(message = "O voto é obrigatório (true/false)")
    Boolean voto
) {
}
