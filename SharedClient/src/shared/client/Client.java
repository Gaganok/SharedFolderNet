package shared.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.media.Media;
import shared.model.ClientMediaPlayer;
import shared.model.FilePackage;

public class Client extends Thread{
	// Operations: 0 = List, 1 = Download, 2 = Import
	private final String ADDRESS = "localhost";
	private final int PORT = 7777;

	private Socket socket;
	private ObjectOutput out;
	private ObjectInput in;

	public Client() throws UnknownHostException, IOException{
		new Thread(() -> {
			do {
				try {
					Thread.sleep(1000);
					socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
					
					out = new ObjectOutputStream(socket.getOutputStream());
					in = new ObjectInputStream(socket.getInputStream());
				}catch(Throwable e) {}
			} while(socket == null);
		}).start();
	}

	public List<String> getList() throws IOException, ClassNotFoundException{
		out.writeInt(0);
		out.flush();

		return (List) in.readObject();
	}

	public void download(String fileName) throws IOException, ClassNotFoundException {
		out.writeInt(1);
		out.flush();

		out.writeObject(fileName);
		out.flush();

		BufferedOutputStream buffer = new BufferedOutputStream(
				new FileOutputStream(
						new File(ClientMediaPlayer.getInstance().getLocalPath() + "/" + fileName)));

		FilePackage filePackage = (FilePackage) in.readObject();

		buffer.write(filePackage.getFileBytes());
		buffer.close();

		System.out.println("File Downloaded");
	}

	public void importMedia(File file) {
		try {
			out.writeInt(2);

			byte[] fileBytes = new byte[(int) file.length()];

			BufferedInputStream buffer = new BufferedInputStream(
					new FileInputStream(file));
			buffer.read(fileBytes, 0, fileBytes.length);
			buffer.close();

			out.writeObject(new FilePackage(fileBytes, file));
			out.flush();

			System.out.println("File Imported");
		} catch (IOException e) {e.printStackTrace();}
	}

	@Override
	public void finalize() {
		try {
			out.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		return socket != null;
	}
}
