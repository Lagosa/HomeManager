package com.lagosa.HomeManager.api;

import com.lagosa.HomeManager.exceptions.ApiRequestException;
import com.lagosa.HomeManager.service.FamilyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("authentication")
public class Authentification {

    private final FamilyManager familyManager;

    @Autowired
    public Authentification(FamilyManager familyManager){
        this.familyManager = familyManager;
    }

    @GetMapping(path = "/registerFamily/{email}/{password}")
    public UUID registerFamily(@PathVariable("email") String emailAddress, @PathVariable("password") String password) throws Exception
    {
        try {
            return familyManager.registerFamily(emailAddress, password);
        }catch (ApiRequestException e){
            throw e;
        }catch (Exception e) {
            throw new Exception("Failed to register: database error!");
        }

    }
}
