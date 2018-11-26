package shared.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shared.model.FilePackage;
import shared.model.SharedFolder;

public class ConnectionHandler extends Thread{

	private final Map<Integer, Runnable> operations = new HashMap<Integer, Runnable>();
	private final SharedFolder sharedFolder = SharedFolder.getInstance();

	private final Socket client;
	private final ObjectOutput out;
	private final ObjectInput in;

	public ConnectionHandler(Socket client) throws IOException {
		this.client = client;

		out = new ObjectOutputStream(client.getOutputStream());
		in = new ObjectInputStream(client.getInputStream());

		operationsInit();

		setDaemon(true);
		start();
	}

	//New Operation Added Here
	private void operationsInit() {
		operations.put(0, this::listMedia);
		operations.put(1, this::downloadMedia);
		operations.put(2, this::importMedia);
	}

	@Override
	public void run() {
		System.out.println("Client Connected");
		while(true) {
			try {
				operations.get(in.readInt()).run();
			} catch (IOException e1) { 
				try {
					out.close();
					in.close();
				} catch (IOException e) {}

				break;
			}
		}
	}


	private void testCase() {

		try {
			out.writeInt(5);
			out.flush();


			System.out.println();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void listMedia() {
		List<String> list = Arrays.asList(sharedFolder.getFileNames());
		try {
			out.writeObject(list);
		} catch (IOException e) {e.printStackTrace();}
	}

	private void downloadMedia() {
		try {
			String name = (String) in.readObject();
			File file = sharedFolder.getFile(name);
			byte[] fileBytes = new byte[(int) file.length()];

			BufferedInputStream buffer = new BufferedInputStream(
					new FileInputStream(file));
			buffer.read(fileBytes, 0, fileBytes.length);
			buffer.close();


			out.writeObject(new FilePackage(fileBytes, file));
			out.flush();

		} catch (IOException | ClassNotFoundException e) {e.printStackTrace();}

		System.out.println("Download");
	}

	private void importMedia() {
		try {
			FilePackage filePackage = (FilePackage) in.readObject();
			BufferedOutputStream buffer = new BufferedOutputStream(
					new FileOutputStream(
							new File(SharedFolder.getInstance().getSharedPath() + "/" + filePackage.getFile().getName())));
			
			buffer.write(filePackage.getFileBytes());
			buffer.close();
		} catch (IOException | ClassNotFoundException e) {e.printStackTrace();} 
	}

}
