package com.pluton.yelody.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pluton.yelody.models.Song;
import com.pluton.yelody.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User>{
	Optional<User> findByUserName (String userName);
	List<User> findBySongViews(Song song);
	Long countBy();
	Optional<User> findByEmail(String email);
}
