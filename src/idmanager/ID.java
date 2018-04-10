package idmanager;

public class ID {
	
	private static int idCounter = 0;
	
	public static int getNewID() {
		return idCounter++;
	}
}
