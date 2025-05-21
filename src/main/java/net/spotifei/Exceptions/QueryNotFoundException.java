package net.spotifei.Exceptions;

public class QueryNotFoundException extends RuntimeException {

    /**
     * Exceção lançada quando uma consulta (query) no banco de dados ou em outra fonte de dados
     * não retorna nenhum resultado.
     *
     * @param message Uma mensagem detalhando a consulta que não encontrou resultados.
     */
    public QueryNotFoundException(String message) {
        super(message);
    }
}
