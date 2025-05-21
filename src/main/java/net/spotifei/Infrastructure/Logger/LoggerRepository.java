package net.spotifei.Infrastructure.Logger;

import org.slf4j.LoggerFactory;

public class LoggerRepository {

    private static final org.slf4j.Logger slf4jLogger = LoggerFactory.getLogger("Spotifei");
    private static boolean debugEnabled = false;

    /**
     * Registra uma mensagem no nível INFO.
     *
     * @param message A mensagem a ser logada.
     */
    public static void logInfo(String message){
        slf4jLogger.info(message);
    }

    /**
     * Registra uma mensagem no nível ERROR com um objeto Throwable (exceção).
     *
     * @param message A mensagem de erro.
     * @param throwable A exceção associada ao erro.
     */
    public static void logError(String message, Throwable throwable){
        slf4jLogger.error(message, throwable);
    }

    /**
     * Registra uma mensagem no nível ERROR sem um objeto Throwable.
     *
     * @param message A mensagem de erro.
     */
    public static void logError(String message){
        slf4jLogger.error(message);
    }

    /**
     * Registra uma mensagem no nível WARN.
     *
     * @param message A mensagem de aviso.
     */
    public static void logWarn(String message){
        slf4jLogger.warn(message);
    }

    /**
     * Registra uma mensagem no nível DEBUG.
     *
     * @param message A mensagem de debug.
     */
    public static void logDebug(String message){
        if (debugEnabled)
            slf4jLogger.debug(message);
    }

    /**
     * Define o status da funcionalidade de logging de debug.
     * @param debugStatus `true` para habilitar o logging de debug, `false` para desabilitar.
     */
    public static void setDebugEnabled(boolean debugStatus){
        debugEnabled = debugStatus;
    }

    /**
     * Retorna o status atual da funcionalidade de logging de debug.
     * @return `true` se o debug estiver habilitado, `false` caso contrário.
     */
    public static boolean isDebugEnabled() {
        return debugEnabled;
    }
}
