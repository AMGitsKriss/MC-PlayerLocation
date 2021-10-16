package uk.qvvz.location;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LocationRecorder {
	private static ConfigManager config;
	private static Queue<String> locationQueue = new ArrayDeque<String>();
	
	private static final String fileWriteDir = "plugins/PlayerLocation/output.txt";
	
	static void setPlugin(ConfigManager c) {
		config = c;
	}
	
	public static void Record(Player player) {
		Location location = player.getLocation();
		
		String name = player.getName();
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		
		if(config.printToConsole()) {	
			String message = String.format("[PlayerLocation] %s at %f, %f, %f", name, x, y, z);
			Bukkit.getLogger().info(message);
		}
		
		locationQueue.add(String.format("[PlayerLocation] %s at %f, %f, %f", name, x, y, z));
		if(locationQueue.size() > 5) {
			Bukkit.getLogger().info(String.format("[PlayerLocation] Writing %d location entries to file.", locationQueue.size()));
			while(!locationQueue.isEmpty()) {
				writeToFile(locationQueue.remove(), locationQueue.isEmpty());			
			}
		}
	}
	
	private static void writeToFile(String message, boolean isLastEntry) {
	    try {
	    	File myObj = new File(fileWriteDir);
	        myObj.createNewFile();
	        
	        FileWriter myWriter = new FileWriter(fileWriteDir, true);
	        myWriter.write(message);
	        if(isLastEntry)
	        	myWriter.close();
	    } 
	    catch (IOException ex) {
	    	  Bukkit.getLogger().info(ex.toString());
	    }
	    
	    
	}
}
