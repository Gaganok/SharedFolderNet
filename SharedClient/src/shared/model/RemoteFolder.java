package shared.model;

import java.io.File;
import java.rmi.Remote;

public interface RemoteFolder extends Remote{
	String[] getFileNames();
	FilePackage getFilePackage(String fileName);
	void importFile(FilePackage filePackage);
}
