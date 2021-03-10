package org.brotherhood.mutantdna.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Setter;

@Entity
@Table(name = "candidates_dna")
public class CandidateDna {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;
	
	@Setter
    @Column(name="dna_hash_code", unique = true)
	private int dnaHashCode;
	
    @Setter
	@Column(name="is_mutant")
	private boolean isMutant;
}
