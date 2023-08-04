package com.wrathyboo.client.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wrathyboo.client.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);
    
	Optional<User> findByUsername(String username);
}
