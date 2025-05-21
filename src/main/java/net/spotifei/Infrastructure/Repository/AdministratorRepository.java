package net.spotifei.Infrastructure.Repository;

//imports
import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class AdministratorRepository {
    private final JDBCRepository jdbcRepository;

    /**
     * Construtor da classe.
     *
     * @param jdbcRepository A instância do JDBCRepository a ser utilizada para as operações de banco de dados.
     */
    public AdministratorRepository(JDBCRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
    }

    /**
     * Verifica se um usuário é um administrador pelo seu ID.
     *
     * @param userId O ID do usuário a ser verificado.
     * @return Retorna `1` se o usuário for um administrador, `0` caso contrário.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados.
     */
    public int checkUserAdminById(int userId) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idUser", userId);

            ScalarHandler<Integer> scalar = new ScalarHandler<>();

            String sql = jdbcRepository.getQueryNamed("CheckUserAdminById");
            Integer isUserAdmin = jdbcRepository.queryProcedure(sql, params, scalar);

            return isUserAdmin == null ? 0 : isUserAdmin;
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Atualiza o último login de um administrador.
     *
     * @param adminId O ID do administrador cujo último login será atualizado.
     * @throws Exception Se ocorrer um erro durante a execução da procedure no banco de dados.
     */
    public void updateAdminLastLoginById(int adminId) throws Exception{
        try{
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            Map<String, Object> params = new HashMap<>();
            params.put("idAdministrator", adminId);
            params.put("lastLogin", timestamp);

            String sql = jdbcRepository.getQueryNamed("UpdateAdminLastLoginById");
            jdbcRepository.executeProcedure(sql, params);

        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Obtém o ID de um administrador pelo seu endereço de e-mail.
     *
     * @param email O endereço de e-mail do administrador.
     * @return O ID do administrador.
     * @throws Exception Se ocorrer um erro durante a execução da query no banco de dados,
     * ou se o administrador não for encontrado (resultando em 0 ou uma exceção dependendo do handler).
     */
    public int getAdminIdByEmail(String email) throws Exception{
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("email", email);

            String sql = jdbcRepository.getQueryNamed("GetAdminIdByEmail");
            int adminId = jdbcRepository.queryProcedure(sql, params, new ScalarHandler<>());

            return adminId;
        } catch (Exception e){
            throw e;
        }
    }
}
