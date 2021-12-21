package com.lagosa.HomeManager.dao;

import com.lagosa.HomeManager.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Repository
public class PollDaoImpl implements PollDao{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PollDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void createPoll(Poll poll) {
        String sql = "INSERT INTO dishPolls (family,message) VALUES (?,?)";
        jdbcTemplate.update(sql,poll.getFamily(),poll.getMessage());
    }

    @Override
    public void addDishToPoll(int pollId, int dishId) {
        String sql = "INSERT INTO dishPolls_dish (poll,dish) VALUES (?,?)";
        jdbcTemplate.update(sql,pollId,dishId);
    }

    @Override
    public void vote(UUID userId, int dishPollId, Vote vote) {
        String sql = "INSERT INTO dishPoll_dish_user (dish,userId,vote) VALUES (?,?,?)";
        jdbcTemplate.update(sql,dishPollId,userId,vote.toString());
    }

    @Override
    public void setStatus(int pollId, PollStatus status) {
        String sql = "UPDATE dishPolls SET status = ?, lastUpdated = ? WHERE id = ?";
        jdbcTemplate.update(sql,status.toString(), Date.valueOf(LocalDate.now()),pollId);
    }

    @Override
    public List<Poll> getPollsOfAFamily(UUID familyId,PollStatus status) {
        String sql = "SELECT dp.id,dp.family,dp.message,dp.status FROM dishPolls AS dp WHERE dp.family = ? AND dp.status = ?";
        List<Poll> polls = jdbcTemplate.query(sql, new PollMapper(),familyId,status.toString());

        sql = "SELECT dp.id, dp.dish AS dishId, d.name AS dishName FROM dishPolls_dish AS dp " +
                "INNER JOIN dishes AS d ON d.id = dp.dish WHERE dp.poll = ?";

        for(Poll poll : polls){ // for each poll of a given family that has an OPEN status
            // get the list of dishes that are in the pole, each dish is represented as a map containing the id,dishId,name
            List<Map<String,Object>> dishes = jdbcTemplate.query(sql,(rs,rowNUm) -> {
                // for each result row make the a new map
                Map<String,Object> newMap = new HashMap<>();

                // assign the keys to the values
                newMap.put("id",rs.getInt("id"));
                newMap.put("dishId",rs.getInt("dishId"));
                newMap.put("dishName",rs.getString("dishName"));

                // get the users that voted FOR the dish, a user consists of its id,nickname and vote choice
                newMap.put("forList",getVotersOfDish((Integer) newMap.get("id"),Vote.FOR)); // put the list of users that voted FOR into a specific field of the dish

                // get the users that voted AGAINST a dish
                newMap.put("againstList",getVotersOfDish((Integer) newMap.get("id"),Vote.AGAINST)); // put the list of users that voted AGAINST into a specific field of the dish

                return newMap;
            },poll.getId());

            poll.setDishes(dishes); // add to the dishes field of the poll the newly created list of maps
        }

        return polls;
    }

    @Override
    public List<Map<String, Object>> getVotersOfDish(int dishPoleId,Vote vote) {
        String sql = "SELECT dpu.userid,dpu.vote,u.nickname AS userName FROM dishpoll_dish_user AS dpu INNER JOIN users AS u ON u.id = dpu.userid WHERE dpu.dish = ? AND dpu.vote = ?";
        return jdbcTemplate.query(sql,(rs,rowNum) ->{
            Map<String,Object> map = new HashMap<>();
            map.put("userId",(UUID) rs.getObject("userid"));
            map.put("userName",rs.getString("userName"));
            map.put("vote", rs.getString("vote"));

            return map;
        },dishPoleId,vote.toString());
    }

    @Override
    public Poll getPollById(int pollId) {
        String sql = "SELECT id,family,message,status FROM dishpolls WHERE id = ?";
        return jdbcTemplate.queryForObject(sql,new PollMapper(),pollId);
    }

    @Override
    public Map<String, Integer> getPollDishById(int pollDish) {
        String sql = "SELECT poll,dish,id FROM dishpolls_dish WHERE id = ?";
        return jdbcTemplate.queryForObject(sql,(rs,rownUM) -> {
            Map<String, Integer> map = new HashMap<>();
            map.put("pollId",rs.getInt("poll"));
            map.put("id",rs.getInt("id"));
            map.put("dishId",rs.getInt("dish"));
            return map;
        },pollDish);
    }

    private final static class PollMapper implements RowMapper<Poll>{

        @Override
        public Poll mapRow(ResultSet rs, int rowNum) throws SQLException {
            Poll poll = new Poll((UUID) rs.getObject("family"),rs.getString("message"));
            poll.setStatus(PollStatus.valueOf(rs.getString("status")));
            poll.setId(rs.getInt("id"));
            return poll;
        }
    }
}
