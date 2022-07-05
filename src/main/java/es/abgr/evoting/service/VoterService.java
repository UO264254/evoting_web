package es.abgr.evoting.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import es.abgr.evoting.model.Voter;
import es.abgr.evoting.web.dto.VoterRegistrationDto;

public interface VoterService extends UserDetailsService {

    Voter findByUsername(String username);
    Voter findByEmail(String email);

    Voter save(VoterRegistrationDto registration);
}
