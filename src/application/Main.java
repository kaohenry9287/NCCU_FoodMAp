package application;

import java.sql.ResultSet;

import SQL.BaseDB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {			
			Parent root = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
			Scene scene = new Scene(root);
			String css = this.getClass().getResource("/View/Login.css").toExternalForm();
			scene.getStylesheets().add(css);
			
			primaryStage.getIcons().add(new Image(getClass().getResource("/images/LOGO.png").toExternalForm()));
			primaryStage.setTitle("政大吃貨");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
