package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.command;

import java.util.UUID;

public record RegistrarVotoCommand(UUID sessaoId, String cpfAssociado, Boolean voto) {
}
