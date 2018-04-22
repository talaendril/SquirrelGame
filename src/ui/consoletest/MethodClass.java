package ui.consoletest;

public class MethodClass {

	public void addIntegers(Object a, Object b) {
		String first = ((String) a).replaceAll("\\s+","");
		String second = ((String) b).replaceAll("\\s+","");
		System.out.println(Integer.parseInt(first) + Integer.parseInt(second));
	}
	
	public void addFloats(Object a, Object b) {
		String first = ((String) a).replaceAll("\\s+","");
		String second = ((String) b).replaceAll("\\s+","");
		System.out.println(Float.parseFloat(first) + Float.parseFloat(second));
	}
	
	public void exitSystem() {
		System.exit(0);
	}
	
	public void help() {
		StringBuilder sb = new StringBuilder("List of all Commands: \n");
		for(MyFavoriteCommandType gct : MyFavoriteCommandType.values()) {
			sb.append("\t" + gct.toString() + "\n");
		}
		System.out.println(sb.toString());
	}
	
	public void echo(Object times, Object string) {
		System.out.println((String) times);
		for (int j = 0; j < Integer.parseInt((String) times); j++) {
			System.out.println((String) string);
		}
	}
}
