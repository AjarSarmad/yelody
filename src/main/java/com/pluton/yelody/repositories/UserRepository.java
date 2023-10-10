package com.pluton.yelody.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pluton.yelody.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>{
	Optional<User> findByUserName (String userName);
}
