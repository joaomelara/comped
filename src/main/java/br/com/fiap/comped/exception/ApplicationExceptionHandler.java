package br.com.fiap.comped.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException error) {

        Map<String, String> errorMap = new HashMap<>();
        List<FieldError> campos = error.getBindingResult().getFieldErrors();

        if (!campos.isEmpty()) {
            // Sempre usa "message" como chave → consistente para o BDD
            errorMap.put("message", campos.get(0).getDefaultMessage());
        }

        return errorMap;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Map<String, String> handleIntegrityViolation(){

        Map<String, String> errorMap = new HashMap<>();

        errorMap.put("erro", "Usuário já cadastrado!");

        return errorMap;

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public Map<String, String> handleUsuarioNaoEncontrado(UsuarioNaoEncontradoException error) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", error.getMessage());
        return errorMap;
    }

}