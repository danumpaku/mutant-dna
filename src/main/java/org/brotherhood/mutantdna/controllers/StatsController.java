package org.brotherhood.mutantdna.controllers;

import org.brotherhood.mutantdna.entities.Stats;
import org.brotherhood.mutantdna.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {

	@Autowired
	private StatsService service;
	
	@GetMapping("/stats")
	public Stats getStats () {
		return service.getStats();
	}
	
}
