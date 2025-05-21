package net.spotifei.Models.Responses;

import net.spotifei.Exceptions.NotApplicableMethodException;

//Classe onde armazena as possiveis respostas de sucesso
public class SuccessResponse<T> implements Response<T> {
    private final String message;
    private final T data;

    public SuccessResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Exception getException() {
        throw new NotApplicableMethodException("Método não aplicado para respostas de sucesso.");
    }
}
