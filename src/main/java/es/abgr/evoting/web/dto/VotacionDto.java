package es.abgr.evoting.web.dto;

import java.util.ArrayList;
import java.util.List;

import es.abgr.evoting.model.Ballot;
import es.abgr.evoting.model.Voter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VotacionDto {

	
	private Voter voter;
	
	private List<Ballot> ballots;
	

	public void addBallot(Ballot ballot) {
		if (ballots==null) {
			ballots = new ArrayList<Ballot>();
		}
		this.ballots.add(ballot);
	}
	
	

}
