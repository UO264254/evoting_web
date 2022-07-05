package es.abgr.evoting.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.abgr.evoting.model.Voter;

@Repository
public interface VoterRepository extends CrudRepository<Voter, String> {
    
    Voter findByUsername(String name);

	Voter findByEmail(String email);
    
}
