package es.abgr.evoting.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.abgr.evoting.exceptions.DEVException;
import es.abgr.evoting.exceptions.ENCException;
import es.abgr.evoting.exceptions.FLRException;
import es.abgr.evoting.model.Ballot;
import es.abgr.evoting.model.BallotId;
import es.abgr.evoting.model.Candidate;
import es.abgr.evoting.model.Runner;
import es.abgr.evoting.model.Voter;
import es.abgr.evoting.model.VoterBallotList;
import es.abgr.evoting.service.BallotService;
import es.abgr.evoting.service.CandidateService;
import es.abgr.evoting.service.ProcedureService;
import es.abgr.evoting.service.VotacionService;
import es.abgr.evoting.service.VotacionValidationService;
import es.abgr.evoting.service.VoterBallotListService;
import es.abgr.evoting.service.VoterService;
import es.abgr.evoting.web.dto.VotacionDto;

@Controller
@RequestMapping({"/", "/votacion"})
public class VotacionController {
	
	private VoterBallotListService voterBallotListService;
	private BallotService ballotService;
	private VoterService voterService;
	private CandidateService candidateService;
	private VotacionService votacionService;
	
	@Autowired
	private VotacionValidationService votacionValidationService;
	
	@Autowired
	private ProcedureService procedureService;
	
	
    public VotacionController(VoterBallotListService voterBallotListService, VoterService voterService, BallotService ballotService, CandidateService candidateService, VotacionService votacionService) {
        super();
        this.ballotService = ballotService;
        this.voterService = voterService;
        this.voterBallotListService = voterBallotListService;
        this.candidateService = candidateService;
        this.votacionService = votacionService;
    }

    @ModelAttribute("cuestionario")
    public VotacionDto votacionDto() {
        return new VotacionDto();
    }

    @GetMapping
    public String showVotacionForm(@ModelAttribute("cuestionario")
    VotacionDto cuestionario, BindingResult result) {
    	Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName(); 
        
        Voter voter = voterService.findByUsername(username);
       
        String[] errores = votacionValidationService.validar(voter);
        if (errores.length>0) {
        	String mensaje;
			try {
				mensaje = URLEncoder.encode(String.join("|", errores), StandardCharsets.UTF_8.toString());
			} catch (UnsupportedEncodingException e) {
				return "redirect:/adios".concat("?errors="+ String.join("|", errores));
			}
        	return "redirect:/adios".concat("?errors="+mensaje);
        }
        
        cuestionario.setVoter(voter);
               
        List<VoterBallotList> ballots = voterBallotListService.findByVoterid(username);
        cuestionario.setBallots(new ArrayList<Ballot>());
        for (VoterBallotList vbl : ballots) {
        	BallotId id = new BallotId();
        	id.setCode(vbl.getId().getBallotcode());
        	id.setProcedure(procedureService.findByCode(vbl.getId().getProcedurecode()));
        	Ballot ballot = ballotService.findById(id);
        	for (Runner runner : ballot.getRunners()) {
        		Candidate candidate = candidateService.findRunner(runner.getId());
        		runner.setFirstname(candidate.getFirstname());
        		runner.setLastname(candidate.getLastname());
        	}
        	
			cuestionario.addBallot(ballot);
        }
      
        
        return "votacion";
    }

    @PostMapping
    public String registerVotacion(@ModelAttribute("cuestionario")
            @Valid VotacionDto votacionDto,  BindingResult result)  {
    	StringBuffer redirectView = new StringBuffer("redirect:/adios");
    	
    	if (votacionDto.getBallots()!=null) {
    		try {
    			String[] errores = votacionValidationService.validarMaxPreferencias(votacionDto);
    			if (errores.length > 0) {
    				int i=0;
			        for(String err : errores) {
			        	result.addError(new ObjectError("votacionError", err));	
			        	i++;
			        }
			        return "votacion";
    			}
				votacionService.sendVotes(votacionDto);
			} catch (DEVException|ENCException|FLRException e) {
				try {
					e.printStackTrace();
					redirectView.append("?errors="+URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8.toString()));
				} catch (UnsupportedEncodingException e1) {
					redirectView = new StringBuffer("redirect:/adios");
				}
				
			}
    	}
    	
        return redirectView.toString(); //"redirect:/adios";
    }
}
