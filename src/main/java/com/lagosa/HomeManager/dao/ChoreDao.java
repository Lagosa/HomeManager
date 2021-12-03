package com.lagosa.HomeManager.dao;

import com.lagosa.HomeManager.model.Chore;
import com.lagosa.HomeManager.model.ChoreType;
import com.lagosa.HomeManager.model.Report;
import com.lagosa.HomeManager.model.User;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface ChoreDao {
    /**
     * Creates a new chore in the database
     * @param chore the chore that needs to be inserted
     */
    void createChore(Chore chore);

    /**
     * Removes a chore from the database
     * @param choreId the id of the chore that needs to be removed
     */
    void deleteChore(int choreId);

    /**
     * Assigns a user to a chore
     * @param choreId the chore to which it needs to be assigned
     * @param userId the user that needs to be assigned
     */
    void takeUpChore(int choreId,UUID userId);

    /**
     * Marks a chore as done
     * @param choreId the chore that needs to be updated
     * @param userId the user that made the chore
     * @param dateDone the date when the chore was done
     */
    void markAsDone(int choreId,UUID userId,Date dateDone);

    /**
     * Gets a list of chores that were not been done within the family
     * @param familyId the family whose chores need to be interrogated
     * @return the list of not done chores
     */
    List<Chore> getListOfNotDoneChores(UUID familyId);

    /**
     * Gets a list of chores that were taken up by a user, but were not done yet
     * @param userId the user whose chores need to be interrogated
     * @return the list of chores not done yet
     */
    List<Chore> getTookUpChores(UUID userId);

    /**
     * Gets the id of a chore type
     * @param choreType the chore type
     * @return the id of the chore type
     */
    int getChoreTypeId(String choreType);

    /**
     * Modifies the deadline of a chore
     * @param choreId the chore that needs to be updated
     * @param newDeadline the new deadline
     */
    void changeDeadline(int choreId, Date newDeadline);

    /**
     * Gets a list all the types of chores
     * @return the list of chores
     */
    List<ChoreType> getChoreTypes();

    /**
     * Gets a list of reports for each member of a family, containing the done and the not finished chores
     * @param familyMembers the family for which the report needs to be made
     * @return a list of reports
     */
    List<Report> getReport(List<User> familyMembers);

    /**
     * Gets a chore by its id
     * @param choreId the id of the chore sought
     * @return the chore with that id
     */
    Chore getChore(int choreId);
}
