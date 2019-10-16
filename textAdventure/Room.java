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
		/*Room r = new Room("A dark basement", 
				"You are in a dark basement. \nThere is a dirty window that allows a little light in. There is a desk here.");
		r.n = "tunnel";
		roomList.put("basement1", r);
		
		r = new Room("A dark tunnel", 
				"You are in a tunnel that twists as it goes downwards...");
		r.s = "basement1";
		roomList.put("tunnel", r);
		*/
		Room r = new Room("Your Cell",
				"You find yourself in a dingy prison cell. The door out of your cell, which is to the north of you, is locked.");
		r.n = ("hallway1");
		roomList.put("cell1", r);
		r = new Room ("Hallway", "You find yourself in a dimly lit hallway, which leads North, East, and West.");
		r.s="cell1";
		r.n="hallway3";
		r.e="hallway2";
		r.w="hallway4";
		roomList.put("hallway1", r);
		//hallway 2
		r = new Room ("Hallway", "You are in a long hallway which runs East to West");
		r.w="hallway1";
		r.e="hallway5";
		roomList.put("hallway2", r);
		
	}
}
