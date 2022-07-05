package es.abgr.evoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.abgr.evoting.model.Ballot;
import es.abgr.evoting.model.BallotId;
import es.abgr.evoting.repositories.BallotRepository;

@Service
public class BallotServiceImpl implements BallotService {
	
	@Autowired
	BallotRepository ballotRepository;

	@Override
	public Ballot findById(BallotId id) {
		return ballotRepository.findBallotById(id);
	}

}
