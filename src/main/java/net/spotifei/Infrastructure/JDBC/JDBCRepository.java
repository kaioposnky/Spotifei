package net.spotifei.Infrastructure.JDBC;

//imports
import net.spotifei.Exceptions.NullParameterException;
import net.spotifei.Exceptions.QueryNotFoundException;
import net.spotifei.Models.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.spotifei.Helpers.AssetsLoader.getQueriesFiles;
import static net.spotifei.Helpers.ParametersHelper.getParametersFromObject;
import static net.spotifei.Infrastructure.Logger.LoggerRepository.logDebug;
import static net.spotifei.Spotifei.getDotEnv;


public class JDBCRepository {
    private final ConcurrentMap<String, String> queriesCache = new ConcurrentHashMap<>();
    private static final Pattern PARAM_PATTERN = Pattern.compile("@(\\w+)");
    // Sobre o regex: o @ significa pegar caractere @ os () significa todos os que estão depois do @
    // e o \\w+ um ou mais quaisquer caracteres de a-z A-Z + underscore
    private final String url;
    private final String user;
    private final String password;
    private Connection connection;

    /**
     * Construtor da classe.
     * Inicializa as configurações de conexão com o banco de dados.
     * Carrega todas as queries SQL de arquivos XML para o cache.
     */
    public JDBCRepository() {
        this.url = getDotEnv().get("DATABASE_URL");
        this.user = getDotEnv().get("DATABASE_USER");
        this.password = getDotEnv().get("DATABASE_PASSWORD");

        try{
            loadQueriesCache();
            logDebug("Queries do JDBC carregadas com sucesso no Cache!");
        } catch (FileNotFoundException e){
            throw new RuntimeException("Erro ao carregar os arquivos de query!", e);
        }
    }

    /**
     * Obtém uma conexão com o banco de dados.
     *
     * @return Uma instância de `Connection` ativa.
     * @throws SQLException Se ocorrer um erro ao abrir ou verificar a conexão.
     */
    private Connection getConnection() throws SQLException {
        if(!isConnectionValid()){
            openConnection();
        }
        return connection;
    }

    /**
     * Checa se a conexão é válida
     * @return true se sim false se não
     */
    private boolean isConnectionValid() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Abre uma nova conexão com o banco de dados.
     *
     * @throws SQLException Se a conexão não puder ser estabelecida.
     */
    private void openConnection() throws SQLException {
        connection = DriverManager.getConnection(
                url, user, password
        );
    }

    /**
     * Fecha a conexão com o banco de dados.
     *
     * @throws SQLException Se a conexão não puder ser fechada.
     */
    private void closeConnection() throws SQLException {
        connection.close();
    }

    /**
     * Carrega as queries SQL de arquivos XML localizados no diretório.
     *
     * @throws FileNotFoundException Se os arquivos XML de query não forem encontrados.
     * @throws RuntimeException Se houver um erro de parsing nos arquivos XML.
     */
    private void loadQueriesCache() throws FileNotFoundException {
        try{
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            File[] xmlFiles = getQueriesFiles();

            for (File xmlFile : xmlFiles){
                Document doc = builder.parse(xmlFile);
                NodeList queries = doc.getElementsByTagName("query");
                for (int i = 0; i < queries.getLength(); i++){
                    String queryName = queries.item(i).getAttributes().getNamedItem("name").getTextContent();
                    String sql = queries.item(i).getTextContent().trim();
                    queriesCache.putIfAbsent(queryName, sql);
                }
            }
        }  catch (NullPointerException e){
            throw new FileNotFoundException("Arquivos de query não encontrados!");
        }
        catch (IOException | IllegalArgumentException | SAXException | ParserConfigurationException e) {
            throw new RuntimeException("Erro ao dar parse nos arquivos XML!", e);
        }
    }

    /**
     * @param sql Query a ser executada
     * @param params Parâmetros que devem ser inseridos na Query, no formato de um HashMap
     * @throws SQLException Gerada se tiver erro de conexão na DB
     */
    public <T> T queryProcedure(String sql, Map<String, Object> params, ResultSetHandler<T> handler) throws SQLException {
        return handler.handle(getPreparedStatement(sql, params).executeQuery());
    }

    /**
     * @param sql Query a ser executada e obtida
     * @param param Parâmetro único em forma de String que será usado na query
     * @return Retorna uma lista das informações obtidas pelo banco
     * @throws SQLException Gerada se tiver erro de conexão na DB
     */
    public <T> T queryProcedure(String sql, String param, ResultSetHandler<T> handler) throws SQLException {
        return handler.handle(getPreparedStatement(sql, param).executeQuery());
    }

    /**
     * @param sql Query a ser executada e obtida
     * @return Retorna uma lista das informações obtidas pelo banco
     * @throws SQLException Gerada se tiver erro de conexão na DB
     */
    public <T> T queryProcedure(String sql, ResultSetHandler<T> handler) throws SQLException {
        return handler.handle(getPreparedStatement(sql).executeQuery());
    }

    /**
     * @param sql Query a ser executada
     * @param userParams Parâmetros do usuário que será modificado
     * @throws SQLException Gerada se tiver erro de conexão na DB
     */
    public void executeProcedure(String sql, User userParams) throws SQLException {
        getPreparedStatement(sql, userParams).execute();
    }

    /**
     * @param sql Query a ser executada
     * @param param Parâmetro único em forma de String que será usado na query
     * @throws SQLException Gerada se tiver erro de conexão na DB
     */
    public void executeProcedure(String sql, String param) throws SQLException {
        getPreparedStatement(sql, param).execute();
    }

    /**
     * @param sql Query a ser executada
     * @param params Parâmetros que devem ser inseridos na Query, no formato de um HashMap
     * @throws SQLException Gerada se tiver erro de conexão na DB
     */
    public void executeProcedure(String sql, Map<String, Object> params) throws SQLException {
        getPreparedStatement(sql, params).execute();
    }

    /**
     * @param sql Query a ser executada
     * @throws SQLException Gerada se tiver erro de conexão na DB
     */
    public void executeProcedure(String sql) throws SQLException {
        getPreparedStatement(sql).execute();
    }

    /**
     * @implNote Use para gerar um PreparedStatement num comando que NÃO PRECISE de parâmetros
     * @param sqlRaw Código SQL obtido pelo getQueryNamed
     * @return Retorna o PreparedStatement do código SQL
     * @throws SQLException Gerada se tiver erro de conexão na DB
     */
    private PreparedStatement getPreparedStatement(String sqlRaw) throws SQLException {
        try{

            return getConnection().prepareStatement(sqlRaw);
        } catch (SQLException ex){
            openConnection();
            return getConnection().prepareStatement(sqlRaw);
        }
    }


    /**
     * @param sqlRaw Código SQL obtido pelo getQueryNamed
     * @param parameter Parâmetro único que será utilizado no SQL
     * @return Retorna o PreparedStatement do código SQL
     * @throws SQLException Gerada se tiver erro de conexão na DB
     */
    private PreparedStatement getPreparedStatement(String sqlRaw, String parameter) throws SQLException {
        // guarda os parâmetros da query
        Matcher matcher = PARAM_PATTERN.matcher(sqlRaw); // regex para obter @XXXXX do código sql

        if (matcher.results().count() > 1){
            throw new IllegalArgumentException("A query selecionada deve possuir apenas 1 campo de parâmetro!");
        }

        // dá replace no @XXXX para o parâmetro inserido
        sqlRaw = PARAM_PATTERN.matcher(sqlRaw).replaceAll("?");
        try {
            PreparedStatement statement = getConnection().prepareStatement(sqlRaw);
            statement.setString(1, parameter);
            return statement;
        } catch (SQLException ex){
            openConnection();
            PreparedStatement statement = getConnection().prepareStatement(sqlRaw);
            statement.setString(1, parameter);
            return statement;
        }
    }

    /**
     * @deprecated Não passe mais uma classe, crie um Map<\String, Object> com as informações da query
     * foi determinada Deprecated por possuir uma fragilidade enorme quanto ao nome das variáveis, use
     * somente caso você saiba o que você está fazendo.
     * @implNote Use para gerar um PreparedStatement num comando que PRECISE de parâmetros
     * @param sqlRaw Código SQL com os parâmetros no formato @Parametro
     * @param classParam Classe para ser usada de parâmetro para preencher as variáveis no SQL
     * @return Retorna um PreparedStatement com os parâmetros já incluídos no código sql
     * @throws SQLException Gerada se tiver erro de conexão na DB
     * @throws NullParameterException Gerada se o parâmetro for nulo
     * @throws IllegalArgumentException Gerada se um dos atributos do parâmetro for nulo
     */
    @Deprecated
    private PreparedStatement getPreparedStatement(String sqlRaw,
                                                   Object classParam)
            throws SQLException, NullParameterException, IllegalArgumentException {

        if (classParam == null || sqlRaw == null) {
            throw new NullParameterException("Nenhum dos parâmetros enviados podem ser nulos!");
        }

        Map<String, Object> paramValues = getParametersFromObject(classParam);
        try{
            return prepareStatementFromMap(sqlRaw, paramValues);
        } catch (SQLException ex){
            openConnection();
            return prepareStatementFromMap(sqlRaw, paramValues);
        }
    }

    /**
     * @implNote Use para gerar um PreparedStatement num comando que PRECISE de parâmetros
     * @param sqlRaw Código SQL com os parâmetros no formato @Parametro
     * @param paramsMap Mapa de String, Object, para adicionar parâmetros do SQL
     * @return Retorna um PreparedStatement com os parâmetros já incluídos no código sql
     * @throws SQLException Gerada se tiver erro de conexão na DB
     * @throws NullParameterException Gerada se o parâmetro for nulo
     * @throws IllegalArgumentException Gerada se um dos atributos do parâmetro for nulo
     */
    private PreparedStatement getPreparedStatement(String sqlRaw,
                                                   Map<String, Object> paramsMap)
            throws SQLException, NullParameterException, IllegalArgumentException {

        if (paramsMap == null || sqlRaw == null) {
            throw new NullParameterException("Nenhum dos parâmetros enviados podem ser nulos!");
        }
        if (paramsMap.containsValue(null)){
            throw new IllegalArgumentException("Nenhum dos valores dentro do mapa de parâmetros pode ser nulo!");
        }

        return prepareStatementFromMap(sqlRaw, paramsMap);
    }

    /**
     * Método auxiliar interno para preencher um `PreparedStatement` a partir de um mapa de parâmetros.
     *
     * @param sqlRaw O código SQL cru com parâmetros nomeados.
     * @param paramsMap Mapa de nomes de parâmetros para seus valores.
     * @return Um `PreparedStatement` com os parâmetros já definidos.
     * @throws SQLException Se houver um erro de banco de dados.
     * @throws IllegalArgumentException Se um parâmetro nomeado na query não for encontrado no mapa.
     */
    private PreparedStatement prepareStatementFromMap(String sqlRaw, Map<String, Object> paramsMap)
            throws SQLException {
        // guarda os parâmetros da query
        List<String> paramNames = new ArrayList<>();
        Matcher matcher = PARAM_PATTERN.matcher(sqlRaw); // regex para obter @XXXXX do código sql
        while (matcher.find()) {
            paramNames.add(matcher.group(1));
        }

        // dá replace em todos os @XXXX para ?
        sqlRaw = PARAM_PATTERN.matcher(sqlRaw).replaceAll("?");

        PreparedStatement sqlFilled = null;
        int tries = 0;
        int maxTries = 5;

        while (tries < maxTries) {
            try {
                if (tries > 0) {
                    try {
                        if (connection != null && !connection.isClosed()) {
                            connection.close();
                        }
                    } catch (Exception ignored) {}
                    connection = null;
                }

                sqlFilled = getConnection().prepareStatement(sqlRaw);

                // converte os valores
                Object[] paramsList = new Object[paramNames.size()];
                for (int i = 0; i < paramNames.size(); i++) {
                    String name = paramNames.get(i);
                    if (!paramsMap.containsKey(name)) {
                        throw new IllegalArgumentException(
                                "Parâmetro não encontrado: " + name);
                    }
                    paramsList[i] = paramsMap.get(name);
                }

                new QueryRunner().fillStatement(sqlFilled, paramsList);

                return sqlFilled;

            } catch (SQLException e) {
                tries++;
                logDebug("Tentativa " + tries + " falhou: " + e.getMessage());

                if (tries >= maxTries) {
                    throw e;
                }

                try {
                    Thread.sleep(100 * tries);
                } catch (InterruptedException ignored) {}
            }
        }

        return sqlFilled;
    }


    /**
     * Obtém uma query SQL pelo seu nome do cache.
     * @param queryName O nome da query (definido no atributo 'name' do XML).
     * @return A string SQL correspondente.
     * @throws QueryNotFoundException Se a query não for encontrada no cache.
     * @throws FileNotFoundException Se o cache estiver vazio e os arquivos não puderem ser recarregados.
     */
    public String getQueryNamed(String queryName) throws QueryNotFoundException, FileNotFoundException {
        if (queriesCache.isEmpty()){
            loadQueriesCache();
        }

        // obter key do cache é bem mais rápido
        String sql = queriesCache.get(queryName);

        if(sql == null){
            // throw está aqui porque ele PRECISA dizer que não conseguiu achar a query,
            // isso implica que o código iria quebrar se não tivesse a exception
            throw new QueryNotFoundException("Não foi possível encontrar a query com nome " + queryName + "!");
        }
        return sql;
    }
}
