package com.lagosa.HomeManager.api;

import com.lagosa.HomeManager.model.Chore;
import com.lagosa.HomeManager.model.ChoreType;
import com.lagosa.HomeManager.model.Memento;
import com.lagosa.HomeManager.model.Report;
import com.lagosa.HomeManager.service.ChoreManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("chore")
public class ChoreAPI {

    private final ChoreManager choreManager;

    @Autowired
    public ChoreAPI(ChoreManager choreManager){
        this.choreManager = choreManager;
    }

//    /**
//     * Creates a new chore and stores it in the database
//     * @param submitterId id of the user that created the chore
//     * @param deadline deadline of the chore
//     * @param type type of the chore
//     * @param description short description of the chore
//     * @param title a meaningful title to the chore
//     */
//    @PostMapping(path = "/create/{submitterId}/{deadline}/{type}/{title}")
//    public void createChore(@PathVariable("submitterId") UUID submitterId, @PathVariable("deadline") String deadline, @PathVariable("type") String type,
//                            @RequestBody Map<String,String> description, @PathVariable("title") String title){
//
//        choreManager.createChore(submitterId,deadline,type,description.get("description"),title);
//    }

    @PostMapping(
            path = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public void createChore(@RequestBody Map<String,Object> object){
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.log(Level.WARNING,"" + object.toString());
      //  choreManager.createChore(submitterId,deadline,type,description.get("description"),title);
    }

    /**
     * Deletes a chore if the submitter is the user who requested the deletion
     * @param choreId the chore that needs to be deleted
     * @param userId the user that requested the deletion
     * @throws Exception if the user is not the submitter of the chore
     */
    @PostMapping(path = "/delete/{choreId}/{userId}")
    public void deleteChore(@PathVariable("choreId") int choreId,@PathVariable("userId") UUID userId) throws Exception{
        choreManager.deleteChore(choreId,userId);
    }

    /**
     * Assigns to a chore the user that performed the request
     * @param choreId the chore to which the user needs to be assigned
     * @param userId the user that performed the request
     */
    @PostMapping(path = "/take/{choreId}/{userId}")
    public void takeUpChore(@PathVariable("choreId") int choreId, @PathVariable("userId") UUID userId){
        choreManager.takeUpChore(choreId,userId);
    }

    /**
     * Marks a chore as done, and updates the doneBy field to the user who did it
     * @param choreId the chore that needs to be updated
     * @param userId the user that did the chore
     */
    @PostMapping(path = "/markDone/{choreId}/{userId}")
    public void markAsDone(@PathVariable("choreId") int choreId,@PathVariable("userId") UUID userId){
        choreManager.markAsDone(choreId,userId);
    }

    /**
     * Gets a list of all the chores of a family that were not made
     * @param userId the user made the request, and whose family's chores needs to be returned
     * @return the chores that were not finished yet
     */
    @GetMapping(path = "/listOfNotDone/{userId}")
    public List<Chore> getListOfNotDoneChores(@PathVariable("userId") UUID userId){
        return choreManager.getListOfNotDoneChores(userId);
    }

    /**
     * Gets a list of all the chores the user took up and did not finish
     * @param userId the user who requested the list
     * @return a list of not finished chores assigned to a user
     */
    @GetMapping(path = "/tookUpChores/{userId}")
    public List<Chore> getTookUpChores(@PathVariable("userId") UUID userId){
        return choreManager.getTookUpChores(userId);
    }

    /**
     * Updates the deadline of a chore if the requester is the submitter of the chore
     * @param choreId the chore that needs to be updated
     * @param newDeadline the new deadline
     * @param userId the user that requested the update
     * @throws Exception if the user is not the submitter of the chore
     */
    @PostMapping(path = "/changeDeadline/{choreId}/{userId}/{deadline}")
    public void changeDeadline(@PathVariable("choreId")int choreId,@PathVariable("userId") UUID userId,@PathVariable("deadline") String newDeadline) throws Exception{
        choreManager.changeDeadline(choreId,userId,newDeadline);
    }

    /**
     * Gets a list of all the chore types
     * @return the list of all the chore types
     */
    @GetMapping(path = "/getChoreTypes")
    public List<ChoreType> getChoreTypes(){
        return choreManager.getChoreTypes();
    }

    /**
     * Gets a report regarding the done and not done chores for every family member of the user
     * @param userId the user for whose family the report needs to be generated
     * @return the list of reports for every family member
     */
    @GetMapping(path = "/getReport/{userId}")
    public List<Report> getReport(@PathVariable("userId") UUID userId){
        return choreManager.getReport(userId);
    }

    @PostMapping(path = "/addMemento/{user}/{title}/{dueDate}")
    public void addMemento(@PathVariable("user") UUID userId,@PathVariable("title") String title,@PathVariable("dueDate") Date dueDATE){
        choreManager.addMemento(userId, title, dueDATE);
    }

    @GetMapping(path = "/getMementos/{userId}/{startDate}/{endDate}")
    public List<Memento> getMementos(@PathVariable("userId") UUID userId,@PathVariable("startDate") Date startDate,@PathVariable("endDate") Date endDate){
        return choreManager.getMementos(userId, startDate, endDate);
    }

}
