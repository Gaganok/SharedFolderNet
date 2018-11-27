package shared.model;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface Client {
	void importMedia(File file);
	boolean isConnected();
	void download(String fileName) throws IOException, ClassNotFoundException;
	List<String> getList() throws IOException, ClassNotFoundException;
}
