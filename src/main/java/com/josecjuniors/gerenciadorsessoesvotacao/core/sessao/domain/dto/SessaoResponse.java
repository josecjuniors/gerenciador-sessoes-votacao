package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record SessaoResponse(
    UUID id,
    UUID pautaId,
    String status,
    LocalDateTime dataAbertura,
    LocalDateTime dataFechamento
) implements Serializable {
}
