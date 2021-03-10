package org.brotherhood.mutantdna.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stats {

	@Getter
	@Setter
	@JsonProperty("count_mutant_dna")
	private long countMutantDna;
	
	@Getter
	@Setter
	@JsonProperty("count_human_dna")
	private long countHumanDna;

	@JsonProperty("ratio")
	public double getRatio() {
		return countMutantDna/countHumanDna;
	}

	@Override
	public String toString() {
		return "Stats [countMutantDna=" + countMutantDna + ", countHumanDna=" + countHumanDna + ", ratio=" + getRatio() + "]";
	}
	
	
}
