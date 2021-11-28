package com.lagosa.HomeManager.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class FamilyDaoImpl implements FamilyDao{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FamilyDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void registerFamily(UUID id, String emailAddress, String password) {
        String sql = "INSERT INTO families (id,email,password) VALUES (?,?,?)";
        jdbcTemplate.update(sql,new Object[]{id,emailAddress,password});
    }
}
