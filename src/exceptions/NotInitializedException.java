package exceptions;
public class NotInitializedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NotInitializedException() {
		System.err.println("Connection not initialized.");
	}
}