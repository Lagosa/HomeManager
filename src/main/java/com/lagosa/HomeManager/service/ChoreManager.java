package com.lagosa.HomeManager.service;

import com.lagosa.HomeManager.dao.ChoreDao;
import com.lagosa.HomeManager.model.Chore;
import com.lagosa.HomeManager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
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

    public void createChore(UUID submitterId, String deadline, String type, String description){
        User submitter = familyManager.getUser(submitterId);
        int typeId = choreDao.getChoreTypeId(type);
        Chore newChore = new Chore(submitter.getFamilyId(),submitterId,Date.valueOf(LocalDate.now()),Date.valueOf(deadline),type,typeId,description);
        choreDao.createChore(newChore);
    }
}
