package net.spotifei.Helpers.SQL;

import com.google.gson.Gson;
import net.spotifei.Models.SQL.ErrorResponse;

import java.util.HashMap;
import java.util.Map;

public class SQLResponseHelper {

    public static Map<String, Object> GenerateErrorResponse(String error, Exception exception){
        ErrorResponse errorResponse = new ErrorResponse(error, exception);
        return new HashMap<>();
    }

    public static Gson GenerateSuccessResponse(String message, Gson data){
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", error);
        errorResponse.put("exception", exception);

        return new Gson().toJson(errorResponse);
    }
}
