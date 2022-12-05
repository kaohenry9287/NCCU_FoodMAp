package Controller;

import java.io.IOException;

import Model.User;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SceneController {
	
	//the login user
	public static User user;
	
	//the selected restaurant
	public static String restaurant;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	private String scenePath;
	private String cssPath;
	
	public SceneController()
	{
		this.restaurant = "";
	}
	
	public void switchToLoginScene(Event event)
	{
		this.scenePath = "/View/Login.fxml";
		this.cssPath = "/View/Login.css";
		this.switchScene(event);
	}
	
	public void switchToRegisterScene(Event event)
	{
		this.scenePath = "/View/Register.fxml";
		this.cssPath = "/View/Register.css";
		this.switchScene(event);
	}
	
	//Overloading
	public void switchToHomeScene(Event event, User user) 
	{
		this.user = user;
		this.scenePath = "/View/Home.fxml";
		this.cssPath = "/View/Home.css";
		this.switchScene(event);
	}
	public void switchToHomeScene(Event event, String restaurant) 
	{
		this.restaurant = restaurant;
		this.scenePath = "/View/Home.fxml";
		this.cssPath = "/View/Home.css";
		this.switchScene(event);
	}
	public void switchToHomeScene(Event event) 
	{
		this.scenePath = "/View/Home.fxml";
		this.cssPath = "/View/Home.css";
		this.switchScene(event);
	}
	
	public void switchToFavoriteScene(Event event)
	{
		this.scenePath = "/View/Favorite.fxml";
		this.cssPath = "/View/Favorite.css";
		this.switchScene(event);
	}
	
	public void switchToRecommendScene(Event event)
	{
		this.scenePath = "/View/Recommend.fxml";
		this.cssPath = "/View/Recommend.css";
		this.switchScene(event);
	}
	
	private void switchScene(Event event)
	{
		try {
			this.root = FXMLLoader.load(getClass().getResource(scenePath));
			this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			this.scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource(this.cssPath).toExternalForm());
			this.stage.setScene(scene);
			this.stage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
