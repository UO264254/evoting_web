package es.abgr.evoting.exceptions;

@SuppressWarnings("serial")
public class ENCException extends Exception {

	public ENCException(String mensaje) {
		super(mensaje);
	}

	public ENCException(String mensaje, Exception e) {
		super(mensaje, e);
	}
}
