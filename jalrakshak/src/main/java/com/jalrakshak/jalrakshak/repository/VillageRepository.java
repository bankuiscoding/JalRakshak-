package com.jalrakshak.jalrakshak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jalrakshak.jalrakshak.model.Village;

public interface VillageRepository extends JpaRepository<Village, Long> {
	List<Village> findByDistrict(String district);

	List<Village> findByState(String state);

	boolean existsByVillageNameAndDistrict(String villageName, String district);

}
