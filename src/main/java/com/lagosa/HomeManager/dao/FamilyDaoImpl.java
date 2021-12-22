package com.lagosa.HomeManager.dao;

import com.lagosa.HomeManager.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public List<User> getFamilyMembers(UUID familyId) {
        String sql = "SELECT id,familyid,nickname,role,birthdate FROM users WHERE familyid = ?";
        return jdbcTemplate.query(sql,new UserMapper(),familyId);
    }

    @Override
    public void sendNotification(Notification notification) {
        String sql = "INSERT INTO notifications (sender,receiver,title,message) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql,notification.getSender(),notification.getReceiver(),notification.getTitle(),notification.getMessage());
    }

    @Override
    public List<Notification> getNotifications(UUID userId) {
        String sql = "SELECT n.id,n.sender,n.receiver,n.title,n.message,n.dateSent,n.status,uR.nickname AS receiverName, uS.nickname AS senderName " +
                "FROM notifications AS n INNER JOIN users AS uR ON uR.id = n.receiver INNER JOIN users AS uS ON uS.id = n.sender WHERE n.receiver = ? AND n.status = 'SENT'";
        return jdbcTemplate.query(sql,new NotificationMapper(),userId);
    }

    @Override
    public void updateNotificationStatus(int notificationId, NotificationStatus status) {
        String sql = "UPDATE notifications SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql,status.toString(),notificationId);
    }

    @Override
    public void setArrival(UUID familyId, UUID userId, ArrivalStatus status) {
        String sql = "INSERT INTO arrivals (family,userId,status) VALUES (?,?,?)";
        jdbcTemplate.update(sql,familyId,userId,status.toString());
    }

    @Override
    public List<Map<String, Object>> getArrivalList(UUID family, Date startDate, Date endDate) {
        String sql = "SELECT u.nickname AS nickname,a.status,a.timestampCol FROM arrivals AS a INNER JOIN users AS u ON u.id = a.userId WHERE family = ? AND (date >= ? AND date <= ?) ORDER BY timestampCol DESC";
        return jdbcTemplate.query(sql,(rs,rowNum) ->{
            Map<String,Object> newMap = new HashMap<>();
            newMap.put("nickname",rs.getString("nickname"));
            newMap.put("status",rs.getString("status"));
            newMap.put("date",rs.getTimestamp("timestampCol"));
            return newMap;
        },family,startDate,endDate);
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

    private static final class NotificationMapper implements RowMapper<Notification>{

        @Override
        public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {
            Notification notification = new Notification((UUID) rs.getObject("sender"),(UUID) rs.getObject("receiver"),
                    rs.getString("title"),rs.getString("message"));
            notification.setId(rs.getInt("id"));
            notification.setStatus(rs.getString("status"));
            notification.setDateSent(rs.getDate("dateSent"));
            notification.setReceiverName(rs.getString("receiverName"));
            notification.setSenderName(rs.getString("senderName"));
            return notification;
        }
    }
}
