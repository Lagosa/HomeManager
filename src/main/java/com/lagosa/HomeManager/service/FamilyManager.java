package com.lagosa.HomeManager.service;

import com.lagosa.HomeManager.dao.FamilyDao;
import com.lagosa.HomeManager.exceptions.ApiRequestException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

@Service
public class FamilyManager {

    private final FamilyDao familyDao;

    public FamilyManager(FamilyDao familyDao){
        this.familyDao = familyDao;
    }

    public UUID registerFamily(String emailAddress, String password){
        if(!validateEmail(emailAddress)) {
            throw new ApiRequestException("Email address not valid!");
        }
        String encryptedPassword = encryptPassword(password);
        UUID id = UUID.randomUUID();
        familyDao.registerFamily(id,emailAddress,encryptedPassword);
        return id;
    }

    private boolean validateEmail(String email)
    {
        return email.contains("@") && email.contains(".") && email.length() >= 6;
    }

    private String encryptPassword(String password){
        String stringToEncrypt = "!Home#Manager![" + password + "]$%^";
        return  DigestUtils.md5DigestAsHex(stringToEncrypt.getBytes(StandardCharsets.UTF_8));
    }

}
