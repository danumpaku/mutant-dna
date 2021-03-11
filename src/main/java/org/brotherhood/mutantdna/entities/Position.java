package org.brotherhood.mutantdna.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Position {
	
	@Getter
	private int row;
	
	@Getter
	private int col;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
}
