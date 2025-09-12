package com.victormacedov.library_api.controller.common;

import com.victormacedov.library_api.controller.dto.ErroCampo;
import com.victormacedov.library_api.controller.dto.ErroResposta;
import com.victormacedov.library_api.exceptions.CampoInvalidoException;
import com.victormacedov.library_api.exceptions.OperacaoNaoPermitidaException;
import com.victormacedov.library_api.exceptions.RegistroDuplicadoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("Erro de validação: {}", e.getMessage());

        List<FieldError> fieldErrors = e.getFieldErrors();
        List<ErroCampo> listaErros = fieldErrors
                .stream()
                .map(fe -> new ErroCampo(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ErroResposta(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação",
                listaErros);
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResposta handleRegistroDuplicadoException(RegistroDuplicadoException e) {
        log.warn("Registro duplicado: {}", e.getMessage());

        return ErroResposta.conflito(e.getMessage());
    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta handleOperacaoNaoPermitidaException(OperacaoNaoPermitidaException e) {
        log.warn("Operação não permitida: {}", e.getMessage());

        return ErroResposta.respostaPadrao(e.getMessage());
    }

    @ExceptionHandler(CampoInvalidoException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta handleCampoInvalidoException(CampoInvalidoException e) {
        log.error("Campo inválido: {} - {}", e.getCampo(), e.getMessage());

        return new ErroResposta(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação.",
                List.of(new ErroCampo(e.getCampo(), e.getMessage())));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErroResposta handleAccessDeniedException(AccessDeniedException e) {
        log.warn("Acesso negado: {}", e.getMessage());

        return new ErroResposta(
                HttpStatus.FORBIDDEN.value(),
                "Acesso negado. Você não possui permissão para acessar este recurso.",
                List.of());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResposta handleErrosNaoTratados(RuntimeException e) {
        log.error("Erro inesperado: ", e);

        return new ErroResposta(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro Inesperado. Entre em contato com a administração do sistema.",
                List.of());
    }
}
