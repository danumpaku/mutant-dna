package org.brotherhood.mutantdna.controllers;

import org.brotherhood.mutantdna.entities.DnaRequest;
import org.brotherhood.mutantdna.services.MutantDnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MutantController {

	@Autowired
	private MutantDnaService service;
		
	@PostMapping("/mutant")
    public ResponseEntity<?> registerMutantDna(@RequestBody DnaRequest dnaRequest) {
		boolean isMutant;
		try{
			isMutant = service.registerMutantDna(dnaRequest.getDna()); 
		}
		catch(IndexOutOfBoundsException e) {
			return ResponseEntity.badRequest().build();
		}
        if (isMutant)
        	return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
