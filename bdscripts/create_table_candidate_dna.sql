CREATE TABLE candidates_dna (
	id bigserial PRIMARY KEY,
	dna_hash_code int4 UNIQUE NOT NULL,
	is_mutant bool NOT NULL
);

