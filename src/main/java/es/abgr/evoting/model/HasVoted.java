package es.abgr.evoting.model;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="hasvoted", catalog="secureballot")
public class HasVoted {

	@EmbeddedId
	HasVotedId id;
	
	String stationid;
	String documenttype;
	String documentid;
}
