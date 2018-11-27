package shared.model;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteFolder extends Remote{
	String[] getFileNames()throws RemoteException;
	FilePackage getFilePackage(String fileName) throws RemoteException;
	void importFile(FilePackage filePackage)throws RemoteException;
}
