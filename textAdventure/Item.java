package textAdventure;

import java.util.HashMap;

class Item {
	String name = "";
	String descr = "";
	
	//constructor
	Item(String s1, String s2) {
		name = s1;
		descr = s2;
	}
	static void setUpItems(HashMap<String,Item> itemList, HashMap<String,Room> roomList) {
		Item z = new Item("Keycard", "A keycard that can be used to unlock a door.");
		
		Item z2 = new Item("Bomb", "IF it goes off the game is over now.");
		
//		z.descrRoom = "There is a card inside the guard's back pocket.";	//this is displayed along with the room description when you look at the room.
//		itemList.put("keycard",z);						//this is the true (hashmap) name of the item. It's never displayed.
		Room r = roomList.get("basement1");
		r.items.add(z);	//and here the item is added to the specific room that you want it in.
		r.items.add(z2);
		
	}
	
	
}
