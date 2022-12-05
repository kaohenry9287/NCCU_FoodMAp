package Model;

import java.util.ArrayList;

public class RecommendPlace {
	public String id;
	public String name;
	public ArrayList<Period> periods;
	public String type;
	
	public RecommendPlace()
	{
		this.periods = new ArrayList<Period>();
	}
	
	public class Period
	{
		public CloseOpen close;
		public CloseOpen open;
	}
	
	public class CloseOpen
	{
		public String openState;
		public OpeningHours openingHours;
		public CloseOpen()
		{
			this.openingHours = new OpeningHours();
		}
	}

	public class OpeningHours
	{
		public int day;
		public String time;
	}
}
