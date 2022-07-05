package es.abgr.evoting.repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.abgr.evoting.model.Procedure;

@Repository
public interface ProcedureRepository extends JpaRepository<Procedure, Integer> {
	Procedure findByCode(int code);
	
	@Query("SELECT p FROM Procedure p WHERE p.starts <= CURRENT_TIME and p.ends>=CURRENT_TIME and p.web is not null and p.web <> 0")
	Collection<Procedure> findAllActiveWebProcedures();
	
	@Query("SELECT p FROM Procedure p WHERE p.starts <= :fecha and p.ends>=:fecha and p.web is not null and p.web <> 0")
	Collection<Procedure> findAllActiveWebProcedures(Date fecha);
	
	@Query("SELECT p FROM Procedure p WHERE  p.web is not null and p.web <> 0")
	Collection<Procedure> findAllWebProcedures();
	
	@Query("SELECT T.symmetrickey FROM Terminal T WHERE T.procedurecode = :procedurecode AND T.sessioncode=:sessioncode  "
			+ " AND T.ipaddress=:ipaddress AND T.type=:type")
	String getSymmetricKey(int procedurecode, Integer sessioncode, String ipaddress, String type);

	@Query("SELECT s.encryptedprivatekey2 FROM Staff s WHERE s.username=:username")
	byte[] getPrivateKey(String username);

	@Query("SELECT s.publickey1 FROM Staff s WHERE s.username=:username")
	byte[] getPublicKey(String username);
	
}
