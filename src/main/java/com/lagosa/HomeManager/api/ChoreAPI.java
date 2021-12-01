package com.lagosa.HomeManager.api;

import com.lagosa.HomeManager.model.Chore;
import com.lagosa.HomeManager.model.ChoreType;
import com.lagosa.HomeManager.model.Report;
import com.lagosa.HomeManager.service.ChoreManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("chore")
public class ChoreAPI {

    private final ChoreManager choreManager;

    @Autowired
    public ChoreAPI(ChoreManager choreManager){
        this.choreManager = choreManager;
    }

    @PostMapping(path = "/create/{submitterId}/{deadline}/{type}/{title}")
    public void createChore(@PathVariable("submitterId") UUID submitterId, @PathVariable("deadline") String deadline, @PathVariable("type") String type,
                            @RequestBody String description,@PathVariable("title") String title){
        choreManager.createChore(submitterId,deadline,type,description,title);
    }

    @PostMapping(path = "/delete/{choreId}")
    public void deleteChore(@PathVariable("choreId") int choreId){
        choreManager.deleteChore(choreId);
    }

    @PostMapping(path = "/take/{choreId}/{userId}")
    public void takeUpChore(@PathVariable("choreId") int choreId, @PathVariable("userId") UUID userId){
        choreManager.takeUpChore(choreId,userId);
    }

    @PostMapping(path = "/markDone/{choreId}/{userId}")
    public void markAsDone(@PathVariable("choreId") int choreId,@PathVariable("userId") UUID userId){
        choreManager.markAsDone(choreId,userId);
    }

    @GetMapping(path = "/listOfNotDone/{userId}")
    public List<Chore> getListOfNotDoneChores(@PathVariable("userId") UUID userId){
        return choreManager.getListOfNotDoneChores(userId);
    }

    @GetMapping(path = "/tookUpChores/{userId}")
    public List<Chore> getTookUpChores(@PathVariable("userId") UUID userId){
        return choreManager.getTookUpChores(userId);
    }

    @PostMapping(path = "/changeDeadline/{choreId}/{deadline}")
    public void changeDeadline(@PathVariable("choreId")int choreId,@PathVariable("deadline") String newDeadline){
        choreManager.changeDeadline(choreId,newDeadline);
    }

    @GetMapping(path = "/getChoreTypes")
    public List<ChoreType> getChoreTypes(){
        return choreManager.getChoreTypes();
    }

    @GetMapping(path = "/getReport/{userId}")
    public List<Report> getReport(@PathVariable("userId") UUID userId){
        return choreManager.getReport(userId);
    }
}
