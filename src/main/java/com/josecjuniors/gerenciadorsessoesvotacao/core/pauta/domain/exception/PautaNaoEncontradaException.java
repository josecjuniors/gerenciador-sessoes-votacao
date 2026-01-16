package com.josecjuniors.gerenciadorsessoesvotacao.core.pauta.domain.exception;

import jakarta.persistence.EntityNotFoundException;

public class PautaNaoEncontradaException extends EntityNotFoundException {
    public PautaNaoEncontradaException(String message) {
        super(message);
    }
}
