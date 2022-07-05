package es.abgr.evoting.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import es.abgr.evoting.Constants;
import es.abgr.evoting.model.Ballot;
import es.abgr.evoting.model.HasVotedId;
import es.abgr.evoting.model.ReferendumOption;
import es.abgr.evoting.model.VoterBallotList;
import es.abgr.evoting.model.VoterBallotListId;
import es.abgr.evoting.repositories.HasVotedRepository;
import es.abgr.evoting.repositories.ProcedureRepository;
import es.abgr.evoting.repositories.VoterBallotListRepository;
import es.abgr.evoting.repositories.VoterRepository;
import es.abgr.evoting.service.VotacionValidationService;
import es.abgr.evoting.web.dto.VotacionDto;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@Import(ServiceImplTestContextConfiguration.class)
public class VotacionValidationServiceImplIntegrationTest extends VotacionTest{

	@Autowired
	ResourceBundleMessageSource messageSource;
	
	@Autowired
	VotacionValidationService votacionValidationService;
	
	@MockBean
	HasVotedRepository hasVotedRepository;
	
	@MockBean
	VoterRepository voterRepository;
	
	@MockBean
	ProcedureRepository procedureRepository;
	
	@MockBean
	VoterBallotListRepository<VoterBallotList, VoterBallotListId> voterBallotListRepository;
	
	@MockBean
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Before
    public void setUp() {
		initData();
		
		HasVotedId hasVoted = new HasVotedId(); 
		hasVoted.setProcedurecode(getProcedures().get(0).getCode());
		hasVoted.setVoterid(getVoters().get(0).getUsername());
    	Mockito.when(hasVotedRepository.findById(Mockito.any(HasVotedId.class))).thenAnswer(
    			invocation-> {
    				HasVotedId hasVotedId = (HasVotedId) (invocation.getArgument(0));
    				if ("alex".equals(hasVotedId.getVoterid())) {
    					return Optional.of(invocation.getArgument(0));
    				} else {
    					return Optional.empty();
    				}
    			});
    	
    	
    }
	
	@Test
	public void whenVoterInvalid_thenReturnErrors() {
		String[] errores = votacionValidationService.validar(getVoters().get(0));
		assertEquals(errores[0], messageSource.getMessage(Constants.ERROR_YA_VOTADO, new Object[] {getVoters().get(0).getUsername(),getVoters().get(0).getProcedure().getName()},Locale.getDefault()));
	}
	
	@Test
	public void whenVoterValid_thenReturnOk() {
		String[] errores = votacionValidationService.validar(getVoters().get(2));
		assertEquals(0, errores.length);
	}
	
	
	@Test 
	public void whenProcesoCerrado_thenReturnErrors() {
		String[] errores = votacionValidationService.validar(getVoters().get(1));
		assertEquals(errores[0], messageSource.getMessage(Constants.ERROR_PROCESO_CERRADO, null,Locale.getDefault()));
	}
	
	@Test 
	public void whenProcesoAbierto_thenReturnOk() {
		String[] errores = votacionValidationService.validar(getVoters().get(2));
		assertEquals(0, errores.length);
	}
	
	@Test
	public void whenCeroSelected_thenReturnOK() {
		
		String[] errores = votacionValidationService.validarMaxPreferencias(getVotacionDto());
		assertEquals(0, errores.length);
	}
	
	@Test
	public void whenSelectedLessEqualsMaxPreferences_thenReturnOK() {
		VotacionDto votacionDto = getVotacionDto();
		for(Ballot ballot : votacionDto.getBallots()) {
			ballot.setSelected(ballot.getOptions().get(0).getId().getText());
		}
		String[] errores = votacionValidationService.validarMaxPreferencias(votacionDto);
		assertEquals(0, errores.length);
	}
	
	@Test
	public void whenSelectedGreaterThanMaxPreferences_thenReturnOK() {
		VotacionDto votacionDto = getVotacionDto();
		for(Ballot ballot : votacionDto.getBallots()) {
			if (ballot.getMaxpreferences()>1) {
				for (ReferendumOption option : ballot.getOptions()) {
					option.setSelected(option.getId().getText());
				}
			}
		}
		String[] errores = votacionValidationService.validarMaxPreferencias(votacionDto);
		assertEquals(1, errores.length);
	}
	
}
