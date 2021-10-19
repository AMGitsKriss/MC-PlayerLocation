package uk.qvvz.location;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.gson.Gson;

public class LocationRecorder {
	private static ConfigManager config;
	private static Queue<RecordModel> locationQueue = new ArrayDeque<RecordModel>();
	
	private static final String fileWriteDir = "plugins/PlayerLocation/";
	
	static void setPlugin(ConfigManager c) {
		config = c;
	}
	
	public static void record(Player player) {
		Location location = player.getLocation();
		
		@SuppressWarnings("deprecation")
		RecordModel loc = new RecordModel(
				player.getUniqueId().toString(),
				location.getX(),
				location.getY(),
				location.getZ(),
				player.getWorld().getEnvironment().getId()
			);
		
		if(config.printToConsole()) {	
			String message = String.format("[PlayerLocation] %s at %f, %f, %f in dimension %d", loc.uuid, loc.xPos, loc.yPos, loc.zPos, loc.dimension);
			Bukkit.getLogger().info(message);
		}
		
		locationQueue.add(loc);
		if(locationQueue.size() > config.batchSize()) {
			save();
		}
	}
	
	public static void save() {
		Bukkit.getLogger().info(String.format("[PlayerLocation] Writing %d location entries to file.", locationQueue.size()));
		
		if(config.endpointUri() != null && !config.endpointUri().trim().isEmpty()) {
			postToApi(locationQueue);
		}
		else {
			writeToCSV(locationQueue);			
		}
	}
	
	private static void writeToCSV(Queue<RecordModel> queue) {
	    try {
	    	Date date = new Date();    
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'");  
	        String dateString = formatter.format(date);
	        
	    	File myObj = new File(fileWriteDir);
	        myObj.createNewFile();	
	        
	    	FileWriter myWriter = new FileWriter(fileWriteDir + dateString +".csv", true);
	    	while(!locationQueue.isEmpty()) {	 
	    		RecordModel loc = locationQueue.poll();
		        String row = String.format("%s, %s, %f, %f, %f, %d", loc.uuid, loc.timeStamp.toString(), loc.xPos, loc.yPos, loc.zPos, loc.dimension);
		        
		        myWriter.write(row);
	    	}
        	myWriter.close();
	    } 
	    catch (IOException ex) {
	    	Bukkit.getLogger().info(ex.toString());
	    }   
	}
	
	private static void postToApi(Queue<RecordModel> queue) {
        String requestBody = new Gson().toJson(queue);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(config.endpointUri()))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			if(response.statusCode() == 200) {
				locationQueue = new ArrayDeque<RecordModel>();
			}
		} catch (Exception ex) {
			Bukkit.getLogger().info("[PlayerLocation] " + ex.toString());
		}

	}
	
	/*private static void postToApi(Queue<RecordModel> queue) {
		try {
			String json = new Gson().toJson(queue);
			byte[] postData       = json.getBytes( StandardCharsets.UTF_8 );
			int    postDataLength = postData.length;
			URL    url            = new URL( config.endpointUri() );
			HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
			conn.setDoOutput( true );
			conn.setInstanceFollowRedirects( false );
			conn.setRequestMethod( "POST" );
			conn.setRequestProperty( "Content-Type", "application/json"); 
			conn.setRequestProperty( "charset", "utf-8");
			conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
			conn.setUseCaches( false );
			
			DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
			wr.write( postData );
			wr.close();
		}
		catch (Exception ex) {
			Bukkit.getLogger().info(ex.toString());
	    }   
	}*/
}
