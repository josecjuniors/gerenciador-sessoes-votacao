package com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.dto;

import java.util.UUID;

public record PautaResponse(UUID id, String titulo, String descricao) {
}
