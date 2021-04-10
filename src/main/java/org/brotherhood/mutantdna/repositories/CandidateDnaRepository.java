package org.brotherhood.mutantdna.repositories;

import org.brotherhood.mutantdna.dtos.CandidateDnaDto;
import org.brotherhood.mutantdna.dtos.StatsDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateDnaRepository extends CrudRepository<CandidateDnaDto, Long>{
	
	@Query("select new org.brotherhood.mutantdna.entities.Stats ("
			+ "count(case when cd.isMutant = true then 1 end), "
			+ "count(case when cd.isMutant = false then 1 end)"
			+ ") "
			+ "from CandidateDna cd")
	public StatsDto getStats(); 
	
}
