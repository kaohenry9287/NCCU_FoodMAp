package Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.mysql.cj.conf.ConnectionUrl.Type;

import Model.Favorite;
import Model.Response;
import Model.User;
import Service.FavoriteService;
import Service.HttpService;
import Service.UserService;
import Utility.JsonMapper;
import Utility.MyFileWriter;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class HomeController implements Initializable{
	//constructor -> fxml annotation -> initialize method
	private SceneController sceneController;
	private User user;
	private HttpService httpService;
	private MyFileWriter writer;
	private JsonMapper jsonMapper;
	private FavoriteService favoriteService;
	private String selectedRestaurant;
	private UserService userService;
	
	@FXML
	private ImageView userImage;
	@FXML
	private Text userName;
	@FXML
	private WebView map;
	@FXML
	private TextField searchField;
	@FXML
	private TextField placeField;
	@FXML
	private Pane sideBar;
	
	public HomeController()
	{
		//before initialize sceneController
		this.selectedRestaurant = SceneController.restaurant;
		
		this.sceneController = new SceneController();
		this.user = SceneController.user;	
		this.httpService = new HttpService();
		this.writer = new MyFileWriter();
		this.jsonMapper = new JsonMapper();
		this.favoriteService = new FavoriteService();
		this.userService = new UserService();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		this.initUserInfo();
		this.initMap();
		
		if(!this.selectedRestaurant.equals("")) {
			this.searchField.setText(this.selectedRestaurant);
			this.search();
		}
	}
	
	//init user image, name, favorite...
	private void initUserInfo()
	{
		File file = new File(this.user.pic);
		Image img = null;
		try {
			img = new Image(file.toURI().toURL().toExternalForm());
			this.userImage.setImage(img);
			this.userName.setText(user.userName);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initMap()
	{
		this.loadWebEngine(this.httpService.sendGetRequest("map").data.toString());
	}
	
	public void search()
	{
		String text = this.searchField.getText().strip();
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Search Place");
		if(text.equals("")) {
			alert.setContentText("Pleace fill valid place name");
			alert.showAndWait();
			return;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("place", text);
		
		Response res = this.httpService.sendPostRequest("place", map);
		writer.writeHTMLFile(res.data.toString()); //write to file, but need to refresh
		this.loadWebEngine(res.data.toString());
	}
	
	public void addFavoritePlace()
	{
		String placeId = this.placeField.getText().strip();
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Add Favorite Place");
		if(placeId.equals("")) {
			alert.setContentText("Pleace fill valid place ID");
			alert.showAndWait();
			return;
		}
		
		Response res = httpService.sendGetRequest("place", "/"+placeId);
		
		//use json simple to parse json
		try {
			Object ob = new JSONParser().parse(res.data.toString());
			JSONObject json = (JSONObject)ob;
			
			Favorite fav = this.favoriteService.jsonObjectToFavorite(json);
			Response favRes = this.favoriteService.createFavorite(fav); //will judge added or not
			if(favRes.success==false) {
				alert.setContentText(favRes.message);
				alert.show();
				return;
			}
			
			this.favoriteService.createUserFavorite(SceneController.user, fav);
			
			alert.setAlertType(AlertType.CONFIRMATION);
			alert.setContentText("Add favorite place successfully!");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			alert.setContentText("Somthing wrong when insert favorite place!");
			e.printStackTrace();
		}
		
		alert.show();
	}
	
	public void clickFavorite(MouseEvent event)
	{
		this.sceneController.switchToFavoriteScene(event);
	}
	
	public void clickRecommend(MouseEvent event)
	{
		this.sceneController.switchToRecommendScene(event);
	}
	
	public void loadWebEngine(String html)
	{
    	Document doc = Jsoup.parse(html, "UTF-8");
        WebEngine webEngine = this.map.getEngine();
        webEngine.loadContent(doc.html());
        
//		InputStream inputStream = getClass().getResourceAsStream("../HTML/index.html");
        //Parse the content
//        try {
//			Document doc = Jsoup.parse(inputStream, "UTF-8", "");
			//use string html to reload or file won't refresh content
//        	Document doc = Jsoup.parse(html, "UTF-8");
//	        WebEngine webEngine = this.map.getEngine();
//	        //Load the new content after make changing
//	        webEngine.loadContent(doc.html());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public void changeName(MouseEvent event)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Change User Name");

		Text oldName = (Text)event.getSource();
		TextField modifiedName = new TextField(oldName.getText());
		modifiedName.getStyleClass().add("modify-name");
		modifiedName.setLayoutX(100);
		modifiedName.setLayoutY(29);
		this.sideBar.getChildren().remove(oldName);
		this.sideBar.getChildren().add(modifiedName);
		
		modifiedName.setOnKeyPressed(new EventHandler<KeyEvent>() {
		    @Override
		    public void handle(KeyEvent ke) {
		        if (ke.getCode().equals(KeyCode.ENTER)) {
		            SceneController.user.userName = modifiedName.getText();
		            Response res = userService.updateUser(user);
		            alert.setContentText(res.message);
		            if(!res.success) {
		            	alert.setAlertType(AlertType.ERROR);
		            }
		            oldName.setText(SceneController.user.userName);
		            sideBar.getChildren().remove(modifiedName);
		            sideBar.getChildren().add(oldName);
		            alert.show();
		            
		        }
		    }
		});
	}
	
	public void changePic(MouseEvent event)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Change User Photo");
		
		File img = this.selectFile(event);
		String imagePath = RegisterController.class.getProtectionDomain().getCodeSource().getLocation().getPath()+"/images/user-images/"+img.getName();
		try {
			File oldFile = new File(SceneController.user.pic);
			if(oldFile.delete()) {
				Files.copy(img.toPath(), new FileOutputStream(imagePath));
				SceneController.user.pic = imagePath;
				Response res = this.userService.updateUser(SceneController.user);
				alert.setContentText(res.message);
				if(!res.success) {
					alert.setAlertType(AlertType.ERROR);
				}else {
					Image newImg = new Image(img.toURI().toURL().toExternalForm());
					this.userImage.setImage(newImg);
				}
				alert.show();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			alert.setContentText(e.getMessage());
			alert.show();
		}
		
	}
	
	private File selectFile(Event event)
	{
		FileChooser chooser = new FileChooser();
		ExtensionFilter filter = new ExtensionFilter("Image Files(*.jpg|*.png)", "*jpg", "*.png");
		chooser.getExtensionFilters().add(filter);
		
		chooser.setTitle("Select Image");
		File img = chooser.showOpenDialog((Stage)((Node)event.getSource()).getScene().getWindow());
		return img;
	}
}
