package org.brotherhood.mutantdna.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class DnaRequest {

	@Getter	
	@JsonProperty("dna")
	private String[] dna;
	
	@Override
	public String toString() {
		
		return "dna={\n" + String.join("\n", dna) + "\n}";
	}
}
