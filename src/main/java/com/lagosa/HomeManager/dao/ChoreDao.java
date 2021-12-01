package com.lagosa.HomeManager.dao;

import com.lagosa.HomeManager.model.Chore;
import com.lagosa.HomeManager.model.ChoreType;
import com.lagosa.HomeManager.model.Report;
import com.lagosa.HomeManager.model.User;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface ChoreDao {
    void createChore(Chore chore);
    void deleteChore(int choreId);
    void takeUpChore(int choreId,UUID userId);
    void markAsDone(int choreId,UUID userId,Date dateDone);
    List<Chore> getListOfNotDoneChores(UUID familyId);
    List<Chore> getTookUpChores(UUID userId);
    int getChoreTypeId(String choreType);
    void changeDeadline(int choreId, Date newDeadline);
    List<ChoreType> getChoreTypes();
    List<Report> getReport(List<User> familyMembers);
}
