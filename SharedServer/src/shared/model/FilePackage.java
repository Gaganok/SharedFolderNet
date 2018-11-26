package shared.model;

import java.io.File;
import java.io.Serializable;

public class FilePackage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] fileBytes;
	private File file;
	
	public FilePackage(byte[] fileBytes, File file){
		this.fileBytes = fileBytes;
		this.file = file;
	}

	public byte[] getFileBytes() {
		return fileBytes;
	}

	public File getFile() {
		return file;
	}
	
}
