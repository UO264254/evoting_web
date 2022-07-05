package es.abgr.evoting.encryption;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;

import es.abgr.evoting.exceptions.DEVException;
import es.abgr.evoting.exceptions.ENCException;

public class NonceManager {
	
	public static int genSingleNonce() {
		SecureRandom random = new SecureRandom();
		return random.nextInt();
	}
	public static String solveChallenge(String encryptedNonce, String password, int challenge) throws DEVException, ENCException {
		int decryptedNonce = AES.decryptNonce(encryptedNonce, password);
		int nonceResponse = getNonceResponse(decryptedNonce, challenge);
		String encryptedNonceResponse = AES.encryptNonce(nonceResponse, password);
		
		return encryptedNonceResponse;
	}
	public static int getNonceResponse(int nonce, int challenge) throws DEVException {
		int modifiedNonce;
		
		switch(challenge) {
			case 1:
				modifiedNonce = nonce + 1;
				break;
			case 2:
				modifiedNonce = nonce * 2;
				break;
			case 3:
				modifiedNonce = nonce + 30;
				break;
			default:
				throw new DEVException("Imposible continuar con la votación en curso");
		}
		
		return modifiedNonce;
	}
	
	public static ArrayList<ArrayList<Integer>> genMultipleNonces(ArrayList<Integer> structure) {
		SecureRandom random = new SecureRandom();
		 
		ArrayList<ArrayList<Integer>> totalNonces = new ArrayList<ArrayList<Integer>>();
		HashSet<Integer> usedNonces = new HashSet<Integer>();
		
		for(Integer ballotMaxPreferences : structure) {
			ArrayList<Integer> ballotNonces = new ArrayList<Integer>();
			
			for(int i = 0; i < ballotMaxPreferences; i++) {
				boolean found = false;
				while(!found) {
					int nonce = random.nextInt();
					found = ! usedNonces.contains(nonce);
					
					if(found) {
						ballotNonces.add(nonce);
						usedNonces.add(nonce);
					}
				}
			}
			
			totalNonces.add(ballotNonces);
		}
		
		return totalNonces;
	}
	
	public static ArrayList<ArrayList<Integer>> genMultipleNonces(int[] structure) {
		ArrayList<Integer> newStructure = new ArrayList<>();
		for (int i=0; i<structure.length; i++)
			newStructure.add(structure[i]);
		
		return genMultipleNonces(newStructure);
	}
	
	public static String[][] encryptMultipleNonces(ArrayList<ArrayList<Integer>> nonces, String sessionKey) throws ENCException{
		String[][] encryptedNonces = new String[nonces.size()][];
		
		int i = 0;
		for(ArrayList<Integer> ballotNonces : nonces) {
			String[] ballotEncryptedNonces = new String[ballotNonces.size()];
			
			int j = 0;
			for(Integer nonce : ballotNonces) {
				ballotEncryptedNonces[j] = AES.encryptNonce(nonce, sessionKey);
				j++;
			}
			
			encryptedNonces[i] = ballotEncryptedNonces;
			i++;
		}
		
		return encryptedNonces;
	}
	
	
	
}
