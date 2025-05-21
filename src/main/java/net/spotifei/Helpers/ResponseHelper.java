package net.spotifei.Helpers;

//imports
import net.spotifei.Infrastructure.Logger.LoggerRepository;
import net.spotifei.Models.Responses.BadResponse;
import net.spotifei.Models.Responses.ErrorResponse;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.Responses.SuccessResponse;

import javax.swing.*;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.logError;

public class ResponseHelper {

    /**
     * Gera uma resposta de erro com uma mensagem e uma exceção associada.
     * A resposta indica que uma operação falhou devido a um erro inesperado ou uma exceção.
     *
     * @param <T> O tipo de dado que a resposta deveria conter (útil para genericidade, mesmo em erros).
     * @param error A mensagem de erro descritiva.
     * @param exception A exceção que causou o erro.
     * @return Uma nova instância de ErrorResponse.
     */
    public static <T> ErrorResponse<T> generateErrorResponse(String error, Exception exception){
        return new ErrorResponse<>(error, exception);
    }

    /**
     * Gera uma resposta de erro apenas com uma mensagem, sem uma exceção específica.
     * Para erros onde não há uma exceção de código subjacente.
     *
     * @param <T> O tipo de dado que a resposta deveria conter.
     * @param error A mensagem de erro descritiva.
     * @return Uma nova instância de ErrorResponse.
     */
    public static <T> ErrorResponse<T> generateErrorResponse(String error){
        return new ErrorResponse<>(error, null);
    }

    /**
     * Gera uma resposta de erro sem mensagem ou exceção.
     *
     * @param <T> O tipo de dado que a resposta deveria conter.
     * @return Uma nova instância de ErrorResponse.
     */
    public static <T> ErrorResponse<T> generateErrorResponse(){
        return new ErrorResponse<>(null, null);
    }

    /**
     * Gera uma resposta de sucesso com uma mensagem e dados.
     * Indica que a operação foi concluída com êxito e retorna dados relevantes.
     *
     * @param <T> O tipo de dado contido na resposta de sucesso.
     * @param message Uma mensagem de sucesso.
     * @param data Os dados resultantes da operação.
     * @return Uma nova instância de SuccessResponse.
     */
    public static <T> SuccessResponse<T> generateSuccessResponse(String message, T data){
        return new SuccessResponse<>(message, data);
    }

    /**
     * Gera uma resposta de sucesso apenas com uma mensagem.
     * Útil para operações que não retornam dados.
     *
     * @param <T> O tipo de dado que a resposta poderia conter (mas será nulo).
     * @param message Uma mensagem de sucesso.
     * @return Uma nova instância de SuccessResponse.
     */
    public static <T> SuccessResponse<T> generateSuccessResponse(String message){
        return new SuccessResponse<>(message, null);
    }

    /**
     * Gera uma resposta de sucesso sem mensagem ou dados.
     *
     * @param <T> O tipo de dado que a resposta poderia conter.
     * @return Uma nova instância de SuccessResponse.
     */
    public static <T> SuccessResponse<T> generateSuccessResponse(){
        return new SuccessResponse<>(null, null);
    }

    /**
     * Gera uma resposta de falha com uma mensagem.
     * Este tipo de resposta indica que a operação não pôde ser concluída
     * Como uma entrada inválida.
     *
     * @param <T> O tipo de dado que a resposta deveria conter.
     * @param message A mensagem que descreve a falha de negócio.
     * @return Uma nova instância de BadResponse.
     */
    public static <T> BadResponse<T> generateBadResponse(String message){
        return new BadResponse<>(message);
    }

    /**
     * Centraliza o tratamento padrão de respostas que indicam um erro.
     * Verifica se a resposta fornecida não é um sucesso. Se for um erro ou uma falha,
     * registra a mensagem em log e exibe uma mensagem ao usuário via JOptionPane.
     *
     * @param response O objeto Response a ser verificado.
     * @return true se a resposta indicar um erro (ErrorResponse ou BadResponse), false caso contrário.
     */
    public static boolean handleDefaultResponseIfError(Response<?> response){
        if (!response.isSuccess()){
            if(response.isError()){
                logError(response.getMessage(), response.getException());
                JOptionPane.showMessageDialog(null,
                        "Ops, deu um erro aqui, tente novamente! " + (LoggerRepository.isDebugEnabled() ? response.getMessage() : ""));
            }else {
                logError(response.getMessage());
                JOptionPane.showMessageDialog(null, response.getMessage());
            }
            return true;
        }
        return false;
    }

}
