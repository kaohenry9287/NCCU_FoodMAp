package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import Model.Favorite;
import Model.FoodType;
import Model.Response;
import Service.FavoriteService;
import Service.FoodTypeService;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.Collation;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class FavoriteController implements Initializable{
	@FXML
	private ScrollPane foodTypeScroll;
	@FXML
	private VBox scrollVBox;
	
	@FXML
	private ScrollPane restaurantScroll;
	@FXML
	private VBox restaurantList;
	
	//*** VBox need to remove prefHeight in fxml
	
	private SceneController sceneController;
	private FoodTypeService foodTypeService;
	private FavoriteService favoriteService;
	
	private ArrayList<String> selectedTypes;
	private ArrayList<Favorite> favorites;
	
	public FavoriteController()
	{
		this.sceneController = new SceneController();
		this.foodTypeService = new FoodTypeService();
		this.favoriteService = new FavoriteService();
		this.selectedTypes = new ArrayList<String>();
		this.favorites = new ArrayList<Favorite>();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//set scroll pane properties
		foodTypeScroll.fitToWidthProperty().set(true);
		foodTypeScroll.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
		foodTypeScroll.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
		
		restaurantScroll.fitToWidthProperty().set(true);
		restaurantScroll.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
		restaurantScroll.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
		
		this.initListBtn();
		this.initList();
	}
	
	private void initListBtn()
	{
		Response res = this.foodTypeService.getAllTypes();
		if(!res.success) {
			return;
		}
		ArrayList<String> types = (ArrayList<String>)res.data;
		
		//based on food type array
		for(int i=0; i<types.size()+1; i++) {
			Button listBtn;
			if(i<types.size()) {
				listBtn = new Button(types.get(i));
			}else {
				listBtn = new Button("其他");
			}	
			
			listBtn.getStyleClass().add("list-btn");
			listBtn.setCursor(Cursor.HAND);
			listBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Button btn = ((Button)event.getSource());
					if(selectedTypes.contains(btn.getText())) {
						btn.getStyleClass().clear();
						btn.getStyleClass().add("list-btn");
						selectedTypes.remove(btn.getText());
					}else {
						btn.getStyleClass().add("active-background");
						selectedTypes.add(((Button)event.getSource()).getText());
					}	
				}
			});
			this.scrollVBox.getChildren().add(listBtn);
		}
	}
	
	private void initList()
	{
		Response res = this.favoriteService.getUserFavorite(SceneController.user);
		if(!res.success) {
			return;
		}
		this.favorites = (ArrayList<Favorite>)res.data;
		this.generateList(this.favorites);
	}
	
	private void generateList(ArrayList<Favorite> favors)
	{
		restaurantList.getChildren().clear();
		for(int i=0; i<favors.size(); i++)
		{
			Favorite fav = favors.get(i);
			Pane resItem = new Pane();
			HBox inner = new HBox();
			Label lab = new Label(String.valueOf(i+1));
			Label content = new Label(fav.restaurantName);
			ImageView cancel = new ImageView("/images/cancel.png");
			
			inner.setAlignment(Pos.CENTER);
			
			cancel.setCursor(Cursor.HAND);
			cancel.setFitWidth(40);
			cancel.setFitHeight(40);
			cancel.setX(500);
			cancel.setY(40-20);
			cancel.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Delete Favorite Place");
					alert.setContentText("Are you sure to delete this favorite place?");
					Optional<ButtonType> choice = alert.showAndWait();
					if(choice.get() != ButtonType.OK) {
						return;
					}
					Response res = favoriteService.deleteFavorite(SceneController.user, fav);
					alert.setContentText(res.message);
					alert.showAndWait();
					initList();
				}
			});
			
			resItem.getStyleClass().add("res-list-wrapper");
			lab.getStyleClass().add("res-list-num-lab");
			content.getStyleClass().add("res-list-cont");
			cancel.getStyleClass().add("cancel");
		
			if(i==0) {
				resItem.getStyleClass().add("res-list-wrapper-first");
				lab.getStyleClass().add("res-list-num-lab-first");
				content.getStyleClass().add("res-list-cont-first");
			}else if(i==favors.size()-1) {
				resItem.getStyleClass().add("res-list-wrapper-last");
				lab.getStyleClass().add("res-list-num-lab-last");
				content.getStyleClass().add("res-list-cont-last");
			}
			
			inner.getChildren().addAll(lab, content);	
			resItem.getChildren().addAll(inner, cancel);
			
			resItem.setCursor(Cursor.HAND);
			content.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					sceneController.switchToHomeScene(event, content.getText());
				}
			});
			restaurantList.getChildren().add(resItem);
		}
	}
	
	public void confirm()
	{
		if(this.selectedTypes.size()==0){
			this.generateList(this.favorites); //show all
			return;
		}
		
		ArrayList<Favorite> filter = new ArrayList<Favorite>();
		for(Favorite fav : this.favorites)
		{
			for(String type : this.selectedTypes)
			{
				if(fav.type.equals(type)) {
					filter.add(fav);
					break;
				}
			}
		}
		
		this.generateList(filter);
	}
	
	public void back(MouseEvent event)
	{
		this.sceneController.switchToHomeScene(event);
	}
}
