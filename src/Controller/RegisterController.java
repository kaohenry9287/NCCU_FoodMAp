package Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Model.Response;
import Model.User;
import Service.AuthenticationService;
import Service.SendEmailService;
import Service.UserService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class RegisterController {
	UserService userService;
	AuthenticationService authenticationService;
	SendEmailService sendEmailService;
	File image;
	SceneController sceneController;
	
	@FXML
	private TextField nameField;
	
	@FXML
	private TextField accountField;

	@FXML
	private TextField passwordField;
	
	public RegisterController()
	{
		this.userService = new UserService();
		this.authenticationService = new AuthenticationService();
		this.image = null;
		this.sceneController = new SceneController();
	}
	
	public void chooseImage(MouseEvent event)
	{
		this.selectFile(event);
	}
	
	public void confirmRegister(MouseEvent event)
	{
		Alert alert = null;
		alert = new Alert(AlertType.ERROR);
		alert.setTitle("Register Error");
		
		String name = this.nameField.getText().trim();
		String account = this.accountField.getText().trim();
		String password = this.passwordField.getText().trim();
		
		//check is empty field
		if(name.equals("") || account.equals("") || password.equals("") || this.image == null)
		{
			alert.setContentText("Please fill all field to register!");
			alert.show();
			return;
		}
		
		//check is valid email format
		Response regiRes = null;
		regiRes = this.authenticationService.checkValidEmailFormat(account);
		if(!regiRes.success)
		{
			alert.setContentText(regiRes.message);
			alert.show();
			return;
		}	
		//check is valid password format
		regiRes = this.authenticationService.checkValidPasswordFormat(password);
		if(!regiRes.success)
		{
			alert.setContentText(regiRes.message);
			alert.show();
			return;
		}
		
		//check is valid authentication code from email
		this.authenticationService.initSendEmailService(account);
		SendEmailService service = AuthenticationService.getSendEmailService();
		alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Verification Email");
		alert.setContentText("Please check out the verification email in "+account);
		alert.showAndWait();
		Response sendRes = service.sendVerificationEmail();
		
		if(sendRes.success) {
			Optional<String> result = this.showverificationDialog();
			result.ifPresent(code ->{
				Response veriRes = service.verify(account, code);
				
				Alert a = null;
				if(veriRes.success) {
					User user = new User();
					user.userName = name;
					user.account = account;
					user.password = password;
					String imagePath = "";
					if(this.image!=null) {
						imagePath = RegisterController.class.getProtectionDomain().getCodeSource().getLocation().getPath()+"/images/user-images/"+image.getName();
						//imagePath = "/Users/maoxunhuang/eclipse-workspace/FinalProject/src/images/user-images/"+image.getName();
					}
					user.pic = imagePath;
					Response res = userService.register(user);
					
					if(!res.success) {
						a = new Alert(AlertType.ERROR);
						a.setTitle("Register Error");
					}else {
						try {
							Files.copy(this.image.toPath(), new FileOutputStream(imagePath));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						a = new Alert(AlertType.INFORMATION);
						a.setTitle("Register Success");
					}
					a.setContentText(res.message);
					a.show();
				}else {
					a = new Alert(AlertType.ERROR);
					a.setTitle("Verification");
					a.setContentText(veriRes.message);
					return;
				}
			});
		}else {
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Verification Email");
			alert.setContentText(sendRes.message);
			return;
		}
	}
	
	public void back(MouseEvent event)
	{
		this.sceneController.switchToLoginScene(event);
	}
	
	private void selectFile(Event event)
	{
		FileChooser chooser = new FileChooser();
		ExtensionFilter filter = new ExtensionFilter("Image Files(*.jpg|*.png)", "*jpg", "*.png");
		chooser.getExtensionFilters().add(filter);
		
		chooser.setTitle("Select Image");
		this.image = chooser.showOpenDialog((Stage)((Node)event.getSource()).getScene().getWindow());
	}
	
	private Optional<String> showverificationDialog()
	{
		TextInputDialog td = new TextInputDialog();
        td.setHeaderText("Please input the verification code");
        
        //wait until dialog be closed
        Optional<String> result = td.showAndWait();
        
        return result;
	}
}
