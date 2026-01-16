package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.exception;

import jakarta.persistence.EntityNotFoundException;

public class SessaoNaoEncontradaException extends EntityNotFoundException {
    public SessaoNaoEncontradaException(String message) {
        super(message);
    }
}
