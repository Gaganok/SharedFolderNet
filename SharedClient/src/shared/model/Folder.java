package shared.model;

import java.util.ArrayList;
import java.util.List;

public class Folder {

	private ArrayList<String> onClientList = new ArrayList<String>();
	private ArrayList<String> onServerList = new ArrayList<String>();

	public Folder() {}

	public boolean makeList(List<String> l) {
		ArrayList<String> clientList = new ArrayList<String>();
		ArrayList<String> serverList = new ArrayList<String>();

		l.forEach(e -> {
			if(ClientMediaPlayer.getInstance().exist(e)) 
				clientList.add(e);
			else
				serverList.add(e);
		});
		
		boolean modified = !(clientList.size() == onClientList.size() &&
				clientList.containsAll(onClientList) &&
				serverList.size() == onServerList.size() &&
				serverList.containsAll(onServerList));
		
		onClientList = clientList;
		onServerList = serverList;
		
		return modified;
	}
	
	public ArrayList<String> clientList() {
		return onClientList;
	}
	
	public ArrayList<String> serverList() {
		return onServerList;
	}
}
