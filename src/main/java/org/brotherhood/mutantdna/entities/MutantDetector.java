package org.brotherhood.mutantdna.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class MutantDetector {

	private final List<MutantDnaSequence> sequences;
	
	@Getter
	private boolean mutantDetected;
	
	public MutantDetector() {
		sequences = new ArrayList<MutantDnaSequence>();
		mutantDetected = false;
	}
	
	public void addDnaSequence(MutantDnaSequence sequence) {
		sequences.add(sequence);
		analizeSequences();
	}
	
	private void analizeSequences() {
		for (int i=0; i<sequences.size() && !mutantDetected; i++)
			for (int j=i+1; j<sequences.size(); j++)
				if (! sequences.get(i).overlapsWith(sequences.get(j)))
					mutantDetected = true;

	}
}
