package es.abgr.evoting.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class VoterBallotListId implements Serializable{

		private int procedurecode;
		private String voterid;
		private int ballotcode;
}
