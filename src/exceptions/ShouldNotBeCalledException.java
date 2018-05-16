package exceptions;

@SuppressWarnings("serial")
public class ShouldNotBeCalledException extends RuntimeException {

	public ShouldNotBeCalledException() {
		super("This method should never be called!");
	}
}
