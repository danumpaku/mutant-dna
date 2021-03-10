package org.brotherhood.mutantdna.repositories;

import org.brotherhood.mutantdna.entities.CandidateDna;
import org.brotherhood.mutantdna.entities.Stats;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateDnaRepository extends CrudRepository<CandidateDna, Long>{
	
	@Query("select new org.brotherhood.mutantdna.entities.Stats ("
			+ "count(case when cd.isMutant = true then 1 end), "
			+ "count(case when cd.isMutant = false then 1 end)"
			+ ") "
			+ "from CandidateDna cd")
	public Stats getStats(); 
	
}
