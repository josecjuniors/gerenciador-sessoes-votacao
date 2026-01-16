package com.josecjuniors.gerenciadorsessoesvotacao.core.sessao.domain.exception;

import jakarta.persistence.EntityNotFoundException;

public class ResultadoNaoEncontradoException extends EntityNotFoundException {
    public ResultadoNaoEncontradoException(String message) {
        super(message);
    }
}
