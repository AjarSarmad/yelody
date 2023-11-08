package com.pluton.yelody.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pluton.yelody.models.Chart;
import com.pluton.yelody.models.Keyword;
import com.pluton.yelody.models.Song;

@Repository
public interface SongRepository extends JpaRepository<Song, UUID>, JpaSpecificationExecutor<Song>{
	Optional<Song> findByName (String name);
	
	List<Song> findByKeywordlist(Keyword keyword);
	Long countBy();

	List<Song> findByChart(Chart chart);

}
