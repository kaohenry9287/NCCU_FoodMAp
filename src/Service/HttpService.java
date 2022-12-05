package Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import Model.Response;
import Utility.ConfigReader;
import Utility.JsonMapper;

public class HttpService {
	//https://www.baeldung.com/java-9-http-client
	
	private ConfigReader reader;
	private String baseURI;
	private HashMap<String, String> uriMap;
	private JsonMapper jsonMapper;
	
	public HttpService()
	{
		this.reader = new ConfigReader();
		try {
			this.uriMap = this.reader.readFile("resource/HttpConfig.txt");
			this.baseURI = this.uriMap.get("baseURI");
			this.jsonMapper = new JsonMapper();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Response sendGetRequest(String route)
	{
		HttpRequest request;
		try {
			request = HttpRequest.newBuilder()
					  .uri(new URI(this.baseURI+this.uriMap.get(route)))
					  .GET()
					  .build();
			HttpClient client = HttpClient.newHttpClient();

			// use the client to send the request
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

			// the response:
			//System.out.println(response.body().toString());
			return new Response(true, "send request successfully!", response.body().toString());
			
		} catch(Exception e) {
			return new Response(false, e.getMessage());
		}
	}
	public Response sendGetRequest(String route, String params)
	{
		HttpRequest request;
		try {
			request = HttpRequest.newBuilder()
					  .uri(new URI(this.baseURI+this.uriMap.get(route)+params))
					  .GET()
					  .build();
			HttpClient client = HttpClient.newHttpClient();

			// use the client to send the request
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

			// the response:
			//System.out.println(response.body().toString());
			return new Response(true, "send request successfully!", response.body().toString());
			
		} catch(Exception e) {
			return new Response(false, e.getMessage());
		}
	}
	
	public Response sendPostRequest(String route, HashMap<String,String> map)
	{
	    String requestBody = this.jsonMapper.mapToJsonString(map);
		
		HttpRequest request;
		try {
			request = HttpRequest.newBuilder()
					  .uri(new URI(this.baseURI+this.uriMap.get(route)))
					  .header("Content-Type", "application/json; charset=UTF-8")
					  .POST(HttpRequest.BodyPublishers.ofString(requestBody))
					  .build();
			HttpClient client = HttpClient.newHttpClient();

			// use the client to send the request
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			// the response:
			//System.out.println(response.body().toString());
			return new Response(true, "send request successfully!", response.body().toString());
			
		} catch(Exception e) {
			return new Response(false, e.getMessage());
		}
	}
}
