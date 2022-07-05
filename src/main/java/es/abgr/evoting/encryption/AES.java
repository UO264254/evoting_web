package es.abgr.evoting.encryption;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import es.abgr.evoting.exceptions.ENCException;
 
public class AES {
	
	private static final String CBCMode = "AES/CBC/PKCS5Padding";
	private static final String ECBMode = "AES/ECB/PKCS5Padding";

	public static byte[] genKey(String random) throws ENCException {
		return Hash.computeHash(random, 32, "chiave");
	}
	
	public static byte[] genIV(String random) throws ENCException {
		return Hash.computeHash(random, 16, "IV");
	}
	
    public static String encryptVote(String plainVote, byte[] ki, byte[] iv) throws ENCException {
    	SecretKeySpec secretKey = new SecretKeySpec(ki, "AES");
    	IvParameterSpec ivSpec = new IvParameterSpec(iv);
        
    	try {
    		Cipher cipher = Cipher.getInstance(CBCMode);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(plainVote.getBytes("UTF-8")));
    	} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
    		throw new ENCException("Error cifrando el voto", e);
		}
    }
 
    public static String decryptVote(String encryptedVote, byte[] ki, byte[] iv) throws ENCException {
    	SecretKeySpec secretKey = new SecretKeySpec(ki, "AES");
    	IvParameterSpec ivSpec = new IvParameterSpec(iv);
        
    	try {
            Cipher cipher = Cipher.getInstance(CBCMode);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedVote)), "UTF-8");
    	} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
    		throw new ENCException("Erro en el descifrado simétrico", e);
		}
    	
    }

    public static byte[] encryptPrivateKey(byte[] key, String secret) throws ENCException {
    	SecretKeySpec secretKey = new SecretKeySpec(genKey(secret), "AES");

    	try {
    		Cipher cipher = Cipher.getInstance(ECBMode);
        	cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        	return cipher.doFinal(key);
    	} 
    	catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
    		throw new ENCException("Error en el cifrado simétrico", e);
    	}
    }
    
    public static byte[] decryptPrivateKey(byte[] encrKey, String secret) throws ENCException {
    	SecretKeySpec secretKey = new SecretKeySpec(genKey(secret), "AES");
    	
    	try {
    		Cipher cipher = Cipher.getInstance(ECBMode);
        	cipher.init(Cipher.DECRYPT_MODE, secretKey);
        	return cipher.doFinal(encrKey);
    	}
    	catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
    		throw new ENCException("Error en el descifrado simétrico", e);
    	}
    }
    
    public static String encryptNonce(int nonce, String secret) throws ENCException {
    	SecretKeySpec secretKey = new SecretKeySpec(genKey(secret), "AES");
    	
    	String nonceString = Integer.toString(nonce);
    	
    	try {
    		byte[] nonceBA = nonceString.getBytes("UTF-8");
        	
        	Cipher cipher = Cipher.getInstance(ECBMode);
        	cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        	
        	return Base64.getEncoder().encodeToString(cipher.doFinal(nonceBA));
    	}
    	catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
    		throw new ENCException("Error en el cifrado simétrico", e);
    	}
    	
    }

    public static int decryptNonce(String encryptedNonce, String secret) throws ENCException  {
    	SecretKeySpec secretKey = new SecretKeySpec(genKey(secret), "AES");
    	
    	try {
    		Cipher cipher = Cipher.getInstance(ECBMode);
        	cipher.init(Cipher.DECRYPT_MODE, secretKey);
        	byte[] nonceBA = cipher.doFinal(Base64.getDecoder().decode(encryptedNonce));
        	String nonceString= new String(nonceBA, "UTF-8");
        	return Integer.parseInt(nonceString);
    	}
    	catch(NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
    		throw new ENCException("Error en el descifrado simétrico", e);
    	}
    	catch (InvalidKeyException | BadPaddingException e) {
    		throw new ENCException("Clave de sesión incorrecta", e);
    	}
    	
    }
}