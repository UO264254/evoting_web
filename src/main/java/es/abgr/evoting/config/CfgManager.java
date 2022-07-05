package es.abgr.evoting.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import es.abgr.evoting.exceptions.FLRException;


/**
 * Classe utilizzata da tutti i moduli per leggere credenziali salvate all'interno di file <i>.cfg</i> nella cartella <i>src/main/resources/cfg/</i>.
 * <br/>
 * Nota che tutti i file in questa directory presentano la seguente formattazione: <i>key:value</i>.
 */
public class CfgManager {
	
	/**
	 * Legge un file <i>.cfg</i> all'interno della cartella <i>src/main/resources/cfg</i> del modulo da cui è chiamata, quindi restituisce il valore
	 * associato alla chiave passata a parametro, se esiste, altrimenti lancia un'eccezione FLRException.
	 * @param fileName Nome del file a cui si vuole accedere
	 * @param key Chiave associata al valore che si vuole recuperare
	 * @return Il valore corrispondente alla chiave
	 * @throws PEException Se il file non è <i>.cfg</i>, se il file non esiste, se la chiave non esiste 
	 */
	public static String getValue(String fileName, String key) throws FLRException {
		if(!fileName.endsWith(".cfg"))
			throw new FLRException("Imposible leer el fichero", null);
		
		try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/cfg/"+fileName))) {
			String line, currKey, currValue;
			
			while ((line = br.readLine()) != null) {
				currKey = line.split(":")[0];
				currValue = line.split(":")[1];
				
				if (currKey.equals(key))
					return currValue;
			}
		} catch (IOException e) {
			throw new FLRException("Imposible leer el fichero",  e);
		}
		throw new FLRException("No se ha encotrado la clave en el fichero", null);
		
	}
	public static byte[] getValue(String fileName) throws FLRException {
		if(!fileName.endsWith(".cfg"))
			throw new FLRException("Imposible leer el fichero", null);
		
		File file = new File("src/main/resources/cfg/"+fileName);
		byte[] bFile = new byte[(int) file.length()];
		FileInputStream fileInputStream = null;
		try {
			//Read bytes with InputStream
			   fileInputStream = new FileInputStream(file);
			   fileInputStream.read(bFile);
			   fileInputStream.close();
			   return bFile;
		} catch (IOException e) {
			throw new FLRException("Imposible leer el fichero",  e);
		}
		
		
	}
	
}
