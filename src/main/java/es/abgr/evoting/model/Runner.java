package es.abgr.evoting.model;


import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="running", catalog="secureballot")
@Getter
@Setter
public class Runner {

	@EmbeddedId
	RunnerId id;
	
	Integer votesreceived;
	
	@Transient
	String firstname;
	
	@Transient
	String lastname;
	
	@Transient
	String selected;
	
	
}
