package net.spotifei.Helpers;

import net.spotifei.Models.Responses.BadResponse;
import net.spotifei.Models.Responses.ErrorResponse;
import net.spotifei.Models.Responses.SuccessResponse;

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
}
