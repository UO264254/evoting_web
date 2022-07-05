package es.abgr.evoting.service;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import es.abgr.evoting.Constants;
import es.abgr.evoting.encryption.AES;
import es.abgr.evoting.encryption.NonceManager;
import es.abgr.evoting.encryption.VoteEncryption;
import es.abgr.evoting.exceptions.DEVException;
import es.abgr.evoting.exceptions.ENCException;
import es.abgr.evoting.exceptions.FLRException;
import es.abgr.evoting.model.Ballot;
import es.abgr.evoting.model.DocumentType;
import es.abgr.evoting.model.HasVoted;
import es.abgr.evoting.model.HasVotedId;
import es.abgr.evoting.model.ReferendumOption;
import es.abgr.evoting.model.Runner;
import es.abgr.evoting.model.Terminals;
import es.abgr.evoting.model.Vote;
import es.abgr.evoting.model.VoteId;
import es.abgr.evoting.model.VotePacket;
import es.abgr.evoting.repositories.HasVotedRepository;
import es.abgr.evoting.repositories.ProcedureRepository;
import es.abgr.evoting.repositories.VoteRepository;
import es.abgr.evoting.web.dto.VotacionDto;
import es.abgr.evoting.web.dto.VotacionEnviadaDto;

@Service
public class VotacionServiceImpl implements VotacionService {

	@Autowired
	private ProcedureRepository procedureRepository;

	@Autowired
	private HasVotedRepository hasvotedRepository;

	@Autowired
	private VoteRepository voteRepository;

	@Autowired
	Environment environment;

	@Override
	public void sendVotes(final VotacionDto votacionDto) throws DEVException, ENCException, FLRException {
		VotacionEnviadaDto votacionEnviadaDto = new VotacionEnviadaDto();
		votacionEnviadaDto.setVoter(votacionDto.getVoter());
		votacionEnviadaDto.setEncryptedBallots(votacionDto.getBallots());
		askForNonces(votacionDto, votacionEnviadaDto);
		int index = 0;
		String supervisor = votacionEnviadaDto.getVoter().getProcedure().getSupervisor();
		byte[] puk = procedureRepository.getPublicKey(supervisor);
		byte[] prk = procedureRepository.getPrivateKey(supervisor);
		
		for (Ballot ballot : votacionDto.getBallots()) {
			encryptBallot(votacionEnviadaDto, ballot, index, puk, prk);
			index++;
		}
		
		storeVotes(votacionEnviadaDto);

	}

	private void encryptBallot(VotacionEnviadaDto votacionEnviadaDto, Ballot ballot, int iBallot, byte[] puk, byte[] prk)
			throws DEVException, ENCException, FLRException {
		int index = 0;
		
		prk = AES.decryptPrivateKey(prk, environment.getProperty("signingPass"));
		if (ballot.getOptions()!=null && ballot.getOptions().size()>0) {
			for (ReferendumOption option : ballot.getOptions()) {
				String markedOption="";
				if (ballot.getMaxpreferences()==1) {
					if (ballot.getSelected()!=null && ballot.getSelected().equals(option.getId().getText())) {
						markedOption = Constants.isOption + ballot.getSelected();
					}
				} else if (ballot.getMaxpreferences()>1) {
					if (option.getSelected()!=null && option.getSelected().equals(option.getId().getText())) {
						markedOption = Constants.isOption + option.getSelected();
					}
				}
				if (!markedOption.isEmpty()) {
					VotePacket packet = VoteEncryption.encrypt(markedOption, puk,
							votacionEnviadaDto.getNonces()[iBallot][index], votacionEnviadaDto.getSessionKey());
					VoteEncryption.signPacket(packet, prk);
					ballot.addEncryptedVotePackets(packet);
					index++;
				}
			}
		} else if (ballot.getRunners()!=null && ballot.getRunners().size()>0) {
			for (Runner runner : ballot.getRunners()) {
				String markedOption="";
				if (ballot.getMaxpreferences()==1) {
					if (ballot.getSelected()!=null && ballot.getSelected().equals(runner.getId().getCandidateid())) {
						markedOption =ballot.getSelected();
					}
				} else if (ballot.getMaxpreferences()>1) {
					if (runner.getSelected()!=null && runner.getSelected().equals(runner.getId().getCandidateid())) {
						markedOption = runner.getSelected();
					}
				}
				if (!markedOption.isEmpty()) {
					VotePacket packet = VoteEncryption.encrypt(markedOption, puk,
							votacionEnviadaDto.getNonces()[iBallot][index], votacionEnviadaDto.getSessionKey());
					VoteEncryption.signPacket(packet, prk);
					ballot.addEncryptedVotePackets(packet);
					index++;
				}
			}
		}
		// Los emptys??? Es decir los que ha dejado sin seleccionar opción???
		if (index==0)  { //Ninguna opción seleccionada
			VotePacket packet = VoteEncryption.encrypt(Constants.emptyBallot, puk, votacionEnviadaDto.getNonces()[iBallot][index], votacionEnviadaDto.getSessionKey());
			VoteEncryption.signPacket(packet, prk);
			ballot.addEncryptedVotePackets(packet);
			index++;
		}
		//Opciones no seleccionadas
		for (int i=ballot.getMaxpreferences()-index; i>0; i--) {
			VotePacket packet = VoteEncryption.encrypt(Constants.emptyPreference, puk, votacionEnviadaDto.getNonces()[iBallot][index], votacionEnviadaDto.getSessionKey());
			VoteEncryption.signPacket(packet, prk);
			ballot.addEncryptedVotePackets(packet);
			index++;
		}
		

		

	}

	@Transactional
	private void storeVotes(VotacionEnviadaDto votacionEnviadaDto) {
		updateHasVoted(votacionEnviadaDto);
		int procedurecode = votacionEnviadaDto.getVoter().getProcedure().getCode();
		
		for (Ballot ballot : votacionEnviadaDto.getEncryptedBallots()) {
			Integer nonceDiscriminator=0;
			for (VotePacket packet : ballot.getEncryptedVotePackets()) {
				nonceDiscriminator = voteRepository.getMaxNonceDiscrim(
						procedurecode, 
						ballot.getId().getCode(),
						packet.getSolvedNonce());
				if (nonceDiscriminator==null) {
					nonceDiscriminator=0;
				} else {
					nonceDiscriminator=nonceDiscriminator+1;
				}
				VoteId voteid = new VoteId();
				voteid.setProcedurecode(procedurecode);
				voteid.setBallotcode(ballot.getId().getCode());
				voteid.setNoncediscrim(nonceDiscriminator);
				voteid.setEncryptednonce(packet.getSolvedNonce());
				Vote vote = new Vote();
				vote.setId(voteid);
				vote.setEncryptediv(packet.getEncryptedIV());
				vote.setEncryptedkey(packet.getEncryptedKi());
				vote.setEncryptedstring(packet.getEncryptedVote());
				vote.setSignature(packet.getSignature());
				voteRepository.save(vote);
			}
			
		}
		
	}

	

	private void updateHasVoted(VotacionEnviadaDto votacionEnviadaDto) {
		HasVoted hasvoted = new HasVoted();
		HasVotedId hasvotedId = new HasVotedId();
		hasvotedId.setProcedurecode(votacionEnviadaDto.getVoter().getProcedure().getCode());
		hasvotedId.setVoterid(votacionEnviadaDto.getVoter().getUsername());
		hasvoted.setDocumentid("");
		hasvoted.setDocumenttype(DocumentType.CONOSCENZA_PERSONALE.toString());
		hasvoted.setId(hasvotedId);
		hasvotedRepository.save(hasvoted);
	}

	private void askForNonces(final VotacionDto votacionDto, VotacionEnviadaDto votacionEnviadaDto)
			throws DEVException, ENCException {
		// Array con el número máximo de opciones elegibles en cada ballot
		int[] maxPreferences = getBallotsMaxPreferences(votacionDto);
		if (maxPreferences == null) {
			throw new DEVException("Imposible seguir con la votación en curso");
		}

		genNonces(votacionDto, maxPreferences, votacionEnviadaDto);

	}

	private void genNonces(final VotacionDto votacionDto, int[] maxPreferences, VotacionEnviadaDto votacionEnviadaDto)
			throws ENCException, DEVException {

		String sessionKey = getSymmetricKey(votacionDto.getBallots().get(0).getId().getProcedure().getCode());
		if (sessionKey==null) {
			throw new DEVException("Terminal no encontrada");
		}
		ArrayList<ArrayList<Integer>> voteNonces = NonceManager.genMultipleNonces(maxPreferences);

		votacionEnviadaDto.setSessionKey(sessionKey);
		votacionEnviadaDto.setNonces(NonceManager.encryptMultipleNonces(voteNonces, sessionKey));

	}

	private String getSymmetricKey(int code) {
		return procedureRepository.getSymmetricKey(code, Integer.valueOf(environment.getProperty("post.session")),
				environment.getProperty("post.ip"), Terminals.Type.Web.name());
	}

	private int[] getBallotsMaxPreferences(final VotacionDto votacionDto) {
		int n = votacionDto.getBallots().size();
		int[] maxPreferences = new int[n];

		for (int i = 0; i < n; i++) {
			maxPreferences[i] = votacionDto.getBallots().get(i).getMaxpreferences();
		}
		return maxPreferences;
	}

}
