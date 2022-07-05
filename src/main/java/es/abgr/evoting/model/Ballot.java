package es.abgr.evoting.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

/**
 * Representa todas las cuestiones que se somenten a votación en un proceso electoral.
 *
 */
@Getter
@Setter
@Entity
@Table(name="ballot", catalog="secureballot")
public class Ballot implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6232797152115412171L;
	
	@EmbeddedId
	BallotId id;
	/*
	int code;
	@ManyToOne
    @JoinColumn(name="procedurecode")
	Procedure procedure;
	*/
	String name;
	String description;
	int maxpreferences;
	@OneToMany
	@JoinColumns({@JoinColumn(name="procedurecode", referencedColumnName="procedurecode"),
		@JoinColumn(name="ballotcode", referencedColumnName="code")})
	List<ReferendumOption> options;
	@OneToMany
	@JoinColumns({@JoinColumn(name="procedurecode", referencedColumnName="procedurecode"),
		@JoinColumn(name="ballotcode", referencedColumnName="code")})
	List<Runner> runners;
	
	@Transient
	private List<VotePacket> encryptedVotePackets;
	
	public void addEncryptedVotePackets(VotePacket votePacket) {
		if (encryptedVotePackets==null) {
			encryptedVotePackets = new ArrayList<VotePacket>();
		}
		this.encryptedVotePackets.add(votePacket);
	}
	
	@Transient
	String selected;
	
}
