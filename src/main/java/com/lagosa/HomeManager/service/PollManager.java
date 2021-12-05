package com.lagosa.HomeManager.service;

import com.lagosa.HomeManager.dao.PollDao;
import com.lagosa.HomeManager.exceptions.ApiRequestException;
import com.lagosa.HomeManager.model.Poll;
import com.lagosa.HomeManager.model.PollStatus;
import com.lagosa.HomeManager.model.User;
import com.lagosa.HomeManager.model.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PollManager {

    private final PollDao pollDao;
    private final FamilyManager familyManager;
    private final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Autowired
    public PollManager(PollDao pollDao, FamilyManager familyManager) {
        this.pollDao = pollDao;
        this.familyManager = familyManager;
    }

    public void createPoll(UUID userId, String message){
        User user = familyManager.getUser(userId);
        Poll newPoll = new Poll(user.getFamilyId(),message);
        pollDao.createPoll(newPoll);
    }

    public void addDishToPoll(UUID userId, int pollId,int dishId){
        Poll poll = pollDao.getPollById(pollId);
        User user = familyManager.getUser(userId);

        log.log(Level.WARNING,"pollFAMILY: " + poll.getFamily() + " -- userFamily: " + user.getFamilyId());

        if(isUserFamilyOwnerOfPoll(userId,pollId)){
            pollDao.addDishToPoll(pollId,dishId);
        }else {
            throw new ApiRequestException("Your family does not own the poll!");
        }
    }

    public void vote(UUID userId, int dishPollId, Vote vote){
        User user = familyManager.getUser(userId);
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("userId",userId);
        userMap.put("userName",user.getNickName());
        userMap.put("vote",Vote.FOR.toString());

        List<Map<String,Object>> votersForDish = pollDao.getVotersOfDish(dishPollId,Vote.FOR);
        if(votersForDish.contains(userMap)){
            throw new ApiRequestException("You already voted!");
        }

        userMap.put("vote",Vote.AGAINST.toString());
        List<Map<String,Object>> votersAgainstDish = pollDao.getVotersOfDish(dishPollId,Vote.AGAINST);
        if(votersAgainstDish.contains(userMap)){
            throw new ApiRequestException("You already voted!");
        }

        pollDao.vote(userId,dishPollId,vote);

        List<User> familyMembers = familyManager.getFamilyMembers(user.getFamilyId());
        if(votersAgainstDish.size() + votersForDish.size() + 1 == familyMembers.size()){
            Poll poll = pollDao.getPollById(pollDao.getPollDishById(dishPollId).get("pollId"));
            pollDao.setStatus(poll.getId(), PollStatus.CLOSED);
        }
    }

    public void updatePollStatus(UUID userId,int pollId,PollStatus status){
        if(isUserFamilyOwnerOfPoll(userId,pollId)){
            pollDao.setStatus(pollId,status);
        }else{
            throw new ApiRequestException("Your family does not own the poll!");
        }
    }

    public List<Poll> getOpenPollsOfFamily(UUID userId){
        User user = familyManager.getUser(userId);
        return pollDao.getPollsOfAFamily(user.getFamilyId(),PollStatus.OPEN);
    }

    public List<Poll> getClosedPollsOfFamily(UUID userId){
        User user = familyManager.getUser(userId);
        return pollDao.getPollsOfAFamily(user.getFamilyId(),PollStatus.CLOSED);
    }

    private boolean isUserFamilyOwnerOfPoll(UUID userId, int pollId){
        User user = familyManager.getUser(userId);
        Poll poll = pollDao.getPollById(pollId);
        return user.getFamilyId().equals(poll.getFamily());
    }
}
