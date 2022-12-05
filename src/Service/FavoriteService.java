package Service;

import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import Controller.SceneController;
import Model.Favorite;
import Model.FoodType;
import Model.Response;
import Model.User;
import Model.User_Favorite;
import SQL.BaseDB;

public class FavoriteService {
	BaseDB db;
	
	public FavoriteService()
	{
		try {
			this.db = new BaseDB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Response getUserFavorite(User user)
	{
		try {
			ArrayList<Favorite> favors = db.<Favorite>read(String.format(""
					+ "SELECT Favorite.*"
					+ " FROM Favorite, `User`, User_Favorite"
					+ " WHERE `User`.user_id=%d and User_Favorite.user_id  = `User`.user_id and User_Favorite.favor_id = Favorite.favor_id;"
					,user.user_id), Favorite.class);
			return new Response(true, "select favorite sccessfully!", favors);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Response(false, e.getMessage());
		}
	}
	
	public ArrayList<Favorite> getFavorite(String sql)
	{
		try {
			return db.<Favorite>read(sql, Favorite.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<Favorite>(); //length==0
		}
	}
	
	public Response createFavorite(Favorite fav)
	{	
		try {
			boolean checkAdded = this.isUserAdded(fav, SceneController.user);
			if(checkAdded) {
				return new Response(false, "You have already added this place");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return new Response(false, "Error Occured");
		}
		
		boolean isSuccess=false;
		String mes="";
		try {
			ArrayList<Favorite> checkFavExist = db.<Favorite>read(String.format("SELECT * from Favorite where favor_id='%s';", fav.favor_id), Favorite.class);
			if(checkFavExist.size()<=0) {
				isSuccess = db.<Favorite>create(fav, Favorite.class);
			}else {
				isSuccess = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mes = isSuccess?"insert favorite successfully!":"something wrong when insert into Favorite";
		return new Response(isSuccess, mes);	
	}
	
	public Response createUserFavorite(User user, Favorite fav)
	{
		User_Favorite uf = new User_Favorite();
		uf.user_id = user.user_id;
		uf.favor_id = fav.favor_id;
		boolean isSuccess=false;
		String mes="";
		 try {
			isSuccess = db.<User_Favorite>create(uf, User_Favorite.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mes = isSuccess?"insert User_Favorite successfully!":"something wrong when insert into User_Favorite";
		return new Response(isSuccess, mes);
	}
	
	public Response deleteFavorite(User user, Favorite fav)
	{
		try {
			//must have value
			User_Favorite uf = db.<User_Favorite>read(String.format("SELECT * from User_Favorite where user_id=%d and favor_id='%s'", user.user_id, fav.favor_id), User_Favorite.class).get(0);
			db.<User_Favorite>delete(uf, User_Favorite.class);
			db.<Favorite>delete(fav, Favorite.class);
			
			return new Response(true, "remove favorite place successfully!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Response(false, "somthing wrong when removing favorite place");
		}
	}
	
	public boolean isUserAdded(Favorite fav, User user) throws Exception
	{
		String sql = String.format("SELECT * from Favorite f, `User` u, User_Favorite uf "
				+ "where uf.favor_id='%s' "
				+ "and uf.user_id=%d and "
				+ "uf.favor_id=f.favor_id and "
				+ "uf.user_id=u.user_id;", fav.favor_id, SceneController.user.user_id);
				
		ArrayList<Favorite> checkUserAdded = db.<Favorite>read(sql, Favorite.class);
		
		if(checkUserAdded.size()>0) {
			return true;
		}
		
		return false;
	}
	
	public Favorite jsonObjectToFavorite(JSONObject json)
	{
		Favorite fav = new Favorite();
		fav.favor_id = ((JSONObject)json.get("data")).get("id").toString();
		fav.googleLocationId = ((JSONObject)json.get("data")).get("id").toString();
		fav.restaurantName = ((JSONObject)json.get("data")).get("name").toString();
		fav.rate = Float.parseFloat(((JSONObject)json.get("data")).get("rating").toString());
		fav.type = this.getFoodType(fav.favor_id);
		
		return fav;
	}
	
	private String getFoodType(String id)
	{
		try {
		  	ArrayList<FoodType> fts = db.<FoodType>read(String.format("SELECT * from FoodType where googleLocationId='%s'", id), FoodType.class);
		  	if(fts.size()==0) {
		  		return "其他";
		  	}
		  	
			return fts.get(0).type;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
