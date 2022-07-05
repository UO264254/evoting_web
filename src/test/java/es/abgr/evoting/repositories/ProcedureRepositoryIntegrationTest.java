package es.abgr.evoting.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import es.abgr.evoting.model.Procedure;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
class ProcedureRepositoryIntegrationTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private ProcedureRepository procedureRepository;
	
	@Test
	public void whenFindAllProcedures_thenReturnList() {
		Procedure procedure = new Procedure();
		procedure.setCode(1);
		procedure.setStarts(DateUtils.addDays(new Date(), -1));
		procedure.setEnds(DateUtils.addDays(new Date(), 1));
		procedure.setWeb(1);
		entityManager.persistAndFlush(procedure);
		
		
		assertTrue(1==procedureRepository.count());
		List<Procedure> procedures = procedureRepository.findAll();
		assertTrue(1==procedures.size());
		assertTrue(procedures.get(0).getStarts().before(new  Date()));
		assertTrue(procedures.get(0).getEnds().after(new  Date()));
		assertTrue(procedures.get(0).getWeb()==1);
		
		assertTrue(1==procedureRepository.findAllWebProcedures().size());
		
		assertTrue(1==procedureRepository.findAllActiveWebProcedures(new Date()).size());
		
	}
	
	@Test
	public void whenFindAllActiveWebProceduresInvalidDate_thenReturnEmpty() {
		assertThat(procedureRepository.findAllActiveWebProcedures(new Date())).isEmpty();
		
	}
	
	@Test
	public void whenFindByInvalidCode_thenReturnEmpty() {
		assertThat(procedureRepository.findById(999)).isEmpty();
		
	}

}
