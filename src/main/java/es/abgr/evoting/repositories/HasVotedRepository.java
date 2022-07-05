package es.abgr.evoting.repositories;

import org.springframework.data.repository.CrudRepository;

import es.abgr.evoting.model.HasVoted;
import es.abgr.evoting.model.HasVotedId;

public interface HasVotedRepository extends CrudRepository<HasVoted, HasVotedId>  {

	

}
