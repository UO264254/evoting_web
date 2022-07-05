package es.abgr.evoting.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import es.abgr.evoting.Constants;
import es.abgr.evoting.model.Ballot;
import es.abgr.evoting.model.HasVotedId;
import es.abgr.evoting.model.ReferendumOption;
import es.abgr.evoting.model.Runner;
import es.abgr.evoting.model.Voter;
import es.abgr.evoting.repositories.HasVotedRepository;
import es.abgr.evoting.web.dto.VotacionDto;

@Service
public class VotacionValidationServiceImpl implements VotacionValidationService {

	@Autowired
	HasVotedRepository hasVotedRepository;
	
	@Autowired
	ResourceBundleMessageSource messageSource;
	
	
	@Override
	public String[] validar(final Voter voter) {
		
		//Ha votado ya ?
		List<String> errores = new ArrayList<String>();
		HasVotedId hasVotedId = new HasVotedId();
		hasVotedId.setProcedurecode(voter.getProcedure().getCode());
		hasVotedId.setVoterid(voter.getUsername());
		if (hasVotedRepository.findById(hasVotedId).isPresent()) {
			errores.add(messageSource.getMessage(Constants.ERROR_YA_VOTADO, new Object[] {voter.getUsername(),voter.getProcedure().getName()},Locale.getDefault()));
			
		}
		
		//Procedimiento abierto?
		Date ahora = new Date();
				
		if (ahora.before(voter.getProcedure().getStarts()) || ahora.after(voter.getProcedure().getEnds())) {
			errores.add(messageSource.getMessage(Constants.ERROR_PROCESO_CERRADO, null, Locale.getDefault()));
		}
			
		return  errores.toArray(new String[errores.size()]);
	}


	public String[] validarMaxPreferencias(VotacionDto votacion) {
		List<String> errores = new ArrayList<String>();
		for(Ballot ballot : votacion.getBallots()) {
			if (ballot.getMaxpreferences() > 1) {
				int selected=0;
				if (ballot.getOptions()!=null && ballot.getOptions().size()>0) {
					for (ReferendumOption option : ballot.getOptions()) {
						if (option.getSelected()!=null && option.getId().getText().equals(option.getSelected())) {
							selected++;
						}
					}
				} else if (ballot.getRunners()!=null && ballot.getRunners().size()>0) {
					for (Runner runner : ballot.getRunners()) {
						if (runner.getSelected()!=null && runner.getSelected().equals(runner.getId().getCandidateid())) {
							selected++;
						}
					}
				}
				if (selected>ballot.getMaxpreferences()) {
					errores.add(messageSource.getMessage(Constants.ERROR_MAXPREFERENCES, new Object[] {ballot.getDescription()}, Locale.getDefault())); 
				}
			}
			
		}
		return  errores.toArray(new String[errores.size()]);
	}

}
