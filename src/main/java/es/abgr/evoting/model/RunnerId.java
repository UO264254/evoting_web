package es.abgr.evoting.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class RunnerId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3943957858727971538L;
	@Column(name="procedurecode")
	int procedureCode;
	@Column(name="ballotcode")
	int ballotCode;
	@Column(name="candidateid")
	String candidateid;

}
