package shared.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;

public class SharedFolderRMI implements RemoteFolder{
	private static SharedFolderRMI instance = new SharedFolderRMI();
	private String sharedFolderPath = System.getProperty("user.dir") + "/src/resourses/shared";
	private File[] currentFiles;

	private SharedFolderRMI(){
		File file = new File(sharedFolderPath);

		if(!file.exists())
			file.mkdirs();

		System.out.println("Server Initialized");
	}

	public static SharedFolderRMI getInstance() {
		return instance;
	}
	
	public static RemoteFolder getRemoteFolder() {
		return instance;
	}

	private File[] getFiles() {
		return new File(getSharedPath())
				.listFiles((f, n) -> {return n.endsWith(".mp3");});
	}

	public String[] getFileNames() {
		currentFiles = getFiles();

		String[] names = new String[currentFiles.length];
		for(int i = 0; i < currentFiles.length; i++)
			names[i] = currentFiles[i].getName();

		return names;
	}

	public boolean isModified() {
		File[] newFiles = getFiles();
		if(newFiles.length == currentFiles.length) {
			for(int i = 0; i < newFiles.length; i++) {
				boolean ok = false;
				
				for(int j = 0; j < currentFiles.length; j++) {
					if(newFiles[i].getName().equals(currentFiles[j].getName()) && 
							newFiles[i].lastModified() == currentFiles[j].lastModified()) {
						ok = true;
						break;
					}
				}
				
				if(!ok)
					return true; 
			}
			return false;
		}
		return true;
	}

	public String getSharedPath() {
		return sharedFolderPath;
	}

	public File getFile(String fileName) {
		return new File(sharedFolderPath + "/" + fileName);
	}

	@Override
	public void importFile(FilePackage filePackage){
		try {
			BufferedOutputStream buffer = new BufferedOutputStream(
					new FileOutputStream(
							new File(SharedFolder.getInstance().getSharedPath() + "/" + filePackage.getFile().getName())));
			
			buffer.write(filePackage.getFileBytes());
			buffer.close();
		} catch (IOException e) {e.printStackTrace();} 
	}

	@Override
	public FilePackage getFilePackage(String fileName){
		try {
			File file = getFile(fileName);
			byte[] fileBytes = new byte[(int) file.length()];

			BufferedInputStream buffer = new BufferedInputStream(
					new FileInputStream(file));
			buffer.read(fileBytes, 0, fileBytes.length);
			buffer.close();

			return new FilePackage(fileBytes, file);
		} catch (IOException e) {e.printStackTrace();}

		return null;
	}
}
