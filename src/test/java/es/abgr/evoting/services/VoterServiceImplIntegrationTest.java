package es.abgr.evoting.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import es.abgr.evoting.model.Voter;
import es.abgr.evoting.model.VoterBallotList;
import es.abgr.evoting.model.VoterBallotListId;
import es.abgr.evoting.repositories.ProcedureRepository;
import es.abgr.evoting.repositories.VoterBallotListRepository;
import es.abgr.evoting.repositories.VoterRepository;
import es.abgr.evoting.service.VoterService;
import es.abgr.evoting.web.dto.VoterRegistrationDto;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@Import(ServiceImplTestContextConfiguration.class)
public class VoterServiceImplIntegrationTest extends VotacionTest{

    @Autowired
    private VoterService voterService;
    
    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private VoterRepository voterRepository;
    
    @MockBean
    private ProcedureRepository procedureRepository;
    
    @MockBean
    private VoterBallotListRepository<VoterBallotList, VoterBallotListId> voterBallotLisRepository;

    
    @Before
    public void setUp() {
    	initData();
    	
    	Mockito.when(procedureRepository.findByCode(getProcedures().get(0).getCode())).thenReturn(getProcedures().get(0));
        Mockito.when(voterRepository.findByUsername(getVoters().get(0).getUsername())).thenReturn(getVoters().get(0));
        Mockito.when(voterRepository.findByEmail(getVoters().get(0).getEmail())).thenReturn(getVoters().get(0));
        Mockito.when(voterRepository.findByUsername("wrong_name")).thenReturn(null);
        Optional<Voter> optVoter = Optional.of(getVoters().get(0));
        Mockito.when(voterRepository.findById(getVoters().get(0).getUsername())).thenReturn(optVoter);
        Mockito.when(voterRepository.findAll()).thenReturn(getVoters());
        Mockito.when(voterRepository.findById("wrong-name")).thenReturn(Optional.empty());
        Mockito.when(voterRepository.save(Mockito.any(Voter.class))).thenAnswer(
        	       invocation -> invocation.getArgument(0) );
    }
    
   

    @Test
    public void whenValidUsername_thenVoterShouldBeFound() {
        String name = "alex";
        Voter found = voterService.findByUsername(name);

        assertThat(found.getUsername()).isEqualTo(name);
    }

    @Test
    public void whenValidUsername_thenVoterShouldNotBeFound() {
        Voter fromDb = voterService.findByUsername("wrong_name");
        assertThat(fromDb).isNull();

        verifyFindByUserameIsCalledOnce("wrong_name");
    }
    
    @Test
    public void whenValidEmail_thenVoterShouldBeFound() {
        String email = "alex@test.es";
        Voter found = voterService.findByEmail(email);

        assertThat(found.getEmail()).isEqualTo(email);
    }

    @Test
    public void whenInvalidEmail_thenVoterShouldNotBeFound() {
        Voter fromDb = voterService.findByEmail("wrong_email");
        assertThat(fromDb).isNull();

    }

    @Test
    public void save() {
    	Voter voter = voterRepository.findByUsername("alex");
    	VoterRegistrationDto dto = new VoterRegistrationDto();
    	dto.setUsername(voter.getUsername());
    	dto.setFirstname(voter.getFirstname());
    	dto.setLastname(voter.getLastname());
    	dto.setProcedure(voter.getProcedure().getCode());
    	dto.setEmail(voter.getEmail());
    	Voter result = voterService.save(dto);
    	assertEquals(result.getEmail(), voter.getEmail());
    	assertEquals(result.getUsername(), voter.getUsername());
    	assertEquals(result.getFirstname(), voter.getFirstname());
    	assertEquals(result.getLastname(), voter.getLastname());
    	assertEquals(result.getProcedure().getCode(), voter.getProcedure().getCode());
    	assertEquals(result.getProcedure().getName(), voter.getProcedure().getName());
    }
    
    @Test
    public void whenLoadUserByUsernameValid_thenReturUserDetails() {
    	assertTrue(voterService.loadUserByUsername("alex").getAuthorities().size()==1);
    	voterService.loadUserByUsername("alex").getAuthorities().forEach(auth -> assertEquals("ROLE_USER",auth.getAuthority()));
    }

     private void verifyFindByUserameIsCalledOnce(String name) {
        Mockito.verify(voterRepository, VerificationModeFactory.times(1)).findByUsername(name);
        Mockito.reset(voterRepository);
    }

    
}
