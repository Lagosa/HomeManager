package com.lagosa.HomeManager.api;

import com.lagosa.HomeManager.service.ChoreManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("chore")
public class ChoreAPI {

    private final ChoreManager choreManager;

    @Autowired
    public ChoreAPI(ChoreManager choreManager){
        this.choreManager = choreManager;
    }

    @PostMapping(path = "/create/{submitterId}/{deadline}/{type}")
    public void createChore(@PathVariable("submitterId") UUID submitterId, @PathVariable("deadline") String deadline, @PathVariable("type") String type,
                            @RequestBody String description){
        try{
            choreManager.createChore(submitterId,deadline,type,description);
        }catch (Exception e){
            throw e;
        }
    }
}
