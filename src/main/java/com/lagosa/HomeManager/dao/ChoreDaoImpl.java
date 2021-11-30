package com.lagosa.HomeManager.dao;

import com.lagosa.HomeManager.model.Chore;
import com.lagosa.HomeManager.model.ChoreType;
import com.lagosa.HomeManager.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
public class ChoreDaoImpl implements ChoreDao{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ChoreDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createChore(Chore chore) {
        String sql = "INSERT INTO chores (family,submittedBy,deadline,type,description,title) VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(sql,chore.getFamily(),chore.getSubmittedBy(),chore.getDeadline(),chore.getTypeId(),chore.getDescription(),chore.getTitle());
    }

    @Override
    public void deleteChore(int choreId) {
        String sql = "DELETE FROM chores WHERE id = ?";
        jdbcTemplate.update(sql,choreId);
    }

    @Override
    public void takeUpChore(int choreId, UUID userId) {
        String sql = "UPDATE chores SET doneBy = ? WHERE id = ?";
        jdbcTemplate.update(sql, userId, choreId);
    }

    @Override
    public void markAsDone(int choreId, UUID userId) {
        String sql = "UPDATE chores SET status = ?,doneBy = ? WHERE id = ?";
        jdbcTemplate.update(sql, Status.DONE.toString(),userId,choreId);
    }

    @Override
    public List<Chore> getListOfNotDoneChores(UUID familyId) {
        String sql = "SELECT chores.id,family,submittedBy,doneBy,status,submissionDate,deadline,choreTypes.type AS typeName, chores.type AS typeId,description,title " +
                "FROM chores INNER JOIN choretypes ON choreTypes.id = chores.type WHERE (family = ? AND status = 'NOT_DONE') ORDER BY deadline ASC";
        return jdbcTemplate.query(sql ,new ChoreMapper(),familyId);
    }

    @Override
    public List<Chore> getTookUpChores(UUID userId) {
        String sql = "SELECT chores.id,family,submittedBy,doneBy,status,submissionDate,deadline,choreTypes.type AS typeName, chores.type AS typeId,description,title " +
                "FROM chores INNER JOIN choretypes ON choreTypes.id = chores.type WHERE doneBy = ? AND status = 'NOT_DONE'";
        return jdbcTemplate.query(sql,new ChoreMapper(),userId);
    }

    @Override
    public int getChoreTypeId(String choreType){
        String sql = "SELECT id,type FROM choreTypes WHERE type = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new ChoreTypeMapper(), choreType).getId();
        }catch (NullPointerException e){
            return Objects.requireNonNull(jdbcTemplate.queryForObject(sql, new ChoreTypeMapper(), "other")).getId();
        }
    }

    @Override
    public void changeDeadline(int choreId, Date newDeadline) {
        String sql = "UPDATE chores SET deadline = ? WHERE id = ?";
        jdbcTemplate.update(sql,newDeadline,choreId);
    }

    @Override
    public List<ChoreType> getChoreTypes() {
        String sql = "SELECT id,type FROM choreTypes";
        return jdbcTemplate.query(sql,new ChoreTypeMapper());
    }

    private static final class ChoreMapper implements RowMapper<Chore>{

        @Override
        public Chore mapRow(ResultSet rs, int rowNum) throws SQLException {
            Chore chore = new Chore((UUID) rs.getObject("family"),(UUID)rs.getObject("submittedBy"),rs.getDate("submissionDate"),
                    rs.getDate("deadline"),rs.getString("typeName"),rs.getInt("typeId"),rs.getString("description"), rs.getString("title"));
            chore.setId(rs.getInt("id"));
            chore.setDoneBy((UUID) rs.getObject("doneBy"));
            chore.setStatus(rs.getString("status"));
            return chore;
        }
    }

    private static final class ChoreTypeMapper implements RowMapper<ChoreType>{

        @Override
        public ChoreType mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ChoreType(rs.getInt("id"), rs.getString("type"));
        }
    }
}
