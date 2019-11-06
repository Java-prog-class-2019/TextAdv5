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
		
		Item wireClippers = new Item("Wireclippers", "A shiny pair of wire clippers which look sharp enough to disable the electric fence computers.");
		
		Item keycard = new Item("Keycard", "A keycard that can be used to unlock a door.");
		
		Item avocado = new Item("Avocado", "A delishous looking vegetable specimen of bright green hue.");

		Room r = roomList.get("cell1");
		r.items.add(key);	//and here the item is added to the specific room that you want it in.
		
		r = roomList.get("cell2");
		r.items.add(sleepingDust);
		
		r = roomList.get("cell3");
		r.items.add(axe);
		
		r = roomList.get("LaundryRoom");
		r.items.add(clothes);
		
		r = roomList.get("cafeteria");
		r.items.add(chair);
		
		r = roomList.get("storage1");
		r.items.add(wireClippers);
				
		r = roomList.get("hallway7");
		r.items.add(keycard);
		
		r = roomList.get("cafeteria");
		r.items.add(avocado);
		
		
	}
	
	
}
