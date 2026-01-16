package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.usecase.command;

import java.util.UUID;

public record AbrirSessaoCommand(UUID pautaId, Integer tempoEmMinutos) {
}
