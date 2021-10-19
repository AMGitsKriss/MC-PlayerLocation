package uk.qvvz.location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordModel {
	
	public RecordModel(String id, double x, double y, double z, int d) {
		uuid = id;
		xPos = x;
		yPos = y;
		zPos = z;
		dimension = d;
		
		Date date = new Date();    
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");  
        timeStamp = formatter.format(date);     
	}
	
	String uuid;
	double xPos;
	double yPos;
	double zPos;
	int dimension;
	String timeStamp;
}