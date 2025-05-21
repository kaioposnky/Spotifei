package net.spotifei.Exceptions;

public class NotApplicableMethodException extends RuntimeException {
    /**
     * Exceção lançada quando um método é invocado.
     *
     * @param message Uma mensagem detalhando o motivo pelo qual o método não é aplicável.
     */
    public NotApplicableMethodException(String message) {
        super(message);
    }
}
