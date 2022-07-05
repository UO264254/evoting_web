package es.abgr.evoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.abgr.evoting.model.Candidate;
import es.abgr.evoting.model.RunnerId;
import es.abgr.evoting.repositories.CandidateRepository;

@Service
public class CandidateServiceImpl implements CandidateService {

	@Autowired
    private CandidateRepository candidateRepository;
	
	@Override
	public Candidate findRunner(RunnerId runnerid) {
		
		return candidateRepository.findRunner(runnerid);
	}

}
