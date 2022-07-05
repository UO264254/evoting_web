package es.abgr.evoting.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;


@Entity
@Table(name="voter", catalog="secureballot")
public class Voter {
    
   
    @NotBlank(message = "Nombre de usuario obligatorio")
    @Length(min=4, message = "Usuario: mínimo 4 caracteres")
    private String username;
   
    private String firstname;
   
    private String lastname;
    
    
    @Email(message = "Introduzca una dirección de correo válida")
    @NotBlank(message = "Email obligatorio")
    private String email;

    @NotBlank(message = "Password obligatorio")
    @Length(min=6, message = "Password: mímino 6 caracteres")
    private String password;
    
    
    private Procedure procedure;

    public Voter() {}

    public Voter(String userName, String email, String password) {
        this.username = userName;
        this.email = email;
        this.password = password;
    }

    
    public void setEmail(String email) {
        this.email = email;
    }


    public String getEmail() {
        return email;
    }
    
    public void setPassword(String password) {
		this.password = password;
		
	}
    
    public String getPassword() {
		return password;
	}
    
    @Id
    @Column(name = "id")
	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	@Column(name="firstname")
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Column(name = "lastname")
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastName) {
		this.lastname = lastName;
	}

	@ManyToOne
    @JoinColumn(name="procedurecode")
	public Procedure getProcedure() {
		return procedure;
	}

	public void setProcedure(Procedure procedure) {
		this.procedure = procedure;
	}

	@Override
	public String toString() {
		return "Voter [userName=" + username + ", email=" + email + "]";
	}


	
}
