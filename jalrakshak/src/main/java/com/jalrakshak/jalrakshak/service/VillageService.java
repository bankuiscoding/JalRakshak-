package com.jalrakshak.jalrakshak.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jalrakshak.jalrakshak.model.Village;
import com.jalrakshak.jalrakshak.repository.VillageRepository;

@Service
public class VillageService {

	private final VillageRepository villageRepository;

	public VillageService(VillageRepository villageRepository) {
		this.villageRepository = villageRepository;
	}

	public Village createVillage(Village village) {

		boolean alreadyExists = villageRepository.existsByVillageNameAndDistrict(village.getVillageName(),
				village.getDistrict());

		if (alreadyExists) {
			throw new RuntimeException("Village already exists in this district");
		}

		return villageRepository.save(village);
	}

	public List<Village> getAllVillages() {
		return villageRepository.findAll();
	}

	public Village getVillageById(Long id) {
		return villageRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Village not found with id: " + id));
	}

	public List<Village> getVillagesByDistrict(String district) {
		return villageRepository.findByDistrict(district);
	}

	public List<Village> getVillagesByState(String state) {
		return villageRepository.findByState(state);
	}

	public Village updateVillage(Long id, Village updatedVillage) {

		Village existingVillage = getVillageById(id);

		existingVillage.setVillageName(updatedVillage.getVillageName());
		existingVillage.setDistrict(updatedVillage.getDistrict());
		existingVillage.setState(updatedVillage.getState());
		existingVillage.setBlockName(updatedVillage.getBlockName());
		existingVillage.setPincode(updatedVillage.getPincode());
		existingVillage.setPopulation(updatedVillage.getPopulation());

		return villageRepository.save(existingVillage);
	}

	public String deleteVillage(Long id) {

		Village village = getVillageById(id);

		villageRepository.delete(village);

		return "Village deleted successfully";
	}
}