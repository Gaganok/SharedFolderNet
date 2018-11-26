package shared.model;


import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;

public class ClientMediaPlayer{
	private static ClientMediaPlayer instance = new ClientMediaPlayer();
	
	private MediaPlayer player;
	public MediaView mediaView;
	
	private String localFolderPath;
	private String currentMedia;
	
	private ClientMediaPlayer() {	
			currentMedia  = "/";
			localFolderPath = System.getProperty("user.dir") + "/src/resourses/local";
			
			File file = new File(localFolderPath);
			
			if(!file.exists())
				file.mkdirs();
			
			System.out.println("Client Initialized");
	}

	public static ClientMediaPlayer getInstance() {
		return instance;
	}

	public String getLocalPath() {
		return localFolderPath;
	}

	public void setLocalPath(String path) {
		localFolderPath = path;
	}

	public void play(String name) {
		if(currentMedia.equals(name)) 
			player.play();
		else {
			if(player != null)
				player.stop();
			
			player = new MediaPlayer(new Media(
						new File(localFolderPath + "/" + name)
							.toURI().toString()));
			
			currentMedia = name;
			player.play();
		}
	}

	public void stop() {
		//player.stop();
		player.pause();
	}
	
	public boolean exist(String name) {
		return new File(getLocalPath() + "/" + name).exists();
	}
	
	public boolean isPlaying() {
		if(player == null)
			return false;
	
		return player.getStatus().equals(Status.PLAYING);
	}
	
	public String getCurrentPlaying() {
		return currentMedia;
	}
}
