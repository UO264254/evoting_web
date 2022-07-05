package es.abgr.evoting.web.dto;

import java.util.ArrayList;
import java.util.List;

import es.abgr.evoting.model.Ballot;
import es.abgr.evoting.model.VotePacket;
import es.abgr.evoting.model.Voter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VotacionEnviadaDto {

	private Voter voter;
	private String[][] nonces;
	private String sessionKey;
	private List<Ballot> encryptedBallots;
	
	
	public void addEncryptedBallot(Ballot encryptedBallot) {
		if (encryptedBallots==null) {
			encryptedBallots = new ArrayList<Ballot>();
		}
		this.encryptedBallots.add(encryptedBallot);
		
	}
	
    
    

	
}
