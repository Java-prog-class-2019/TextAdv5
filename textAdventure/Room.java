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
		Room r = new Room("A dark basement", 
				"You are in a dark basement. \nThere is a dirty window that allows a little light in. There is a desk here.");
		r.n = "tunnel";
		roomList.put("basement1", r);
		
		r = new Room("A dark tunnel", 
				"You are in a tunnel that twists as it goes downwards...");
		r.s = "basement1";
		roomList.put("tunnel", r);
	}
}
