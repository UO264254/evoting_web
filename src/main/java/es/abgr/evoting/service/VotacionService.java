package es.abgr.evoting.service;

import es.abgr.evoting.exceptions.DEVException;
import es.abgr.evoting.exceptions.ENCException;
import es.abgr.evoting.exceptions.FLRException;
import es.abgr.evoting.web.dto.VotacionDto;

public interface VotacionService {

	void sendVotes(VotacionDto votacionDto) throws DEVException, ENCException, FLRException;

}
