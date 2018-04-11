package core;

import entities.GoodPlant;
import entities.HandOperatedMasterSquirrel;
import idmanager.ID;
import location.XY;

public class Engine {

	public static void main(String[] args) {
		EntitySet es = new EntitySet();
		
		int x = 0, y = 0;
		
		es.addEntity(new GoodPlant(ID.getNewID(), new XY(x++, y--)));
		es.addEntity(new HandOperatedMasterSquirrel(ID.getNewID(), new XY(x++, y--)));
		
		System.out.println(es.toString());
		es.callNextStep();
		System.out.println(es.toString());
	}
}
