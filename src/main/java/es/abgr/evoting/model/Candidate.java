package es.abgr.evoting.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="candidate", catalog="secureballot")
@Getter
@Setter
public class Candidate {

	@EmbeddedId
	CandidateId id;
	
	String firstname;
	String lastname;
	String dateofbirth;
	
}
