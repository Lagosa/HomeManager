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

    public void createChore(UUID submitterId, String deadline, String type, String description,String title){
        User submitter = familyManager.getUser(submitterId);
        int typeId = choreDao.getChoreTypeId(type);
        Chore newChore = new Chore(submitter.getFamilyId(),submitterId, Date.valueOf(LocalDate.now()),Date.valueOf(deadline),type,typeId,description, title);
        choreDao.createChore(newChore);
    }

    public void deleteChore(int choreId){
        choreDao.deleteChore(choreId);
    }

    public void takeUpChore(int choreId, UUID userId){
        choreDao.takeUpChore(choreId,userId);
    }

    public void markAsDone(int choreId, UUID userId){
        choreDao.markAsDone(choreId,userId,Date.valueOf(LocalDate.now()));
    }

    public List<Chore> getListOfNotDoneChores(UUID userId){
        User user = familyManager.getUser(userId);
        return choreDao.getListOfNotDoneChores(user.getFamilyId());
    }

    public List<Chore> getTookUpChores(UUID userId){
        return choreDao.getTookUpChores(userId);
    }

    public void changeDeadline(int choreId, String newDeadline){
        choreDao.changeDeadline(choreId,Date.valueOf(newDeadline));
    }

    public List<ChoreType> getChoreTypes(){
        return choreDao.getChoreTypes();
    }

    public List<Report> getReport(UUID userId){
        User user = familyManager.getUser(userId);
        return choreDao.getReport(familyManager.getFamilyMembers(user.getFamilyId()));
    }
}
