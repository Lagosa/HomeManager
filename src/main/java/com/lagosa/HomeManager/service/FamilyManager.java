package com.lagosa.HomeManager.service;

import com.lagosa.HomeManager.dao.FamilyDao;
import com.lagosa.HomeManager.exceptions.ApiRequestException;
import com.lagosa.HomeManager.model.Family;
import com.lagosa.HomeManager.model.Roles;
import com.lagosa.HomeManager.model.User;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class FamilyManager {

    private final FamilyDao familyDao;
    private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public FamilyManager(FamilyDao familyDao){
        this.familyDao = familyDao;
    }

    /**
     * Checks the data given about a family. And if there were no problems creates a new family instance
     *  and stores this family in the database.
     * @param emailAddress email address representing the family
     * @param password password used to log into the account
     * @return the id of the newly created family
     * @throws Exception if there was invalid data or the data could not be inserted into the database
     */
    public UUID registerFamily(String emailAddress, String password) throws Exception{
        // validate data given
        if(!validateEmail(emailAddress)) {
            throw new ApiRequestException("Email address not valid!");
        }
        if(!validatePassword(password)){
            throw new ApiRequestException("Password is too weak!");
        }

        String encryptedPassword = encryptPassword(password); // encrypt the password
        UUID id = UUID.randomUUID(); // generate an id for this family
        int joinCode = generateJoinCode(); // generate a join code for this family

        Family newFamily = new Family(id,emailAddress,encryptedPassword,joinCode); // create a new family instance
        familyDao.registerFamily(newFamily); // insert the family into the database
        return id; // return the id of the newly inserted family
    }

    /**
     * Check if the login information sent correspond to the informations in the databse
     * @param email the email used to identify the family
     * @param password the password used to log in into the account
     * @return the id of the family if the information corresponds, else null
     */
    public UUID loginFamily(String email, String password){
        Family family = familyDao.getFamily(email); // gets the family based on its email from the database
        if(family == null){ // if there was no match for that email address return false
            return null;
        }

        String encryptedPassword = encryptPassword(password); // encrypts the given password to match the stored one
        if(family.getPassword().equals(encryptedPassword)){
            return family.getId();
        }

        return null;
    }

    /**
     * Gets the join code of a family
     * @param id id of the family
     * @return join code of the family
     */
    public int getJoinCode(UUID id){
        try{
            return familyDao.getFamily(id).getJoinCode();
        }catch (NullPointerException e){
            throw new NullPointerException("Family could not be found based on that id");
        }
    }

    /**
     * Registers a new user. Creates an id for it and joins it with the family whose join code was given as parameter.
     * @param joinCode the join code of the family the user belongs to
     * @param nickname the nickname of the user
     * @param role the role of the user
     * @param birthdate the birthdate of the user
     * @return the id of the newly created user
     */
    public UUID joinFamily(int joinCode, String nickname, Roles role, String birthdate){
        try{
            UUID id = UUID.randomUUID(); // generates an id

            UUID familyId = familyDao.getFamily(joinCode).getId(); // gets the family id based on join code

            User newUser = new User(id,familyId,nickname,role,Date.valueOf(birthdate)); // creates the user with the data given

            familyDao.joinFamily(newUser); // inserts the user into the database

            return id; // returns the id of the newly created user
        }catch (NullPointerException e){
            throw new NullPointerException("Family could not be found based on that join code!");
        }
    }

    /**
     * Gets all the data about a user
     * @param id the id of the user sought
     * @return the object of the user sought
     */
    public User getUser(UUID id){
        return familyDao.getUser(id);
    }

    /**
     * Gets the family members of the a family
     * @param familyId the id of the family whose members are sought
     * @return a list of users representing family members
     */
    public List<User> getFamilyMembers(UUID familyId){
        return familyDao.getFamilyMembers(familyId);
    }

    /**
     * Validates an email
     * @param email the email that needs to be validated
     * @return true if the email contains @ and . (dot)
     */
    private boolean validateEmail(String email)
    {
        return email.contains("@") && email.contains(".") && email.length() >= 6;
    }

    /**
     * Validates a password
     * @param password the password that needs to be validated
     * @return true if the password has a length greater than 8 characters and if it contains upper and lowercase
     * letters and digits
     */
    private boolean validatePassword(String password) {
        // checks the length of the password first
        if(password.length() < 8){
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigits = false;
        //ToDo: check if the password has special characters too
        char character;
        // checks if there are lower and uppercase letters and digits in the password
        for(int i=0;i<password.length();i++){
            character = password.charAt(i);
            if(Character.isDigit(character))
            {
                hasDigits = true;
            }
            if(Character.isUpperCase(character)){
                hasUppercase = true;
            }
            if(Character.isLowerCase(character)){
                hasLowercase = true;
            }
        }
        if(hasDigits && hasLowercase && hasUppercase){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Encrypts a password using md5
     * @param password the password that needs to be encrypted
     * @return the encrypted password
     */
    private String encryptPassword(String password){
        String stringToEncrypt = "!Home#Manager![" + password + "]$%^"; // add a secret format to the password to ensure
                                                                        // it is secured
        return  DigestUtils.md5DigestAsHex(stringToEncrypt.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a join code that was not yet generated
     * @return the join code generated
     * @throws Exception if there was no unique code found
     */
    private int generateJoinCode() throws Exception{
        List<Integer> allJoinCodes = familyDao.getAllJoinCodes(); // get the list of all the codes generated so far
        int count = 0;
        int newCode;
        while(count < 2147483647) // try to generate a code INT_MAX times
        {
            newCode = Math.round((int)(Math.random() * 899999) + 1000000); // generate a 6 digit code
            if(!allJoinCodes.contains(newCode)){ // if the code is not in the already generated code's list return it
                return newCode;
            }
            count++;
        }
        throw new Exception("There is no available code");
    }

}
