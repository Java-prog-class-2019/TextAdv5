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
		//cell1
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
		r = new Room ("Hallway", "You are in a long hallway which runs East to West." +
				" There is a guard in the hallway to the east");
		r.w="hallway1";
		r.e="hallway5";
		roomList.put("hallway2", r);
		//hallway 3
		r = new Room ("Hallway", "North of you is a locked gate. South of you is your cell");
		r.n="cafeteria";
		r.s="hallway1";
		roomList.put("hallway3", r);
		//hallway 4
		r= new Room ("Hallway", "Your are in a long hallway that runs East to West");
		r.e="hallway1";
		r.w="hallway6";
		roomList.put("hallway4", r);
		//hallway 5 (contains guard)
		r=new Room ("Hallway", "You are in a section of hallway which contains an AWAKE guard."+
				"If you go by him while he's awake you will lose.");
		r.e="hallway7";
		r.w="hallway2";
		roomList.put("hallway5", r);
		//hallway6
		r=new Room ("Hallway", "There are two rooms nearbye; one room is to the North and one is to the south"+
				"To the west and east is more hallway");
		r.w="hallway8";
		r.e="hallway4";
		r.n="LaundryRoom";
		r.s="cell2";
		roomList.put("hallway6", r);
		//hallway7
		r=new Room("Hallway", "There are two rooms nearbye; one room is to the East and one room is to the South. "+
				"To the west is more hallway");
		r.e="SecurityRoom";
		r.w="hallway5";
		r.s="cell3";
		roomList.put("hallway7", r);
		//hallway8
		r=new Room("Hallway", "To the east of you is more hallway. To the West of you is a locked gate which blocks your way.");
		r.e="hallway6";
		r.w="hallway9";
		roomList.put("hallway8", r);
		//hallway 9
		r=new Room("Hallway", "There is a room to the West of you from which you can hear the sound of guard voices."+
				"You can also see a set of stairs leading down. To the East is a gate.");
		r.d="stairs";
		r.w="GuardRoom";
		r.e="hallway8";
		roomList.put("hallway9", r);
		//cell2
		r=new Room("Empty Cell","You are in an empty cell. On the floor is a bottle of strange blue powder."); //once we finish the game we want to actually hide the powder in the cell, but not yet
		r.n="hallway6";
		roomList.put("cell2", r);
		//cell3
		r=new Room("Empty Cell","You are in an empty cell. From the behind the bes you can see something shiny glinting."); //need command like "look in corner". Also do we want to take the word in out of commands as well as the???
		r.n="hallway7";
		roomList.put("cell3", r);
		//Security Room
		r=new Room("Security Room", "A sound of electricity fills the room and you find yourself looking at a giant computer screen"+
				"which displays the video feed from the cafeteria."); //can we put the security camera in the cafeteria. If they are in the cafeteria and the camera isn't turned off the player will lose.
		r.w="hallway7";
		roomList.put("SecurityRoom", r);
		//Guard Room - enter this room and you lose
		r=new Room("Guard Room", "The room is filled with gaurds! YOU LOSE!!!");
		r.e="hallway9";
		roomList.put("GuardRoom", r);
		//Laundry Room - go here to find change of clothes
		r=new Room("Laundry Room", "There are baskets of laundry everywhere filled with clean guard unifroms.");
		r.s="hallway6";
		roomList.put("LaundryRoom", r);
		//cafeteria
		r=new Room("Cafeteria", "There is a security camera in this room and you are caught on camera. YOU LOSE!!!");
		//we want this option for the cafteria if the security camera is turned off
		//r=new Room("Cafeteria", "There is a security camera, but it is turned off. The room is filled with tables and chairs.");
		r.s="hallway3";
		r.n="ExcerciseYard";
		roomList.put("cafeteria", r);
		r=new Room("Excercise Yard","You are in a grassy field. A tall electric fence on the north side of the field separates you from the outside world");
		r.s="cafeteria";
		r.n="ElectricFence";
		roomList.put("ExcerciseYard", r);
	}
}
