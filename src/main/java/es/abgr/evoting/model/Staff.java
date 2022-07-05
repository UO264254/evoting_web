package es.abgr.evoting.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Staff {
	
	
	byte[] hashedpassword; 
	@Id
	String username;
	byte[] publickey1; 
	byte[] publickey2;
	byte[] encryptedprivatekey1;
	byte[] encryptedprivatekey2; 
	String type;
}
