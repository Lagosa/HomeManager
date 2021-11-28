package com.lagosa.HomeManager.dao;

import java.util.UUID;

public interface FamilyDao {
    void registerFamily(UUID id, String emailAddress, String password);
}
