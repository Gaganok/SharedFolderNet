package shared.core;

import java.io.IOException;

import shared.server.ServerSockets;

public class ServerMain {

	public static void main(String[] args) {
		try {
			new ServerSockets();
		} catch (IOException e) {}
	}

}
