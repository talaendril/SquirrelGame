package exceptions;

public class EntityNotFoundException extends RuntimeException {

	public EntityNotFoundException(String e) {
		super(e);
	}
}
