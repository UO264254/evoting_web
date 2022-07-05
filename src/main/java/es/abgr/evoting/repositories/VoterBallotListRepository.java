package es.abgr.evoting.repositories;

import java.util.List;

import es.abgr.evoting.model.Voter;

public interface VoterBallotListRepository<VoterBallotList, VoterBallotListId>  {
	void add(Voter voter);
	List<VoterBallotList> findByVoterid(String voterid);
}
