package textAdventure;

import java.util.HashMap;

class Room {
	String name = "";
	String descr = "";
	
	//exits
	String n,s,e,w,u,d;
	//items in an arraylist
	
	//constructor
	Room(String s1, String s2) {
		name = s1;
		descr = s2;
	}
	
	static void setupRooms(HashMap<String,Room> roomList) {
		Room r = new Room("A dark basement", 
				"You are in a dark basement. \nThere is a dirty window that allows a little light in. There is a desk here.");
		r.n = "basement2";
		roomList.put("basement1", r);
		
		r = new Room("A dark basement", 
				"You are in a tunnel that twists as it goes downwards...");
		r.s = "basement1";
		roomList.put("basement2", r);
	}
}
