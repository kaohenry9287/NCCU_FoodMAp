package Service;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Model.RecommendPlace;
import Model.RecommendPlace.CloseOpen;
import Model.RecommendPlace.Period;
import Model.Response;

public class RecommendService {
	private HttpService httpService;
	
	public RecommendService()
	{
		this.httpService = new HttpService();
	}
	
	public Response getFullRestaurants()
	{
		Response res = this.httpService.sendGetRequest("full-restaurants");
		ArrayList<RecommendPlace> results = new ArrayList<RecommendPlace>();
		if(res.success) {
			Object ob;
			try {
				ob = new JSONParser().parse(res.data.toString());
				JSONObject json = (JSONObject)ob;

				JSONArray datas = (JSONArray)json.get("data");
				Iterator i = datas.iterator();
				
				while(i.hasNext())
				{
					RecommendPlace recPlace = new RecommendPlace();
					JSONObject place = (JSONObject)i.next();
					
					recPlace.id = place.get("id").toString();
					recPlace.name = place.get("name").toString();
					recPlace.type = place.get("type").toString();
					
					JSONArray openingHours = (JSONArray)place.get("opening_hours");
					Iterator j = openingHours.iterator();
					while(j.hasNext())
					{
						RecommendPlace.Period period =  recPlace.new Period();
						RecommendPlace.CloseOpen close = recPlace.new CloseOpen();
						RecommendPlace.CloseOpen open = recPlace.new CloseOpen();
						
						JSONObject hour = (JSONObject)j.next();
					
						close.openingHours.day = Integer.parseInt(((JSONObject)hour.get("close")).get("day").toString());
						close.openingHours.time = ((JSONObject)hour.get("close")).get("time").toString();
						
						open.openingHours.day = Integer.parseInt(((JSONObject)hour.get("open")).get("day").toString());
						open.openingHours.time = ((JSONObject)hour.get("open")).get("time").toString();
						
						period.close = close;
						period.open = open;
						
						recPlace.periods.add(period);	
					}
					results.add(recPlace);
				}
				return new Response(true, "get full restaurants successfully!", results);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new Response(false, "Something wrong when parsing json");
			}
		}else{
			return new Response(false, "Something wrong when get full restaurants");
		}
	}
}
