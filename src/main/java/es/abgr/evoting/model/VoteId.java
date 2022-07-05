package es.abgr.evoting.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Embeddable
@Getter
@Setter
public class VoteId implements Serializable{
	int noncediscrim;
	int procedurecode;
	int ballotcode;
	String encryptednonce;
}
