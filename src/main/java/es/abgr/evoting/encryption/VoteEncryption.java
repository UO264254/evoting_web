package es.abgr.evoting.encryption;

import java.security.Key;

import es.abgr.evoting.exceptions.DEVException;
import es.abgr.evoting.exceptions.ENCException;
import es.abgr.evoting.model.VotePacket;

public class VoteEncryption {
	
	private static VotePacket encrypt(String vote, Key Kpu_rp, String encryptedNonce, String sessionKey) throws ENCException, DEVException {
		byte[] ki = AES.genKey(RandStrGenerator.gen(64, 128));
		byte[] iv = AES.genIV(RandStrGenerator.gen(16, 32));
		
		String encryptedVote = AES.encryptVote(vote, ki, iv);
		String encryptedKi = RSA_OAEP.encrypt(ki, Kpu_rp, false);
		String encryptedIv = RSA_OAEP.encrypt(iv, Kpu_rp, false);
		String solvedNonce = NonceManager.solveChallenge(encryptedNonce, sessionKey, 3);
		
		VotePacket packet = new VotePacket(encryptedVote, encryptedKi, encryptedIv, solvedNonce);
		HMAC.sign(packet, sessionKey);
		
		return packet;
	}

	public static VotePacket encrypt(String vote, byte[] Kpu_rpBA, String encryptedNonce, String sessionKey) throws ENCException, DEVException {
		Key Kpu_rp = KeyPairManager.getPublicFromBytes(Kpu_rpBA);
		
		return encrypt(vote, Kpu_rp, encryptedNonce, sessionKey);
	}

	private static String decrypt(VotePacket packet, Key Kpr_rp) throws ENCException {
		String encryptedVote = packet.getEncryptedVote();
		String encryptedKi = packet.getEncryptedKi();
		String encryptedIV = packet.getEncryptedIV();
		
		byte[] ki = RSA_OAEP.decrypt(encryptedKi, Kpr_rp, false);
		byte[] iv = RSA_OAEP.decrypt(encryptedIV, Kpr_rp, false);
		String vote = AES.decryptVote(encryptedVote, ki, iv);
		
		return vote;
	}
	
	public static String decrypt(VotePacket packet, byte[] Kpr_rpBA) throws ENCException {
		Key Kpr_rp = KeyPairManager.getPrivateFromBytes(Kpr_rpBA);
		
		return decrypt(packet, Kpr_rp);
	}
	
	public static void signPacket(VotePacket packet, byte[] Kpr_rpBA) throws ENCException {
		int size = 32;
		
		byte[] digest = Hash.computeHash(packet, size);
		
		Key Kpr_rp = KeyPairManager.getPrivateFromBytes(Kpr_rpBA);
		
		String signature = RSA_OAEP.encrypt(digest, Kpr_rp, true);
		
		packet.sign(signature);
	}
	
	public static boolean verifyPacketSignature(VotePacket packet, byte[] Kpu_rpBA) throws ENCException {
		try {
			String signature = packet.getSignature();
			Key Kpu_rp = KeyPairManager.getPublicFromBytes(Kpu_rpBA);
			byte[] decryptedDigest = RSA_OAEP.decrypt(signature, Kpu_rp, true);
			
			return Hash.verifyHash(packet, decryptedDigest);
		}
		catch (ENCException e) {
			throw new ENCException("Error en la verificación de la firma");
		}
	}
}
