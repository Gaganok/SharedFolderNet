package shared.core;

import java.nio.file.Paths;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shared.client.ClientRMI;

public class PlayerMain extends Application{

	private static Stage primaryStage;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		PlayerMain.primaryStage = primaryStage;
		Parent root = FXMLLoader.load(Paths.get(System.getProperty("user.dir") + "/src/resourses/fxml/view.fxml")
				.toUri().toURL());
		
		primaryStage.setScene(new Scene(root, 1280, 800));
		primaryStage.setResizable(false);
		primaryStage.show();
		
	}
	
	public static Stage getStage() {
		return primaryStage;
	}
}
