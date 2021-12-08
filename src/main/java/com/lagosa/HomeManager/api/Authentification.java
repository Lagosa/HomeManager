package com.lagosa.HomeManager.api;

import com.lagosa.HomeManager.exceptions.ApiRequestException;
import com.lagosa.HomeManager.model.Roles;
import com.lagosa.HomeManager.model.User;
import com.lagosa.HomeManager.service.FamilyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * <H3> Is the API for requests which are related to authentication</H3>
 * It has methods for registering a family, joining a family, displaying information about
 * the family and about the user. It can be accessed trough the link: <br>
 * localhost:8080/authentication
 */

@RestController
@RequestMapping("authentication")
public class Authentification {

    private final FamilyManager familyManager;
    private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Autowired
    public Authentification(FamilyManager familyManager){
        this.familyManager = familyManager;
    }

    /**
     * <h3>Saves a new family in the database.</h3>
     * <br>
     * A sample link trough which it can be called:
     * <br>
     * localhost:8080/authentication/registerFamily/sample@email.com/samplePassword
     * @param emailAddress the email address representing the family (address of one of the family members)
     * @param password the password used to log into the family account.
     * @return the id of the family, used further to identify it
     * @throws ApiRequestException if the email does not have an email form (must contain @ and . (dot)) or if the password
     * is too short and does not contain any digit and uppercase and lowercase letters
     * @throws Exception if there was a problem with inserting the data into the database
     */
    @GetMapping(path = "/registerFamily/{email}/{password}")
    public Map<String,String> registerFamily(@PathVariable("email") String emailAddress, @PathVariable("password") String password) throws ApiRequestException,Exception
    {
        try {
            Map<String,String> uuidMap = new HashMap<>();
            uuidMap.put("UUID",familyManager.registerFamily(emailAddress, password).toString());
            return uuidMap;
        }catch (ApiRequestException e){
            throw e;
        }catch (SQLException e) {
            throw new Exception("Failed to register: database error!");
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * <h3>Logs in a family based on email and password</h3>
     * @param email email used to identify the account
     * @param password password used to check identity
     * @return the id of the family if login was successful, else returns null
     * @throws Exception if the data from the database could not be retrieved
     */
    @GetMapping(path = "/login/{email}/{password}")
    public UUID loginFamily(@PathVariable("email") String email,@PathVariable("password") String password)throws Exception{
        try{
            return familyManager.loginFamily(email, password);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * <h3>Gets the code used to join a family from the database</h3>
     * @param id the id of the family the code needs to be got
     * @return the joining code
     * @throws Exception if there was a problem communicating with the database
     */
    @GetMapping(path = "/registerFamily/{id}")
    public int getJoinCode(@PathVariable("id") UUID id){
        try{
            return familyManager.getJoinCode(id);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * <h3>Saves a new user in the database.</h3>
     * <br>
     * It connects the user with the family it belongs to
     * @param joinCode the join code of the family which the user wants to join
     * @param nickname the nickname of the user
     * @param role the role of the user it must be either CHILD or PARENT
     * @param birthdate the birthdate of the user, format: yyyy-mm-dd
     * @return the id of the registered user
     * @throws Exception if the user couldn't be inserted into the database
     */
    @GetMapping(path = "/joinFamily/{joinCode}/{nickname}/{role}/{birthdate}")
    public UUID joinFamily(@PathVariable("joinCode") int joinCode, @PathVariable("nickname") String nickname,
                           @PathVariable("role") Roles role, @PathVariable("birthdate") String birthdate) throws Exception {
        try {
            return familyManager.joinFamily(joinCode, nickname, role, birthdate);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * <h3>Gets information about the user with the given id</h3>
     * @param id id of the user that needs to be returned
     * @return the information about the user with given id
     */
    @GetMapping(path = "/getUserData/{id}")
    public User getUser(@PathVariable("id") UUID id){
        try{
            return familyManager.getUser(id);
        }catch (Exception e){
            throw e;
        }
    }

}
