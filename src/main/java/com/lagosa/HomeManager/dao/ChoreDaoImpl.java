package com.lagosa.HomeManager.dao;

import com.lagosa.HomeManager.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public void markAsDone(int choreId, UUID userId, Date dateDone) {
        String sql = "UPDATE chores SET status = ?,doneBy = ?, donedate = ? WHERE id = ?";
        jdbcTemplate.update(sql, Status.DONE.toString(),userId,dateDone,choreId);
    }

    @Override
    public List<Chore> getListOfNotDoneChores(UUID familyId) {
        String sql = "SELECT chores.id,family,submittedBy,doneBy,status,submissionDate,deadline,choreTypes.type AS typeName, chores.type AS typeId,description,title, " +
                " users.nickname AS submittedByName, userDone.nickname AS doneByName FROM chores INNER JOIN choretypes ON choreTypes.id = chores.type " +
                " INNER JOIN users ON users.id = chores.submittedBy LEFT JOIN users AS userDone ON userdone.id = chores.doneBy WHERE (family = ? AND status = 'NOT_DONE') ORDER BY deadline ASC";
        return jdbcTemplate.query(sql ,new ChoreMapper(),familyId); // also gets the name of the submitter and doer based on their id (if it was done already, else puts null
                                                                    // in those fields), and the name of the chore type
    }

    @Override
    public List<Chore> getTookUpChores(UUID userId) {
        String sql = "SELECT chores.id,family,submittedBy,doneBy,status,submissionDate,deadline,choreTypes.type AS typeName, chores.type AS typeId,description,title,users.nickname AS submittedByName, " +
                "userDone.nickname AS doneByName FROM chores INNER JOIN choretypes ON choreTypes.id = chores.type INNER JOIN users ON users.id = chores.submittedBy " +
                "INNER JOIN users AS userDone ON userDone.id = chores.doneBy WHERE doneBy = ? AND status = 'NOT_DONE' ORDER BY deadline ASC";
        return jdbcTemplate.query(sql,new ChoreMapper(),userId);// also gets the name of the submitter and doer based on their id (if it was done already, else puts null
                                                                // in those fields), and the name of the chore type
    }

    @Override
    public int getChoreTypeId(String choreType){
        String sql = "SELECT id,type FROM choreTypes WHERE type = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new ChoreTypeMapper(), choreType).getId();
        }catch (NullPointerException e){
            // if the type given could not be found in the chore type table modify the type to other
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

    @Override
    public List<Report> getReport(List<User> familyMembers) {

        String doneChores = "SELECT chores.id,chores.family,chores.submittedby,chores.doneby,chores.status,chores.submissiondate,chores.deadline,chores.type AS typeId," +
                "chores.description,chores.title,choreTypes.type AS typeName,users.nickname AS submittedByName, usersDone.nickname AS doneByName  FROM chores INNER JOIN choreTypes ON chores.type = choreTypes.id " +
                "INNER JOIN users ON users.id = chores.submittedby INNER JOIN users AS usersDone ON usersDone.id = chores.doneby WHERE status = 'DONE' AND chores.doneby = ? ORDER BY donedate DESC";
        String notFinishedChores = "SELECT chores.id,chores.family,chores.submittedby,chores.doneby,chores.status,chores.submissiondate,chores.deadline,chores.type AS typeId," +
                "chores.description,chores.title,choreTypes.type AS typeName,users.nickname AS submittedByName, usersDone.nickname AS doneByName  FROM chores INNER JOIN choreTypes ON chores.type = choreTypes.id " +
                "INNER JOIN users ON users.id = chores.submittedby INNER JOIN users AS usersDone ON usersDone.id = chores.doneby WHERE status = 'NOT_DONE' AND chores.doneby = ? ORDER BY donedate DESC";
        List<Report> reportList = new ArrayList<>();
        for(User user:familyMembers){
            // for each user in the family
            List<Chore> doneChoresList = jdbcTemplate.query(doneChores,new ChoreMapper(),user.getId()); // gets the list of done chores
            List<Chore> notFinishedChoresList = jdbcTemplate.query(notFinishedChores,new ChoreMapper(),user.getId()); // gets the list of not finished chores

            reportList.add(new Report(user,doneChoresList,notFinishedChoresList, doneChoresList.size(), notFinishedChoresList.size())); // makes a report with those lists and
                                                                                                                                        // adds it to the result list
        }

        return reportList;
    }

    @Override
    public Chore getChore(int choreId) {
        String sql = "SELECT id,family,submittedBy,doneby,status,submissiondate,deadline,type,description,title,donedate " +
                "FROM chores WHERE id = ?";
        return jdbcTemplate.queryForObject(sql,new ChoreMapper(), choreId);
    }

    private static final class ChoreMapper implements RowMapper<Chore>{

        @Override
        public Chore mapRow(ResultSet rs, int rowNum) throws SQLException {
            Chore chore = new Chore((UUID) rs.getObject("family"),(UUID)rs.getObject("submittedBy"), rs.getDate("submissionDate"),
                    rs.getDate("deadline"),rs.getString("typeName"),rs.getInt("typeId"),rs.getString("description"), rs.getString("title"));
            chore.setId(rs.getInt("id"));
            chore.setDoneBy((UUID) rs.getObject("doneBy"));
            chore.setStatus(rs.getString("status"));
            chore.setDoneByName(rs.getString("doneByName"));
            chore.setSubmitterName(rs.getString("submittedByName"));
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
