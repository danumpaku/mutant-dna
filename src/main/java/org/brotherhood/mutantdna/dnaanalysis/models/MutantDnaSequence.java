package org.brotherhood.mutantdna.dnaanalysis.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class MutantDnaSequence {

	@Getter
	private char dnaBase;
	
	@Getter
	private List<Position> dnaPositions;
	
	public boolean overlapsWith(MutantDnaSequence other) {
		if (dnaBase != other.getDnaBase())
			return false;
		for (Position position : dnaPositions)
			if (other.getDnaPositions().contains(position))
				return true;
		return false;
	}
}
