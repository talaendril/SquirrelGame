package core;

public class EntitySet {
	
	private static EntitySet instance = null;
	
	private EntitySet() {
		//singleton class
	}
	
	public static EntitySet getInstance() {
		if(instance == null) {
			instance = new EntitySet();
		}
		return instance;
	}
}
