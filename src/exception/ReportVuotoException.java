package exception;

public class ReportVuotoException extends RuntimeException {
		public ReportVuotoException() {
		super("Nessun messaggio");
	}
	
	public ReportVuotoException(String messaggio) {
		super(messaggio);
	}

}
