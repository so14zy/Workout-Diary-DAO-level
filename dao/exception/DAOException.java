package basis.dao.exception;

public class DAOException extends Exception {

	private static final long serialVersionUID = -929847478054279748L;
	
	public DAOException(Throwable cause) {
		super(cause);
	}
	
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}
}
