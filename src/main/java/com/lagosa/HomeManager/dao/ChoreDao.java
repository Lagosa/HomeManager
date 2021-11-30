package com.lagosa.HomeManager.dao;

import com.lagosa.HomeManager.model.Chore;

import java.util.List;
import java.util.UUID;

public interface ChoreDao {
    void createChore(Chore chore);
    void deleteChore(int choreId);
    void takeUpChore(int choreId,int userId);
    void markAsDone(int choreId,int userId);
    List<Chore> getListOfUndoneChore(UUID familyId);
    List<Chore> getTookUpChores(int userId);
    int getChoreTypeId(String choreType);
}
