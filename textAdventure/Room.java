package textAdventure;

import java.util.ArrayList;
import java.util.HashMap;

class Room {
	String name = "";
	String descr = "";
	
	//exits
	String n,s,e,w,u,d;
	//items in an arraylist
	ArrayList<Item> items = new ArrayList<Item>(); 
	
	//constructor
	Room(String s1, String s2) {
		name = s1;
		descr = s2;
	}
	
	static void setupRooms(HashMap<String,Room> roomList) {
		Room r = new Room("cellblock", 
				"You are in your cell. \nThere is a dirty window that allows a little light in. There is a desk here.");
		r.n = "hallway";
		roomList.put("cellblock", r);
		
		r = new Room("hallway", 
				"You are in a hallway that hass an intersection");
		r.s = "cellblock";
		roomList.put("tunnel", r);
	}
}
