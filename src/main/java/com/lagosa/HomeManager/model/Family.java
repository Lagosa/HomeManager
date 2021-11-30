package com.lagosa.HomeManager.model;

import javax.management.relation.Role;
import java.util.UUID;

public class Family {
    private final UUID id;
    private final String email;
    private final String password;
    private final int joinCode;

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getJoinCode() {
        return joinCode;
    }

    public Family(UUID id, String email, String password, int joinCode) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.joinCode = joinCode;
    }
}
