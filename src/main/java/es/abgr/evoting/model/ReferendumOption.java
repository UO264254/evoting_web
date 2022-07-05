package es.abgr.evoting.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="referendumoption", catalog="secureballot")
@Getter
@Setter
public class ReferendumOption {
	
	@EmbeddedId
	ReferendumOptionId id;
	
	Integer votesreceived;
	
	@Transient
	String selected;
	
}
