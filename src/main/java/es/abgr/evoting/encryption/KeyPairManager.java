package es.abgr.evoting.encryption;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import es.abgr.evoting.exceptions.ENCException;



public class KeyPairManager {
	
	private static KeyPairGenerator generator = null;
	private static KeyFactory factory = null;
	private static final int keySize = 2048;
	
	private static void setGenFacProv(boolean needGen, boolean needFact) throws ENCException {
		if(generator == null && factory == null) {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		}
		
		if(generator == null && needGen) {
			SecureRandom random = new SecureRandom();
			try {
			    generator = KeyPairGenerator.getInstance("RSA", "BC");
			    generator.initialize(keySize, random);
			} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
				throw new ENCException("Error RSA", e);
			}
		}
		
		if(factory == null && needFact) {
			try {
				factory = KeyFactory.getInstance("RSA", "BC");
			} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
				throw new ENCException("Error RSA", e);
			}
		}
	}
	
	public static KeyPair genKeyPair() throws ENCException {
		setGenFacProv(true, false);
		return generator.generateKeyPair();
	}
	
	public static Key getPublicFromBytes(byte[] pubBytes) throws ENCException {
		setGenFacProv(false, true);
		
		X509EncodedKeySpec ks = new X509EncodedKeySpec(pubBytes);
		Key Kpu;
		try {
			Kpu = factory.generatePublic(ks);
			return Kpu;
		} catch (InvalidKeySpecException e) {
			throw new ENCException("Error RSA", e);
		}
	}
	
	public static Key getPrivateFromBytes(byte[] prBytes) throws ENCException {
		setGenFacProv(false, true);
		
		PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(prBytes);
		Key Kpr;
		try {
			Kpr = factory.generatePrivate(ks);
			return Kpr;
		} catch (InvalidKeySpecException e) {
			throw new ENCException("Error RSA", e);
		}		
	}
}
