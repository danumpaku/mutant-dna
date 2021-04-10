package org.brotherhood.mutantdna.services.impl;

import java.util.Arrays;

import org.brotherhood.mutantdna.dnaanalysis.DnaAnalizer;
import org.brotherhood.mutantdna.dtos.CandidateDnaDto;
import org.brotherhood.mutantdna.repositories.CandidateDnaRepository;
import org.brotherhood.mutantdna.services.MutantDnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class MutantDnaServiceImpl implements MutantDnaService {

	private static final int MUTANT_SEQUENCE_LENGTH = 4;
	
	@Autowired
	CandidateDnaRepository candidatesRepository;
	
	@Override
	public boolean registerMutantDna(String[] dna) {

		int hashCode=Arrays.hashCode(dna);
		boolean isMutant = new DnaAnalizer(MUTANT_SEQUENCE_LENGTH).isMutant(dna);
		
		CandidateDnaDto candidate = new CandidateDnaDto();
		candidate.setDnaHashCode(hashCode);
		candidate.setMutant(isMutant);
		
		try {
			candidatesRepository.save(candidate);
		}
		catch (DataIntegrityViolationException e) {
			//Ignore errors caused by duplicated DNA
			System.out.println("Duplicated DNA: Ignored");
		}
		
		return isMutant;
	}
}
