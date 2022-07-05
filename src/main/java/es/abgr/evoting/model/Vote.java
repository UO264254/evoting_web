package es.abgr.evoting.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="vote", catalog="secureballot")
public class Vote {

	@EmbeddedId
	VoteId id;
	
	
	String encryptedstring;
	String encryptedkey;
	String encryptediv;
	String signature;
	
	
}
