package es.abgr.evoting.service;

import java.util.Collection;

import es.abgr.evoting.model.Procedure;

public interface ProcedureService {
	Collection<Procedure> findAllActiveWebProcedures();
	Procedure findByCode(int procedurecode);
}
