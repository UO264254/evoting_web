package es.abgr.evoting.service;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import es.abgr.evoting.model.Voter;
import es.abgr.evoting.repositories.ProcedureRepository;
import es.abgr.evoting.repositories.VoterBallotListRepository;
import es.abgr.evoting.repositories.VoterRepository;
import es.abgr.evoting.web.dto.VoterRegistrationDto;

@Service
public class VoterServiceImpl implements VoterService {

    @Autowired
    private VoterRepository voterRepository;
    
    @Autowired
    private ProcedureRepository procedureRepository;
    
    @Autowired
    private VoterBallotListRepository voterBallotListRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Voter findByEmail(String email){
        return voterRepository.findByEmail(email);
    }
    
    public Voter findByUsername(String username){
        return voterRepository.findByUsername(username);
    }

    public Voter save(VoterRegistrationDto registration){
        Voter voter = new Voter();
        
        voter.setUsername(registration.getUsername());
        voter.setFirstname(registration.getFirstname());
        voter.setLastname(registration.getLastname());
        voter.setProcedure(procedureRepository.findByCode(registration.getProcedure()));
        voter.setEmail(registration.getEmail());
        voter.setPassword(passwordEncoder.encode(registration.getPassword()));
        //Relaciono voter-procedure-ballots
        voterBallotListRepository.add(voter);
        return voterRepository.save(voter);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Voter user = voterRepository.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                getAuthorities("ROLE_USER")) ;
    }
    
    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Arrays.asList(new SimpleGrantedAuthority(role));
 
    }

    
}
