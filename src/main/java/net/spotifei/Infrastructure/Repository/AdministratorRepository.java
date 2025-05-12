package net.spotifei.Infrastructure.Repository;

import net.spotifei.Infrastructure.JDBC.JDBCRepository;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class AdministratorRepository {
    private final JDBCRepository jdbcRepository;

    public AdministratorRepository(JDBCRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
    }

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
