package com.lagosa.HomeManager.dao;

import com.lagosa.HomeManager.model.Chore;
import com.lagosa.HomeManager.model.ChoreType;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface ChoreDao {
    void createChore(Chore chore);
    void deleteChore(int choreId);
    void takeUpChore(int choreId,UUID userId);
    void markAsDone(int choreId,UUID userId);
    List<Chore> getListOfNotDoneChores(UUID familyId);
    List<Chore> getTookUpChores(UUID userId);
    int getChoreTypeId(String choreType);
    void changeDeadline(int choreId, Date newDeadline);
    List<ChoreType> getChoreTypes();
}
