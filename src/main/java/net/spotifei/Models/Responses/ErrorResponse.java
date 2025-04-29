package net.spotifei.Models.Responses;

import net.spotifei.Exceptions.NotApplicableMethodException;

public class ErrorResponse<T> implements Response<T> {
    private final String message;
    private final Exception exception;

    public ErrorResponse(String message, Exception exception) {
        this.message = message;
        this.exception = exception;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    @Override
    public T getData() {
        throw new NotApplicableMethodException("Método não aplicado para respostas de error.");
    }
}
