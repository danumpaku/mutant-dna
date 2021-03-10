package org.brotherhood.mutantdna.services.impl;

import java.util.Arrays;

import org.brotherhood.mutantdna.entities.CandidateDna;
import org.brotherhood.mutantdna.helpers.DnaHelper;
import org.brotherhood.mutantdna.repositories.CandidateDnaRepository;
import org.brotherhood.mutantdna.services.MutantDnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class MutantDnaServiceImpl implements MutantDnaService {

	@Autowired
	CandidateDnaRepository candidatesRepository;
	
	@Override
	public boolean registerMutantDna(String[] dna) {

		int hashCode=Arrays.hashCode(dna);
		boolean isMutant = DnaHelper.isMutant(dna);
		
		CandidateDna candidate = new CandidateDna();
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
