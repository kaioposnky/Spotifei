package net.spotifei.Helpers.SQL;

import com.google.gson.Gson;
import net.spotifei.Models.SQL.ErrorResponse;
import net.spotifei.Models.SQL.Response;
import net.spotifei.Models.SQL.SuccessResponse;

import java.util.HashMap;
import java.util.Map;

public class SQLResponseHelper {

    public static Response GenerateErrorResponse(String error, Exception exception){
        Response errorResponse = new Response();
        errorResponse.setErrorResponse(new ErrorResponse(error, exception));
        return errorResponse;
    }

    public static Response GenerateSuccessResponse(String message, Gson data){
        Response successResponse = new Response();
        successResponse.setSuccessResponse(new SuccessResponse(message, data));
        return successResponse;
    }
}
