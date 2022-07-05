package es.abgr.evoting.repositories;

import es.abgr.evoting.model.Candidate;
import es.abgr.evoting.model.RunnerId;

public interface CandidateRepository {
	
	Candidate findRunner(RunnerId runnerid);

}
