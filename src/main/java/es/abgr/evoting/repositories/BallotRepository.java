package es.abgr.evoting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.abgr.evoting.model.Ballot;
import es.abgr.evoting.model.BallotId;

public interface BallotRepository extends JpaRepository<Ballot, BallotId> {

	Ballot findBallotById(BallotId id);

}
