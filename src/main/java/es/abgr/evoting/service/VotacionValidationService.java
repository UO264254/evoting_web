package es.abgr.evoting.service;

import es.abgr.evoting.model.Voter;
import es.abgr.evoting.web.dto.VotacionDto;

public interface VotacionValidationService {

	String[] validar(Voter voter);
	
	public String[] validarMaxPreferencias(VotacionDto votacion);

}
