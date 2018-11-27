package shared.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;

import shared.model.Client;
import shared.model.ClientMediaPlayer;
import shared.model.FilePackage;
import shared.model.RemoteFolder;

public class ClientRMI extends Thread implements Client{
/*	private final String ADDRESS = "localhost";
	private final int PORT = 7777;*/

	private Socket socket;
	private final RemoteFolder folder;

	public ClientRMI() throws UnknownHostException, IOException, NotBoundException{
		Registry registry = LocateRegistry.getRegistry(null);
		folder = (RemoteFolder) registry.lookup("sharedFolder");
		
		System.out.println(folder.getFileNames().toString());
	}

	public List<String> getList() throws IOException, ClassNotFoundException{
		return Arrays.asList(folder.getFileNames());
	}

	public void download(String fileName) throws IOException, ClassNotFoundException {
		BufferedOutputStream buffer = new BufferedOutputStream(
				new FileOutputStream(
						new File(ClientMediaPlayer.getInstance().getLocalPath() + "/" + fileName)));

		FilePackage filePackage = (FilePackage) folder.getFilePackage(fileName);

		buffer.write(filePackage.getFileBytes());
		buffer.close();

		System.out.println("File Downloaded");
	}

	public void importMedia(File file) {
		try {
			byte[] fileBytes = new byte[(int) file.length()];

			BufferedInputStream buffer = new BufferedInputStream(
					new FileInputStream(file));
			buffer.read(fileBytes, 0, fileBytes.length);
			buffer.close();

			folder.importFile(new FilePackage(fileBytes, file));
			
			System.out.println("File Imported");
		} catch (IOException e) {e.printStackTrace();}
	}

	public boolean isConnected() {
		return folder != null;
	}
}
