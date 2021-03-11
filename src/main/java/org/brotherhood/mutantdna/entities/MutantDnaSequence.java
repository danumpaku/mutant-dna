package org.brotherhood.mutantdna.entities;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class MutantDnaSequence {

	@Getter
	private char dnaBase;
	
	@Getter
	private List<Position> dnaPositions;
	
	public boolean collidesWith(MutantDnaSequence other) {
		if (dnaBase != other.dnaBase)
			return false;
		for (Position position : dnaPositions)
			if (other.getDnaPositions().contains(position)) {
				System.out.println("Collided!!" + "(" + position.getCol() + "," + position.getRow() + ")");
				return true;
			}
		return false;
	}
}
