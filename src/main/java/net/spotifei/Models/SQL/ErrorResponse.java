package net.spotifei.Models.SQL;

public class ErrorResponse {
    private String error;
    private Exception exception;

    public ErrorResponse(String error, Exception exception) {
        this.error = error;
        this.exception = exception;
    }
}
