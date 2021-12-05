package com.lagosa.HomeManager.dao;

import com.lagosa.HomeManager.model.Poll;
import com.lagosa.HomeManager.model.PollStatus;
import com.lagosa.HomeManager.model.Vote;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PollDao {

    void createPoll(Poll poll);
    void addDishToPoll(int pollId, int dishId);
    void vote(UUID userId, int dishPollId, Vote vote);
    void setStatus(int pollId, PollStatus status);

    List<Poll> getPollsOfAFamily(UUID familyId,PollStatus status);
    List<Map<String,Object>> getVotersOfDish(int dishPoleId,Vote vote);

    Poll getPollById(int pollId);
    Map<String,Integer> getPollDishById(int pollDish);
}
