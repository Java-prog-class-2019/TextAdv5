package textAdventure;

import java.util.ArrayList;

class Doors {
	
	boolean unlocked;
	//locations on either side of the door
	String loc1; 
	String loc2;
	
	//constructor
	Doors (boolean a, String b, String c){
		unlocked=a;
		loc1=b;
		loc2=c;		
	}
		
	
	static void setUpDoors (ArrayList<Doors> doorList) {
		Doors cellDoor=new Doors(false, "cell1", "hallway1");
		Doors cafDoor=new Doors(false, "hallway3","cafeteria");
		Doors westDoor=new Doors(false, "hallway8", "hallway9");
		doorList.add(cellDoor);
		doorList.add(cafDoor);
		doorList.add(westDoor);
	}
}
