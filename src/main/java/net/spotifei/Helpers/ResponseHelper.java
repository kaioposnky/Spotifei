package net.spotifei.Helpers;

import net.spotifei.Infrastructure.Logger.LoggerRepository;
import net.spotifei.Models.Responses.BadResponse;
import net.spotifei.Models.Responses.ErrorResponse;
import net.spotifei.Models.Responses.Response;
import net.spotifei.Models.Responses.SuccessResponse;

import javax.swing.*;

import java.util.List;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.logError;

public class ResponseHelper {

    public static <T> ErrorResponse<T> GenerateErrorResponse(String error, Exception exception){
        return new ErrorResponse<>(error, exception);
    }

    public static <T> ErrorResponse<T>  GenerateErrorResponse(String error){
        return new ErrorResponse<>(error, null);
    }

    public static <T> ErrorResponse<T>  GenerateErrorResponse(){
        return new ErrorResponse<>(null, null);
    }

    public static <T> SuccessResponse<T>  GenerateSuccessResponse(String message, T data){
        return new SuccessResponse<>(message, data);
    }

    public static <T> SuccessResponse<T>  GenerateSuccessResponse(String message){
        return new SuccessResponse<>(message, null);
    }

    public static <T> SuccessResponse<T>  GenerateSuccessResponse(){
        return new SuccessResponse<>(null, null);
    }

    public static <T> BadResponse<T> GenerateBadResponse(String message){
        return new BadResponse<>(message);
    }

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
