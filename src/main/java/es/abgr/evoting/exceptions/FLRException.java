package es.abgr.evoting.exceptions;

@SuppressWarnings("serial")
public class FLRException extends Exception {

	public FLRException(String mensaje, Exception exception) {
		super(mensaje, exception);
	}

}
