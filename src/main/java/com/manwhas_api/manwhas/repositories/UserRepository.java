package com.manwhas_api.manwhas.repositories;

import com.manwhas_api.manwhas.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository 
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    
    //Para saber si ya existen en la base de datos
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    
}
