package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import Model.Favorite;
import Model.RecommendPlace;
import Model.RecommendPlace.Period;
import Model.Response;
import Service.FavoriteService;
import Service.FoodTypeService;
import Service.HttpService;
import Service.RecommendService;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class RecommendController implements Initializable{
	private SceneController sceneController;
	private FoodTypeService foodTypeService;
	private RecommendService recommendService;
	private ArrayList<RecommendPlace> recommendPlaces;
	
	private ArrayList<String> selectedTypes;
	private ArrayList<RecommendPlace> recommend;
	private ArrayList<String[]> timeFilter;
	private HashMap<String, String[]> timeMap;
	
	@FXML
	private ScrollPane foodTypeScroll;
	@FXML
	private VBox scrollVBox;
	
	@FXML
	private ScrollPane restaurantScroll;
	@FXML
	private VBox restaurantList; 
	
	public RecommendController()
	{
		this.sceneController = new SceneController();
		this.foodTypeService = new FoodTypeService();
		this.selectedTypes = new ArrayList<String>();
		this.recommendService = new RecommendService();
		this.recommendPlaces = new ArrayList<RecommendPlace>();
		this.recommend = new ArrayList<RecommendPlace>();
		this.timeFilter = new ArrayList<String[]>();
		this.timeMap = new HashMap<String, String[]>();
		timeMap.put("Breakfast", new String[]{"0530", "1200"});
		timeMap.put("Lunch", new String[]{"1200", "1600"});
		timeMap.put("Dessert", new String[]{"1400", "1600"});
		timeMap.put("Dinner", new String[]{"1700", "2200"});
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
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
	
	public void initList()
	{
		Response res = this.recommendService.getFullRestaurants();
		if(res.success) {
			this.recommendPlaces = (ArrayList<RecommendPlace>)res.data;
			this.generateList(this.recommendPlaces);
		}
	}
	
	private void generateList(ArrayList<RecommendPlace> recPlaces)
	{
		restaurantList.getChildren().clear();
		for(int i=0; i<recPlaces.size(); i++)
		{
			RecommendPlace recPlace = recPlaces.get(i);
			Pane resItem = new Pane();
			HBox inner = new HBox();
			Label lab = new Label(String.valueOf(i+1));
			Label content = new Label(recPlace.name);
			
			inner.setAlignment(Pos.CENTER);
			
			resItem.getStyleClass().add("res-list-wrapper");
			lab.getStyleClass().add("res-list-num-lab");
			content.getStyleClass().add("res-list-cont");
		
			if(i==0) {
				resItem.getStyleClass().add("res-list-wrapper-first");
				lab.getStyleClass().add("res-list-num-lab-first");
				content.getStyleClass().add("res-list-cont-first");
			}else if(i==recPlaces.size()-1) {
				resItem.getStyleClass().add("res-list-wrapper-last");
				lab.getStyleClass().add("res-list-num-lab-last");
				content.getStyleClass().add("res-list-cont-last");
			}
			
			inner.getChildren().addAll(lab, content);	
			resItem.getChildren().addAll(inner);
			
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
		if(this.selectedTypes.size()==0 && this.timeFilter.size()==0) {
			this.generateList(this.recommendPlaces);
			return;
		}
		
		this.recommend.clear();
		
		boolean typeFlag = false;
		boolean timeFlag = false;
		Calendar calendar = Calendar.getInstance();
		int today = calendar.get(Calendar.DAY_OF_WEEK)-1;
		for(RecommendPlace place : this.recommendPlaces)
		{
			//every time focus on one single place
			typeFlag = false;
			timeFlag = false;
			
			//filter the type
			if(this.selectedTypes.size()>0) {
				for(String type : this.selectedTypes)
				{
					if(place.type.equals(type)) {
						typeFlag = true;
						if(this.timeFilter.size()==0) {
							this.recommend.add(place); //for only one condition
						}
						break;
					}
				}
			}
			
			//filter the time (兩個都有選且上面通過 或 只有選時間)
			//以今天星期為推薦依據！
			if((this.selectedTypes.size()>0 && this.timeFilter.size()>0 && typeFlag) || (this.selectedTypes.size()==0 && this.timeFilter.size()>0)) {
				for(String[] time : this.timeFilter)
				{
					//if only have one time filter, then the beg. and end. time difference should <=2hr
					//if have more than 2 filter, then the above one is accepted and if it cover more than one interval, it is qualified, too
					
					//period 可能多個 由小到大
					for(Period p : place.periods) 
					{
						if(p.close.openingHours.day==today) {

							if(Math.abs(Integer.parseInt(time[0])-Integer.parseInt(p.open.openingHours.time))<=200 && Math.abs(Integer.parseInt(time[1])-Integer.parseInt(p.close.openingHours.time))<=200) {
								timeFlag = true;
								this.recommend.add(place);
								
								break;
							}
						}
						
						if(!timeFlag && this.timeFilter.size()>=2) {
							if(Integer.parseInt(p.open.openingHours.time)<=Integer.parseInt(time[0]) && Integer.parseInt(time[1])<=Integer.parseInt(p.close.openingHours.time)) {
								timeFlag = true;
								this.recommend.add(place);
								
								break;
							}
						}
						
						if(p.close.openingHours.day > today){
							break; //已經超過今天的星期
						}
					}
			
					if(timeFlag) {
						break;
					}
				}
			}
			
		}
		
		this.generateList(this.recommend);
	}
	
	public void addTimeFilter(MouseEvent event)
	{
		CheckBox box = (CheckBox)event.getSource();
		
		if(box.isSelected()) {
			this.timeFilter.add(this.timeMap.get(box.getText()));
		}else {
			this.timeFilter.remove(this.timeMap.get(box.getText()));
		}
	}
	
	public void back(MouseEvent event)
	{
		this.sceneController.switchToHomeScene(event);
	}

}
