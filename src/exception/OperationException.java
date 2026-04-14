package exception;

public class OperationException extends Exception {
	public OperationException() {
		super("Nessun messaggio");
	}
	
	public OperationException(String messaggio) {
		super(messaggio);
	}
}
