package es.abgr.evoting.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ReferendumOptionId implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int procedurecode;
	int ballotcode;
	String text;
	
}
