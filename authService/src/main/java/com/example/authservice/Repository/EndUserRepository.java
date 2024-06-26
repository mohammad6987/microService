package com.example.authservice.Repository;

import com.example.authservice.Model.EndUser;
import com.example.authservice.Model.EndUserDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Optional;


@Repository
public interface EndUserRepository extends CrudRepository<EndUser , Long>{
    Optional<EndUser> getEndUserById(Long id);
    Optional<EndUser> getEndUserByUsername(String username);
    ArrayList<EndUser> getAllByIdNotNull();
    void removeEndUserByUsername(String username);

}
