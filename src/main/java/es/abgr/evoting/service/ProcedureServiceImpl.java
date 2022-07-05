package es.abgr.evoting.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.abgr.evoting.model.Procedure;
import es.abgr.evoting.repositories.ProcedureRepository;

@Service
public class ProcedureServiceImpl implements ProcedureService {

	@Autowired
    private ProcedureRepository procedureRepository;
	
	@Override
	public Collection<Procedure> findAllActiveWebProcedures() {
		
		return procedureRepository.findAllActiveWebProcedures();
	}

	@Override
	public Procedure findByCode(int procedurecode) {
		return procedureRepository.findByCode(procedurecode);
	}

}
