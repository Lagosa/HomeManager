package com.lagosa.HomeManager.model;

import java.sql.Date;
import java.util.UUID;

public class User {
    private final UUID id;
    private String nickName;
    private final UUID familyId;
    private final Roles role;
    private final Date birthDate;

    public User(UUID id, UUID familyId,String nickName,  Roles role, Date birthDate) {
        this.id = id;
        this.nickName = nickName;
        this.familyId = familyId;
        this.role = role;
        this.birthDate = birthDate;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public UUID getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

    public UUID getFamilyId() {
        return familyId;
    }

    public Roles getRole() {
        return role;
    }

    public String getRoleString(){
        return role.toString();
    }

    public Date getBirthDate() {
        return birthDate;
    }
}
