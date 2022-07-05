package es.abgr.evoting.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="procedure", catalog="secureballot")
public class Procedure {

		@Id	
		Integer code;
		String name;
		String supervisor;
		Date starts;
		Date ends;
		Integer web;
		
		@OneToMany(mappedBy="id.procedure")
		List<Ballot> ballots;
					
}
