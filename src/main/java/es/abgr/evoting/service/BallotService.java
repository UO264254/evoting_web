package es.abgr.evoting.service;

import es.abgr.evoting.model.Ballot;
import es.abgr.evoting.model.BallotId;

public interface BallotService {
	
	Ballot findById(BallotId id);
}
