package net.spotifei.Models.Responses;

/**
 * Lida com respostas ao banco de dados
 * @param <T> Classe que será retornada da data do SuccessResponse
 */
public interface Response<T> {
    boolean isSuccess();
    T getData();
    String getMessage();
    Exception getException();
}
