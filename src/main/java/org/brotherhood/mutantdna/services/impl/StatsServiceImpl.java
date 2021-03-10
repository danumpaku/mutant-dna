package org.brotherhood.mutantdna.services.impl;

import org.brotherhood.mutantdna.entities.Stats;
import org.brotherhood.mutantdna.repositories.CandidateDnaRepository;
import org.brotherhood.mutantdna.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatsServiceImpl implements StatsService {

	@Autowired
	private CandidateDnaRepository candidatesRepository;
	
	@Override
	public Stats getStats() {   
		return candidatesRepository.getStats();
	}

}
