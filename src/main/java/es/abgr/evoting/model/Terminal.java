package es.abgr.evoting.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="terminal", catalog="secureballot")
public class Terminal {

	@Id
	int id;
	String ipaddress;
	String type;
	
	int procedurecode;
	int sessioncode;
	String symmetrickey;
	
	
}
