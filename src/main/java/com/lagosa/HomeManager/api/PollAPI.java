package com.lagosa.HomeManager.api;

import com.lagosa.HomeManager.model.Poll;
import com.lagosa.HomeManager.model.PollStatus;
import com.lagosa.HomeManager.model.Vote;
import com.lagosa.HomeManager.service.PollManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("poll")
public class PollAPI {

    private final PollManager pollManager;

    @Autowired
    public PollAPI(PollManager pollManager) {
        this.pollManager = pollManager;
    }

    @PostMapping(path = "/create/{userId}/{message}")
    public String createPoll(@PathVariable("userId") UUID userId,@PathVariable("message") String message){
        pollManager.createPoll(userId,message);
        return "ok";
    }

    @PostMapping(path = "/addDish/{userId}/{pollId}/{dishId}")
    public String addDishToPoll(@PathVariable("userId") UUID userId,@PathVariable("pollId") int pollId,@PathVariable("dishId") int dishId){
        pollManager.addDishToPoll(userId,pollId,dishId);
        return "ok";
    }


    @PostMapping(path = "/vote/{userId}/{dishPollId}/{vote}")
    public String vote(@PathVariable("userId") UUID userId,@PathVariable("dishPollId") int dishPollId,@PathVariable("vote") String vote){
        pollManager.vote(userId,dishPollId, Vote.valueOf(vote));
        return "ok";
    }

    @PostMapping(path = "/updateStatus/{userId}/{pollId}/{status}")
    public String updatePollStatus(@PathVariable("userId") UUID userId,@PathVariable("pollId") int pollId,@PathVariable("status") String status){
        pollManager.updatePollStatus(userId,pollId, PollStatus.valueOf(status));
        return "ok";
    }

    @GetMapping(path = "/getOpenPolls/{userId}")
    public List<Poll> getOpenPollsOfFamily(@PathVariable("userId") UUID userId){
        return pollManager.getOpenPollsOfFamily(userId);
    }

    @GetMapping(path = "/getClosedPolls/{userId}")
    public List<Poll> getClosedPollsOfFamily(@PathVariable("userId") UUID userId){
        return pollManager.getClosedPollsOfFamily(userId);
    }
}
