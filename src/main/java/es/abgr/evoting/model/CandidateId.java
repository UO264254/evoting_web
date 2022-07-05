package es.abgr.evoting.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class CandidateId implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = -6751674906850823529L;
	@Column(name = "procedurecode")
	int procedurecode;
	String id;
}
