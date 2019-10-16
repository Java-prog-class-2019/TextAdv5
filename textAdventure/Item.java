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
		Item key = new Item("Key", "A key that can be used to unlock your cellblock.");
		
		Item sleepingDust = new Item("Sleeping Dust", "A magic sleeping dust that can put a person to sleep.");
		
		Item axe = new Item("Axe", "An shiny axe.");
		
		Item clothes = new Item("Clothes","A change of clothes. Strangely, it is your size...");
		
		Item chair = new Item("Chair", "A well crafted chair that looks very durable.");
		
		Item wireClippers = new Item("Wire Clippers", "A wire clippers.");
		
		Item keycard = new Item("Keycard", "A keycard that can be used to unlock a door.");

		Room r = roomList.get("cellblock");
		r.items.add(key);	//and here the item is added to the specific room that you want it in.
		
//		Room r1 = roomList.get("hallway");
//		r1.items.add(wireClippers);
		
	}
	
	
}