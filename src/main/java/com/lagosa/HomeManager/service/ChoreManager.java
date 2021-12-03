package com.lagosa.HomeManager.service;

import com.lagosa.HomeManager.dao.ChoreDao;
import com.lagosa.HomeManager.model.Chore;
import com.lagosa.HomeManager.model.ChoreType;
import com.lagosa.HomeManager.model.Report;
import com.lagosa.HomeManager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ChoreManager {

    private final ChoreDao choreDao;
    private final FamilyManager familyManager;

    @Autowired
    public ChoreManager(ChoreDao choreDao,FamilyManager familyManager){
        this.choreDao = choreDao;
        this.familyManager = familyManager;
    }

    /**
     * Creates a new chore and stores it in the database
     * @param submitterId id of the submitter
     * @param deadline deadline of the chore
     * @param type the type of the chore
     * @param description short description of the task
     * @param title sort title of the chore
     */
    public void createChore(UUID submitterId, String deadline, String type, String description,String title){
        User submitter = familyManager.getUser(submitterId); // gets information about the submitter based on its id
        int typeId = choreDao.getChoreTypeId(type); // gets the id of the chore type
        Chore newChore = new Chore(submitter.getFamilyId(),submitterId, Date.valueOf(LocalDate.now()),Date.valueOf(deadline),type,typeId,description, title);
        // creates a new chore with the date of submission the current date
        choreDao.createChore(newChore);
    }

    /**
     * Removes a chore from the database if the submitter is the person that wants to delete it
     * @param choreId the id of the chore to be removed
     * @param userId the user that wants to delete id
     */
    public void deleteChore(int choreId, UUID userId)throws Exception{
        Chore chore = choreDao.getChore(choreId);
        if(chore != null && chore.getSubmittedBy() == userId){
            choreDao.deleteChore(choreId);
        }else{
            throw new Exception("Can't delete chore! You are not the owner of the chore!");
        }

    }

    /**
     * Assigns a chore to a user
     * @param choreId id of the chore that needs to be assigned to
     * @param userId id of the user that needs to be assigned
     */
    public void takeUpChore(int choreId, UUID userId){
        choreDao.takeUpChore(choreId,userId);
    }

    /**
     * Marks a chore as done
     * @param choreId the id of the chore that needs to be updated
     * @param userId the id of the user that did the chore
     */
    public void markAsDone(int choreId, UUID userId){
        choreDao.markAsDone(choreId,userId,Date.valueOf(LocalDate.now()));
    }

    /**
     * Gets a list of chores that were not done in a family
     * @param userId user who called the function
     * @return a list of chores that were not finished yet in the family of the user
     */
    public List<Chore> getListOfNotDoneChores(UUID userId){
        User user = familyManager.getUser(userId);
        return choreDao.getListOfNotDoneChores(user.getFamilyId());
    }

    /**
     * Gets a list of chores that were taken up by a user
     * @param userId user whose chores are sought
     * @return a list of chores not finished yet
     */
    public List<Chore> getTookUpChores(UUID userId){
        return choreDao.getTookUpChores(userId);
    }

    /**
     * Modifies the deadline of a chore if the submitter is the modification requester
     * @param choreId chore that needs to be updated
     * @param userId the user that requested the modification
     * @param newDeadline the new deadline
     */
    public void changeDeadline(int choreId,UUID userId ,String newDeadline)throws Exception{
        Chore chore = choreDao.getChore(choreId);
        if(chore != null && chore.getSubmittedBy() == userId){
            choreDao.changeDeadline(choreId,Date.valueOf(newDeadline));
        }else{
            throw new Exception("Can't modify chore! You are not the submitter!");
        }
    }

    /**
     * Gets all the chore types
     * @return all the chore types
     */
    public List<ChoreType> getChoreTypes(){
        return choreDao.getChoreTypes();
    }

    /**
     * Gets a list of reports for every member of a family of the user
     * @param userId the user for whose family the report needs to be made
     * @return a list of reports for every family member
     */
    public List<Report> getReport(UUID userId){
        User user = familyManager.getUser(userId);
        return choreDao.getReport(familyManager.getFamilyMembers(user.getFamilyId()));
    }
}
