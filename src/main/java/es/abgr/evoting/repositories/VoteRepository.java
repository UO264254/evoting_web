package es.abgr.evoting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.abgr.evoting.model.Vote;
import es.abgr.evoting.model.VoteId;

@Repository
public interface VoteRepository extends JpaRepository<Vote, VoteId> {
	@Query("SELECT MAX(vote.id.noncediscrim) " + "FROM Vote vote "
			+ "WHERE vote.id.procedurecode = :procedurecode AND vote.id.ballotcode = :ballotcode AND vote.id.encryptednonce = :encryptednonce")
	Integer getMaxNonceDiscrim(int procedurecode, int ballotcode, String encryptednonce);
}
