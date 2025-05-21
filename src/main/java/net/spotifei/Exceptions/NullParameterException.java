package net.spotifei.Exceptions;

public class NullParameterException extends RuntimeException {

    /**
     * Exceção lançada quando um parâmetro de método ou um argumento de construtor
     * é nulo, mas um valor não nulo é esperado ou exigido para a operação.
     *
     * @param message Uma mensagem detalhando qual parâmetro está nulo.
     */
    public NullParameterException(String message) {
        super(message);
    }
}
