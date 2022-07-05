package es.abgr.evoting.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import es.abgr.evoting.model.Ballot;
import es.abgr.evoting.model.BallotId;
import es.abgr.evoting.model.Procedure;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
class BallotRepositoryIntegrationTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	BallotRepository ballotRepository;
	
	@Autowired
	ProcedureRepository procedureRepository;
	
	
	@BeforeEach
	public void setup() {
		Procedure procedure = new Procedure();
		procedure.setCode(1);
		procedure.setStarts(DateUtils.addDays(new Date(), -1));
		procedure.setEnds(DateUtils.addDays(new Date(), 1));
		procedure.setWeb(1);
		entityManager.persistAndFlush(procedure);
		
		BallotId ballotId = new BallotId();
		ballotId.setCode(1);
		ballotId.setProcedure(procedureRepository.findByCode(1));
		
		Ballot ballot = new Ballot();
		ballot.setId(ballotId);
		ballot.setMaxpreferences(2);
		ballot.setName("test");
		entityManager.persistAndFlush(ballot);
	}
	
	@Test
	public void whenFindById_thenReturnBallot() {
		BallotId ballotId = new BallotId();
		ballotId.setCode(1);
		ballotId.setProcedure(procedureRepository.findByCode(1));
		assertEquals(ballotRepository.findById(ballotId).get().getName(),"test");
	}
	
	@Test
	public void whenFindAll() {
		
		assertTrue(ballotRepository.findAll().size()==1);
	}
	
	@Test
	public void whenFindInvalidId_thenReturnEmpty() {
		BallotId ballotId = new BallotId();
		ballotId.setCode(3);
		ballotId.setProcedure(procedureRepository.findByCode(1));
		assertTrue(ballotRepository.findById(ballotId).isEmpty());
	}

}
