package com.lagosa.HomeManager.api;

import com.lagosa.HomeManager.model.Notification;
import com.lagosa.HomeManager.model.User;
import com.lagosa.HomeManager.service.FamilyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("family")
public class FamilyAPI {

    private final FamilyManager familyManager;

    @Autowired
    public FamilyAPI(FamilyManager familyManager) {
        this.familyManager = familyManager;
    }

    @PostMapping(path = "/sendNotification/{sender}/{receiver}/{title}/{message}")
    public String sendNotification(@PathVariable("sender") UUID sender, @PathVariable("receiver") UUID receiver, @PathVariable("title") String title, @PathVariable("message") String message) {
        familyManager.sendNotification(sender, receiver, title, message);
        return "ok";
    }

    @GetMapping(path = "/getNotifications/{userId}")
    public List<Notification> getNotifications(@PathVariable("userId") UUID userid) {
        return familyManager.getNotifications(userid);
    }

    @PostMapping(path = "/setArrived/{user}")
    public void setArrivalArrived(@PathVariable("user") UUID userId) {
        familyManager.setArrivalArrived(userId);
    }

    @PostMapping(path = "/setLeft/{user}")
    public void setArrivalLeft(@PathVariable("user") UUID userId) {
        familyManager.setArrivalLeft(userId);
    }

    @GetMapping(path = "/getWhoIsHome/{user}")
    public List<String> getWhoIsHome(@PathVariable("user") UUID userId){
        return familyManager.getWhoIsHome(userId);
    }

    @GetMapping(path = "/getArrivalList/{user}/{start}/{end}")
    public List<Map<String,Object>> getArrivalList(@PathVariable("user") UUID userId,@PathVariable("start") String startDate,@PathVariable("end") String endDate){
        return familyManager.getArrivalList(userId, Date.valueOf(startDate),Date.valueOf(endDate));
    }

    @GetMapping(path = "/getFamilyMembers/{userId}")
    public List<User> getFamilyMembers(@PathVariable("userId") UUID userId){
        return familyManager.getFamilyMembersByUser(userId);
    }

}