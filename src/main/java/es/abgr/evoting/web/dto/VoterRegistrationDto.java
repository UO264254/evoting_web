package es.abgr.evoting.web.dto;

import java.util.Collection;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;

import es.abgr.evoting.model.Procedure;

public class VoterRegistrationDto {
	
	@Autowired
	ResourceBundleMessageSource messageSource;

	@NotBlank(message = "Usuario obligatorio")
    @Length(min=4, message = "Usuario: mínimo 4 caracteres")
    private String username;

	@NotEmpty(message ="Nombre obligatorio")
    private String firstname;
    
    private String lastname;
    
   
    private Integer procedure;
    
    @NotEmpty(message="Password obligatorio")
    @Length(min=6, message = "Password: mímino 6 caracteres")
    private String password;

   
    @Email(message="Email incorrecto")
    @NotEmpty(message="Email obligatorio")
    private String email;

   
    
	private Collection<Procedure> procedures;

   
    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstName) {
		this.firstname = firstName;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastName) {
		this.lastname = lastName;
	}

	public Integer getProcedure() {
		return procedure;
	}

	public void setProcedure(Integer procedure) {
		this.procedure = procedure;
	}

	public void setProcedures(Collection<Procedure> procedures) {
		this.procedures = procedures;
	}
	
	
	public Collection<Procedure> getProcedures() {
		return procedures;
	}

   

}
