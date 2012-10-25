/**
 * @author Theneva
 * @author Mads
 * @version 1.0
 * Due Date 2012.10.22
 */

package exceptions;

public class NotInitializedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NotInitializedException() {
		System.err.println("Connection not initialized.");
	}
}