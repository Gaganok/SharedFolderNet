package shared.core;

import java.io.IOException;

import shared.server.Server;

public class ServerMain {

	public static void main(String[] args) {
		try {
			new Server();
		} catch (IOException e) {}
	}

}
