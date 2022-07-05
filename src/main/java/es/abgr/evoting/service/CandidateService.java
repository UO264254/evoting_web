package es.abgr.evoting.service;

import es.abgr.evoting.model.Candidate;
import es.abgr.evoting.model.RunnerId;

public interface CandidateService {
	Candidate findRunner(RunnerId id);
}
