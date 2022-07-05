package es.abgr.evoting.service;

import java.util.List;

import es.abgr.evoting.model.VoterBallotList;

public interface VoterBallotListService {
	List<VoterBallotList> findByVoterid(String voterid);
}
