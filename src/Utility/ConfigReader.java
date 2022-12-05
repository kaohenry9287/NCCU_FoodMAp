package Utility;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class ConfigReader {
	public HashMap<String, String> readFile(String fileName) throws FileNotFoundException {
		File doc = new File(fileName);	          
		Scanner sc = new Scanner(doc);
		HashMap<String, String> map = new HashMap<String, String>();
		
		while(sc.hasNextLine())
		{
			String[] info = sc.nextLine().split(",");
			map.put(info[0], info[1]);
		}
		      
		return map;
	}
	
	
}
