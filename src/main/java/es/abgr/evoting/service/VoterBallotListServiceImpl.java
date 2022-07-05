package es.abgr.evoting.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.abgr.evoting.model.VoterBallotList;
import es.abgr.evoting.repositories.VoterBallotListRepository;

@Service
public class VoterBallotListServiceImpl implements VoterBallotListService {

	@Autowired
	VoterBallotListRepository voterBallotListRepository;

	@Override
	public List<VoterBallotList> findByVoterid(String voterid) {
		return voterBallotListRepository.findByVoterid(voterid);
	}
	
	

}
