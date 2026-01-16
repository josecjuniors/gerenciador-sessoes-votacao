package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.command;

import java.util.UUID;

public record FecharSessaoCommand(UUID sessaoId) {
}
