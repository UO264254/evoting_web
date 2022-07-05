package es.abgr.evoting.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import es.abgr.evoting.model.Ballot;
import es.abgr.evoting.model.BallotId;
import es.abgr.evoting.model.Procedure;
import es.abgr.evoting.model.ReferendumOption;
import es.abgr.evoting.model.ReferendumOptionId;
import es.abgr.evoting.model.Voter;
import es.abgr.evoting.web.dto.VotacionDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VotacionTest {
	
		private List<Procedure> procedures;
		private List<Voter> voters;
		private VotacionDto votacionDto;

		public void initData() {
			procedures = new ArrayList<Procedure>();
			//Proceso electoral
	    	Procedure procedure = new Procedure();
	    	procedure.setCode(1);
	    	procedure.setName("TEST1");
	    	procedure.setStarts(DateUtils.addDays(new Date(), -1));
	    	procedure.setEnds(DateUtils.addDays(new Date(), 1));
	    	procedure.setSupervisor("supervisor");
	    	procedure.setWeb(1);
	    	
	    	procedures.add(procedure);
	    	
	    	Procedure procedure2 = new Procedure();
	    	procedure2.setCode(2);
	    	procedure2.setName("TEST2");
	    	procedure2.setStarts(DateUtils.addDays(new Date(), -3));
	    	procedure2.setEnds(DateUtils.addDays(new Date(), -1));
	    	procedure2.setSupervisor("supervisor");
	    	
	    	procedures.add(procedure2);
	    	
	    	
	    	List<String> usernames = Arrays.asList("alex", "bob","john");
	    	voters = new ArrayList<Voter>();
	    	
	    	for (String username : usernames) {
	    		voters.add(createVoter(username, procedures.get(voters.size()%2)));
	    	}
	    	
	    	
	    	
	    	votacionDto = new VotacionDto();
	    	votacionDto.setVoter(getVoters().get(2));
	    	votacionDto.setBallots(createBallots());
	    	
	    
		}
		
		private List<Ballot> createBallots() {
			List<Ballot> ballots = new ArrayList<>();
			for (int i=0; i<2; i++) {
				Ballot ballot = new Ballot();
		    	BallotId ballotId = new BallotId();
		    	ballotId.setCode(i+1);
		    	ballotId.setProcedure(procedures.get(0));
		    	ballot.setId(ballotId);
		    	ballot.setDescription("Ballot test " + (i+1));
		    	ballot.setMaxpreferences(i+1);
		    	ballot.setOptions(createOptions(ballotId.getCode(), ballotId.getProcedure().getCode()));
		    	ballots.add(ballot);
			}
			return ballots;
		}
		
		private List<ReferendumOption> createOptions(int ballotcode, int procedurecode) {
			List<ReferendumOption> options = new ArrayList<ReferendumOption>();
			for (int i=0; i<3; i++) {
				ReferendumOptionId refOptionId = new ReferendumOptionId();
		    	refOptionId.setBallotcode(ballotcode);
		    	refOptionId.setProcedurecode(procedurecode);
		    	refOptionId.setText("Option " + i);
		    	ReferendumOption refOption = new ReferendumOption();
		    	refOption.setId(refOptionId);
		    	options.add(refOption);
			}
			return options;
		}

		private Voter createVoter(String username, Procedure procedure) {
	    	Voter voter = new Voter();
	    	voter.setUsername(username);
	    	voter.setEmail(username+"@test.es");
	    	voter.setFirstname(username.toUpperCase());
	    	voter.setLastname(username.toUpperCase());
	    	voter.setProcedure(procedure);
	    	voter.setPassword(username+"2022");
	    	return voter;
		   }
}
