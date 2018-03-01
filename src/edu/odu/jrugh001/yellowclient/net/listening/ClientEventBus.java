package edu.odu.jrugh001.yellowclient.net.listening;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import edu.odu.jrugh001.yellowclient.ServerHandle;
import lombok.AllArgsConstructor;

public class ClientEventBus {
	
	@AllArgsConstructor
	private class CallbackData {
		private Listener listener;
		private Method method;
	}
	
	private Map<Character, CallbackData> listeners = new HashMap<>();
	
	public void registerListener(Listener listener) {
		for (Method method : listener.getClass().getMethods()) {
			for (Opcode opcodeAnno : method.getAnnotationsByType(Opcode.class)) {
				Class<?>[] params = method.getParameterTypes();
				String error = "Listener: " + listener;
				if (params.length != 1) throw new RuntimeException(error + " must have 1 parameter.");
				if (!params[0].equals(ServerHandle.class)) 
					throw new RuntimeException(error + " first parameter must be of type ServerHandle.");
				listeners.put(opcodeAnno.getOpcode(), new CallbackData(listener, method));
			}
		}
	}
	
	public void publish(char opcode, ServerHandle serverHandle) {		
		CallbackData callbackData = listeners.get(opcode);
		if (callbackData == null) {
			System.out.printf("callback data was null for %d", opcode);
			return;
		}
		try {
			callbackData.method.invoke(callbackData.listener, serverHandle);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
