package com.jalrakshak.jalrakshak.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jalrakshak.jalrakshak.model.Village;
import com.jalrakshak.jalrakshak.service.VillageService;

@RestController
@RequestMapping("/api/villages")
@CrossOrigin("*")
public class VillageController {

	private final VillageService villageService;

	public VillageController(VillageService villageService) {
		this.villageService = villageService;
	}

	@PostMapping
	public Village createVillage(@RequestBody Village village) {
		return villageService.createVillage(village);
	}

	@GetMapping
	public List<Village> getAllVillages() {
		return villageService.getAllVillages();
	}

	@GetMapping("/{id}")
	public Village getVillageById(@PathVariable Long id) {
		return villageService.getVillageById(id);
	}

	@GetMapping("/district/{district}")
	public List<Village> getVillageByDistrict(@PathVariable String district) {
		return villageService.getVillagesByDistrict(district);
	}

	@GetMapping("/state/{state}")
	public List<Village> getVillageByState(@PathVariable String state) {
		return villageService.getVillagesByState(state);
	}

	@PutMapping("/{id}")
	public Village updayeVillage(@PathVariable Long id, @RequestBody Village village) {
		return villageService.updateVillage(id, village);
	}

	@DeleteMapping("/{id}")
	public String deleteVillage(@PathVariable Long id) {
		return villageService.deleteVillage(id);
	}

}
