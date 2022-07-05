package es.abgr.evoting.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import es.abgr.evoting.model.Ballot;
import es.abgr.evoting.model.Voter;
import es.abgr.evoting.model.VoterBallotList;
import es.abgr.evoting.model.VoterBallotListId;

@Repository
public class VoterBallotListRepositoryImpl implements VoterBallotListRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public void add(Voter voter) {
		for (Ballot ballot : voter.getProcedure().getBallots()) {
			VoterBallotList vvl = new VoterBallotList();
			VoterBallotListId vvli = new VoterBallotListId();
			vvli.setVoterid(voter.getUsername());
			vvli.setProcedurecode(voter.getProcedure().getCode());
			vvli.setBallotcode(ballot.getId().getCode());
			vvl.setId(vvli);
			entityManager.merge(vvl);
		}
	
	}

	@Override
	public List<VoterBallotList> findByVoterid(String voterid) {
		return entityManager
				.createQuery("SELECT vbl FROM VoterBallotList vbl WHERE vbl.id.voterid=:voterid ORDER BY ballotcode")
				.setParameter("voterid", voterid)
				.getResultList();
	}


	
}
