package exception;

public class DAOException extends RuntimeException{
	public DAOException() {
		super("Nessun messaggio");
	}
	
	public DAOException(String messaggio) {
		super(messaggio);
	}

}
