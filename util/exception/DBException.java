package basis.util.exception;

public class DBException extends Exception {

	private static final long serialVersionUID = -647110130117824635L;
	
	public DBException(Throwable cause) {
		super(cause);
	}
	
	public DBException(String message, Throwable cause) {
		super(message, cause);
	}
}
