package shared.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import shared.client.ClientRMI;
import shared.core.PlayerMain;
import shared.interfaces.Monitorable;
import shared.model.Client;
import shared.model.ClientMediaPlayer;
import shared.model.Folder;
import shared.service.Monitor;

public class Controller implements Initializable, Monitorable{

	@FXML ListView<String> clientView;
	@FXML ListView<String> serverView;
	@FXML Label sharedFolderLabel;
	@FXML Pane mediaPane;
	@FXML Label connectionLabel;
	
	private ClientMediaPlayer player;
	private Client client;
	private Folder folder;

	public Controller(){
		player = ClientMediaPlayer.getInstance();
		folder = new Folder();
	}


	private void initSharedList() {
		updateSharedList();

		clientView.setOnMouseClicked((e) -> {
			if(player.isPlaying() && player.getCurrentPlaying().equals(
						clientView.getSelectionModel().getSelectedItem()))
				stop();
			else
				play();
		});
	}

	private void updateSharedList() {
		if(client.isConnected()) {
			
			try {
				if(folder.makeList(client.getList())) {
					Platform.runLater(() -> {
						clientView.getItems().clear();
						serverView.getItems().clear();

						clientView.getItems().addAll(folder.clientList());
						serverView.getItems().addAll(folder.serverList());
						
						connectionLabel.setText("Connected");
					});
				}
			} catch (Throwable e) {e.printStackTrace();}
		} else 
			Platform.runLater(() -> connectionLabel.setText("Connecting..."));
	}

	public void stop() {
		player.stop();
	}

	public void importMedia() {
		if(client.isConnected()) {
			FileChooser.ExtensionFilter filter 
			= new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.mp4");
			FileChooser fc = new FileChooser();
			fc.setSelectedExtensionFilter(filter);

			client.importMedia(fc.showOpenDialog(PlayerMain.getStage()));
		} else 
			System.out.println("No Server Connection!");

	}

	public void play() {
		String name = clientView.getSelectionModel().getSelectedItem();

		if(name != null) {
			player.play(name);
		} else
			System.out.println();
	}

	public void download() {
		if(client != null) {
			try {
				client.download(serverView.getSelectionModel().getSelectedItem());
				int index = serverView.getSelectionModel().getSelectedIndex();
				clientView.getItems().add((serverView.getItems().remove(index)));
			} catch (Throwable e) {e.printStackTrace();}	
		} else 
			System.out.println("No Server Connection!");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			//!!!!!!!!!!!!!!!!!!!!!!!! Change This To client RMI OR SOCKET implementation
			client = new ClientRMI();
		} catch (Throwable e) {e.printStackTrace();};
		
		initSharedList();
		new Monitor(this);
	}

	@Override
	public void monitor() {
		System.out.println("Monitor!!");
		updateSharedList();
	}
}
