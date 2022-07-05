package es.abgr.evoting.exceptions;

@SuppressWarnings("serial")
public class CSTException extends Exception {

	public CSTException(String mensaje, IllegalArgumentException e) {
		super(mensaje, e);
	}

}
