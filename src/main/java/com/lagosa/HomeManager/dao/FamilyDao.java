package com.lagosa.HomeManager.dao;

import com.lagosa.HomeManager.model.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FamilyDao {
    /**
     * Registers a family in the database
     * @param family the family that needs to be registered
     */
    void registerFamily(Family family);

    /**
     * Gets a family based on the join code
     * @param joinCode the join code used to identify the family
     * @return a family object containing all the information about that family
     */
    Family getFamily(int joinCode);

    /**
     * Gets a family based on the id
     * @param id the id used to identify the family
     * @return a family object containing all the information of that family
     */
    Family getFamily(UUID id);

    /**
     * Gets a family based on its email
     * @param email the email used to identify the family
     * @return a family object caontaining all the data of the family
     */
    Family getFamily(String email);

    /**
     * Registers a new user in the database
     * @param user the user that needs to be registered
     */
    void joinFamily(User user);

    /**
     * Gets a user based in its id
     * @param id the id used to identify the user
     * @return a user object containing all the data of the user
     */
    User getUser(UUID id);

    /**
     * Gets all the join codes from the families database
     * @return a list containing all the join codes used
     */
    List<Integer> getAllJoinCodes();

    /**
     * Gets all the family members of a family
     * @param familyId the id of the family whose members are sought
     * @return a list of users representing family members
     */
    List<User> getFamilyMembers(UUID familyId);

    void sendNotification(Notification notification);
    List<Notification> getNotifications(UUID userId);
    void updateNotificationStatus(int notificationId, NotificationStatus status);

    void setArrival(UUID familyId, UUID userId, ArrivalStatus status);
    List<Map<String,Object>> getArrivalList(UUID family, Date startDate, Date endDate);
}
