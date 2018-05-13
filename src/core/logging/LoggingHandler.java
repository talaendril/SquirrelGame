package core.logging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingHandler implements InvocationHandler {
	
	private Object inst;
	private Logger log;
	
	public LoggingHandler(Object inst, String loggerName) {
		this.inst = inst;
		this.log = Logger.getLogger(loggerName);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		StringBuilder s = new StringBuilder("");
		s.append(method.getName());
		s.append("(");
		
		if(args != null) {
			for(int i = 0; i < args.length; i++) {
				if(i > 0) {
					s.append(", ");
				}
				s.append(args[i].toString());
			}
		}
		s.append(")");
		
		this.log.log(Level.INFO, s.toString());
		try {
			return method.invoke(this.inst, args);
		} catch (Exception e) {
			this.log.severe("Something went wrong while trying to invoke the method");
			return null;
		}
	}
}