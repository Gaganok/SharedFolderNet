package shared.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class ServerSockets {

	private final ServerSocket server;
	private Map<Integer, BooleanSupplier> map;

	public ServerSockets() throws IOException {
		server = new ServerSocket(7777, 10);
		
		while(true) 
			new ConnectionHandler(server.accept());
	}
}
