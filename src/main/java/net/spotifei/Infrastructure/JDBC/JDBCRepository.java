package net.spotifei.Infrastructure.JDBC;

import net.spotifei.Exceptions.NullParameterException;
import net.spotifei.Exceptions.QueryNotFoundException;
import net.spotifei.Helpers.ParametersHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.spotifei.Helpers.AssetsLoader.getQueriesFiles;

public class JDBCRepository {
    private final ConcurrentMap<String, String> queriesCache = new ConcurrentHashMap<>();
    private static final Pattern PARAM_PATTERN = Pattern.compile("@(\\w+)");
    // Sobre o regex: o @ significa pegar caractere @ os () significa todos os que estão depois do @
    // e o \\w+ um ou mais quaisquer caracteres de a-z A-Z + underscore
    private static String url;
    private static String user;
    private static String password;
    private Connection _connection;

    private Connection getConnection() throws SQLException {
        if(_connection == null || _connection.isClosed()){
            openConnection();
        }
        return _connection;
    }

    private void openConnection() throws SQLException {
        _connection = DriverManager.getConnection(
                url, user, password
        );
    }

    private void closeConnection() throws SQLException {
        _connection.close();
    }

    private ResultSet executeQuery(String sql, Object params) throws SQLException, RuntimeException {
        return getConnection().prepareStatement(getPreparedStatement(sql, params).toString()).executeQuery();
    }

    /**
     * @implNote Use para gerar um PreparedStatement em um comando que não precisa de parâmetros
     * @param queryCode Código SQL obtido pelo getQueryNamed
     * @return Retorna o PreparedStatement do código SQL
     * @throws SQLException
     */
    private PreparedStatement getPreparedStatement(String queryCode) throws SQLException {
        return _connection.prepareStatement(queryCode);
    }

    private PreparedStatement getPreparedStatement(String sqlRaw,
                                                   Object classParam)
            throws SQLException, NullParameterException {

        if (classParam == null || sqlRaw == null) {
            throw new NullParameterException("Nenhum dos parâmetros enviados podem ser nulos!");
        }

        // guarda os parâmetros da query
        List<String> paramNames = new ArrayList<>();
        Matcher matcher = PARAM_PATTERN.matcher(sqlRaw); // regex para obter @XXXXX do código sql
        while (matcher.find()) {
            paramNames.add(matcher.group(1));
        }

        // dá replace em todos os @XXXX para ?
        sqlRaw = PARAM_PATTERN.matcher(sqlRaw).replaceAll("?");

        PreparedStatement sqlFilled = _connection.prepareStatement(sqlRaw);

        Map<String, Object> paramValues = new ParametersHelper()
                .getParametersFromObject(classParam);

        // converte os valores
        Object[] paramsList = new Object[paramNames.size()];
        for (int i = 0; i < paramNames.size(); i++) {
            String name = paramNames.get(i);
            if (!paramValues.containsKey(name)) {
                throw new IllegalArgumentException(
                        "Parâmetro não encontrado na classe: " + name);
            }
            paramsList[i] = paramValues.get(name);
        }

        new QueryRunner().fillStatement(sqlFilled, paramsList);

        return sqlFilled;
    }

    private String getQueryNamed(String queryName) throws QueryNotFoundException {
        // obter key do cache é bem mais rápido
        if(queriesCache.containsKey(queryName)){
            return queriesCache.get(queryName);
        }

        DocumentBuilder builder;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Erro ao criar o parser XML", e);
        }

        try{
            File[] xmlFiles = getQueriesFiles();
            for (File xmlFile : xmlFiles){
                Document doc = builder.parse(xmlFile);
                NodeList queries = doc.getElementsByTagName("query");
                for (int i = 0; i < queries.getLength(); i++){
                    if (queries.item(i).getAttributes().getNamedItem("name").getTextContent().equals(queryName)){
                        String sql = queries.item(i).getTextContent().trim();
                        queriesCache.put(queryName, sql);
                        return sql;
                    }
                }
            }
            // throw está aqui porque ele PRECISA dizer que não conseguiu achar a query,
            // isso implica que o código iria quebrar se não tivesse a exception
            throw new QueryNotFoundException("Não foi possível encontrar a query com nome " + queryName + "!");
        } catch (IOException | IllegalArgumentException | SAXException e) {
            throw new RuntimeException("Erro ao dar parse nos arquivos XML!",e);
        }

    }


}
