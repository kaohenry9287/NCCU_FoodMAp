package Controller;

import java.sql.SQLException;

import Model.Response;
import Model.User;
import SQL.BaseDB;
import Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginController {
	private UserService userService;
	private SceneController sceneController;
	
	@FXML
	private TextField accountTextField;
	
	@FXML
	private TextField passwordTextField;
	
	public LoginController()
	{
		this.userService = new UserService();
		this.sceneController = new SceneController();
	}
	
	public void login(MouseEvent event)
	{
		String account = this.accountTextField.getText().trim();
		String pasword = this.passwordTextField.getText().trim();
		
		Response res = this.userService.login(account, pasword);
		
		if(res.success) {
			//change scene and get data
			this.sceneController.switchToHomeScene(event, (User)res.data);
		}else if(!res.success){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Log in error");
			alert.setContentText(res.message);
			alert.show();
		}
	}
	
	public void register(MouseEvent event)
	{
		this.sceneController.switchToRegisterScene(event);
	}
}
