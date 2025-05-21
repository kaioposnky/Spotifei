package net.spotifei.Models.Responses;

import net.spotifei.Exceptions.NotApplicableMethodException;

//Classe onde armazena as possiveis respostas de falha
public class BadResponse<T> implements Response<T> {
    private final String message;

    public BadResponse(String message) {
        this.message = message;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public T getData() {
        throw new NotApplicableMethodException("Método não aplicado para respostas sem sucesso.");
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Exception getException() {
        throw new NotApplicableMethodException("Método não aplicado para respostas sem excessões.");
    }
}
