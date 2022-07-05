package es.abgr.evoting.services;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

import es.abgr.evoting.repositories.CandidateRepository;
import es.abgr.evoting.repositories.CandidateRepositoryImpl;
import es.abgr.evoting.service.VotacionValidationService;
import es.abgr.evoting.service.VotacionValidationServiceImpl;
import es.abgr.evoting.service.VoterService;
import es.abgr.evoting.service.VoterServiceImpl;

@TestConfiguration
public class ServiceImplTestContextConfiguration {

	@Bean
	public VoterService voteService() {
		return new VoterServiceImpl();
	}

	@Bean
	public VotacionValidationService votacionValidationService() {
		return new VotacionValidationServiceImpl();
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {

		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasenames("messages/label");
		source.setUseCodeAsDefaultMessage(true);

		return source;
	}
	
	@Bean
	public CandidateRepository candidateRepository() {
		return new CandidateRepositoryImpl();
	}
}
