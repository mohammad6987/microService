package com.example.authservice.Repository;

import com.example.authservice.Model.EndUser;
import com.example.authservice.Model.TokenPack;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface TokenRepository extends CrudRepository<TokenPack, String> {
    TokenPack getTokenPackByName(String name);
    Collection<TokenPack> findByOwnerUsername(String username);
    void deleteByName(String name);
    void deleteByTokenValue(String tokenValue);
}
