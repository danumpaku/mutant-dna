package org.brotherhood.mutantdna.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class StatsDto {

	@Getter
	@Setter
	private long countMutantDna;
	
	@Getter
	@Setter
	private long countHumanDna;
}
