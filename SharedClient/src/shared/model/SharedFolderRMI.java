package shared.model;

import java.io.File;
import java.rmi.Remote;

public class SharedFolderRMI implements Remote{
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
	
	public static Remote getRemoteInstance() {
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
}
