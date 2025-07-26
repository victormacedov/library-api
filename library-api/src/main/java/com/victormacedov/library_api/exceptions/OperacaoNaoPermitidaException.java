package com.victormacedov.library_api.exceptions;

public class OperacaoNaoPermitidaException extends RuntimeException {

    public OperacaoNaoPermitidaException(String message) {
        super(message);
    }
}
