package net.spotifei.Infrastructure.Logger;

import org.slf4j.LoggerFactory;

public class LoggerRepository {

    private static final org.slf4j.Logger slf4jLogger = LoggerFactory.getLogger("Spotifei");
    private static boolean debugEnabled = false;

    public static void logInfo(String message){
        slf4jLogger.info(message);
    }

    public static void logError(String message, Throwable throwable){
        slf4jLogger.error(message, throwable);
    }

    public static void logError(String message){
        slf4jLogger.error(message);
    }

    public static void logWarn(String message){
        slf4jLogger.warn(message);
    }

    public static void logDebug(String message){
        if (debugEnabled)
            slf4jLogger.debug(message);
    }

    public static void setDebugEnabled(boolean debugStatus){
        debugEnabled = debugStatus;
    }

}
