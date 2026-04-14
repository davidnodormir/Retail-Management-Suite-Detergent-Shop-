package exception;

public class DBConnectionException extends RuntimeException {
	public DBConnectionException() {
		super("Nessun messaggio");
	}
	
	public DBConnectionException(String messaggio) {
		super(messaggio);
	}
}
