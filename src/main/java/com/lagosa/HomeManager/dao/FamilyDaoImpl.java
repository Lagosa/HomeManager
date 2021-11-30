package com.lagosa.HomeManager.dao;

import com.lagosa.HomeManager.model.Family;
import com.lagosa.HomeManager.model.Roles;
import com.lagosa.HomeManager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Repository
public class FamilyDaoImpl implements FamilyDao{

    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


    @Autowired
    public FamilyDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void registerFamily(Family family) {
        String sql = "INSERT INTO families (id,email,password,joincode) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql, family.getId(),family.getEmail(),family.getPassword(),family.getJoinCode());
    }

    @Override
    public Family getFamily(int joinCode) {
        String sql = "SELECT id,email,password,joincode FROM families WHERE joincode = ?";
        try{
            return jdbcTemplate.queryForObject(sql,new FamilyMapper(),joinCode);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public Family getFamily(UUID id) {
        String sql = "SELECT id,email,password,joincode FROM families WHERE id = ?";
        return jdbcTemplate.queryForObject(sql,new FamilyMapper(),id);

    }

    @Override
    public Family getFamily(String email) {
        String sql = "SELECT id,email,password,joincode FROM families WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new FamilyMapper(), email);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public void joinFamily(User user) {

        String sql = "INSERT INTO users (id,familyid,nickname,role,birthdate) VALUES (?,?,?,?,?)";
        jdbcTemplate.update(sql,user.getId(),user.getFamilyId(),user.getNickName(),user.getRoleString(),user.getBirthDate());
    }

    @Override
    public User getUser(UUID id) {
        String sql = "SELECT id,familyid,nickname,role,birthdate FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql,new UserMapper(),id);
    }

    @Override
    public List<Integer> getAllJoinCodes() {
        String sql = "SELECT joincode FROM families";
        return jdbcTemplate.query(sql,(rs,rowcount) -> rs.getInt("joincode"));
    }


    private static final class FamilyMapper implements RowMapper<Family> {
        // maps all the columns of the families table to fields in a family object
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        public Family mapRow(ResultSet rs, int rowNum) throws SQLException {

            //logger.log(Level.WARNING,"Rs: " + rs.first() + " rows: " + rowNum);
            try{
                Family fam = new Family((java.util.UUID) rs.getObject("id"),rs.getString("email"),
                        rs.getString("password"),rs.getInt("joincode"));
                return fam;
            }catch (NullPointerException e){
                return null;
            }
        }
    }
    private static final class UserMapper implements RowMapper<User> {
        // maps all the columns in the users table to fields in a user object
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User((java.util.UUID) rs.getObject("id"),(UUID) rs.getObject("familyid"),
                                rs.getString("nickname"), Roles.valueOf(rs.getString("role")),
                                rs.getDate("birthdate"));
            return user;
        }
    }
}
