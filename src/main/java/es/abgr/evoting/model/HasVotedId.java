package es.abgr.evoting.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@Embeddable
public class HasVotedId implements Serializable{
	int procedurecode;
	String voterid;
}
