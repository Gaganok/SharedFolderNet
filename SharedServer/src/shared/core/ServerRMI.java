package shared.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.function.BooleanSupplier;

import shared.model.RemoteFolder;
import shared.model.SharedFolderRMI;

public class ServerRMI {

/*	private final ServerSocket server;
	private Map<Integer, BooleanSupplier> map;

	public ServerRMI() throws IOException {
		server = new ServerSocket(7777, 10);
		
		

	}
*/
	public static void main(String args[]) { 
		try {
			RemoteFolder folder = (RemoteFolder) UnicastRemoteObject.exportObject(SharedFolderRMI.getRemoteFolder(), 0);
			LocateRegistry.getRegistry().bind("sharedFolder", folder);
		} catch (AlreadyBoundException | RemoteException e) {e.printStackTrace();}
	}
}
