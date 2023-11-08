package com.pluton.yelody.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pluton.yelody.models.User;
import com.pluton.yelody.models.UserPreferences;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreferences, UUID>{
	List<UserPreferences> getByUser(User user);
}