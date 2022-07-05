package es.abgr.evoting.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="voterballotslist", catalog="secureballot")
@Getter
@Setter
public class VoterBallotList {

		@EmbeddedId
		private VoterBallotListId id;
		
		
}
