package Service;

import java.sql.SQLException;
import java.util.ArrayList;

import Model.Favorite;
import Model.FoodType;
import Model.Response;
import SQL.BaseDB;

public class FoodTypeService {
	private BaseDB db;
	
	public FoodTypeService()
	{
		try {
			this.db = new BaseDB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Response getAllFoodTypeData()
	{
		try {
			return new Response(true, "get food type successfully", db.<FoodType>read("SELECT * from FoodType", FoodType.class));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Response(false, e.getMessage());
		}
	}
	
	public Response getAllTypes()
	{
		ArrayList<String> types = new ArrayList<String>();
		try {
			ArrayList<FoodType> fts = db.<FoodType>read("SELECT * from FoodType", FoodType.class);
			for(FoodType ft : fts)
			{
				if(!types.contains(ft.type)) {
					types.add(ft.type);
				}	
			}
			
			return new Response(true, "get type successfully", types);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Response(false, e.getMessage());
		}
	}
}
