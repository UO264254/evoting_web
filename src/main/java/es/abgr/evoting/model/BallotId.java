package es.abgr.evoting.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@Embeddable
public class BallotId implements Serializable{
	
	int code;
	@ManyToOne
    @JoinColumn(name="procedurecode")
	Procedure procedure;

}
