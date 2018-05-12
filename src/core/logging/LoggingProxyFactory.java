package core.logging;

import java.lang.reflect.Proxy;

public class LoggingProxyFactory {

	public static final Object create(Object impl, String loggerName) {
		LoggingHandler handler = new LoggingHandler(impl, loggerName);
		return Proxy.newProxyInstance(impl.getClass().getClassLoader(), impl.getClass().getInterfaces(), handler);
	}
}