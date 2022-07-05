package es.abgr.evoting.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import es.abgr.evoting.model.Candidate;
import es.abgr.evoting.model.RunnerId;

@Repository
public class CandidateRepositoryImpl implements CandidateRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Candidate findRunner(RunnerId runnerid) {
		return (Candidate) entityManager
				.createQuery("SELECT c FROM Candidate c WHERE c.id.procedurecode=:procedurecode and c.id.id=:candidateid")
				.setParameter("procedurecode", runnerid.getProcedureCode())
				.setParameter("candidateid", runnerid.getCandidateid())
				.getSingleResult();
	}

}
