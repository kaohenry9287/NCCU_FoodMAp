package Utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import Model.Response;

public class MyFileWriter {
	private static int changeCode;
	private static String path;
	
	public MyFileWriter()
	{
		changeCode = 0;
		path = "src/HTML/index.html";
	}
	
	public Response writeHTMLFile(String text)
	{
		File file = null;
		BufferedWriter writer = null;
		try {
		    file = new File(path); //need to hit fn+f5 to refresh project to see the file
		    writer = new BufferedWriter(new FileWriter(file));
		    writer.write(text);
		    writer.newLine(); //this is not actually needed for html files - can make your code more readable though 
		    writer.flush();
		    writer.close(); //make sure you close the writer object 
		    return new Response(true, "HTML File build successfully!", path);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(false, e.getMessage());
		}
	}
	
	public static String getPath()
	{
		return path;
	}
}
