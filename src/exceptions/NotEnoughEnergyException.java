package exceptions;

@SuppressWarnings("serial")
public class NotEnoughEnergyException extends Exception {

	public NotEnoughEnergyException() {
		super("Entity doesn't have enough energy to perform this task");
	}
}
