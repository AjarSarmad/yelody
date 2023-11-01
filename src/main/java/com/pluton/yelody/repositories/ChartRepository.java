package com.pluton.yelody.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pluton.yelody.models.Chart;

@Repository
public interface ChartRepository extends JpaRepository<Chart , UUID>{
	Optional<Chart> findChartByName(String name);
	Long countBy();
}
