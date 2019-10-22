package textAdventure;

import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/* A skeleton program for a text adventure game */
/* some other parts, like rooms, will be explained in class */

public class AdventureMain {

	static int INVSIZE = 10; //size of inventory	

	//instance variables
	//ArrayList<Room> roomList = new ArrayList<Room>();
	HashMap<String,Room> roomList = new HashMap<String,Room>();
	HashMap<String, Item> itemList = new HashMap<String,Item>(); //list of all item objects
	//ArrayList<String> inventory = new ArrayList<String>();
	
	String currentRoom;
	Player player;

	int turns = 0;

	public static void main(String[]args){
		new AdventureMain();
	}

	AdventureMain() {

		boolean playing = true;
		String command = "";

		setup(); //create all objects needed, including map; print intro. message

		lookAtRoom(true); //display information about the current room

		/***** MAIN GAME LOOP *****/
		while (playing) {

			command = getCommand();

			playing = parseCommand(command);
			System.out.println("\n== " + roomList.get(currentRoom).name + " ==");
			//check to see if player has died (in whichever various ways the player can die)

			//check to see if the player has won the game

		}

		// does anything need to be done after th emain game loop exits?

	}

	void setup() {
		Room.setupRooms(roomList);
		Item.setUpItems(itemList, roomList);
		// ... more stuff ...
		currentRoom = "cell1";
		player = new Player();
		//DEBUG
		
		
		
	}

	String getCommand() {
		Scanner sc = new Scanner(System.in);		
		String text = sc.nextLine();
		if (text.length() == 0) text = "qwerty"; //default command
		//sc.close();
		return text;
	}

	
	boolean parseCommand(String text) {

		/***** PREPROCESSING *****/
		//P1. 
		text = text.toLowerCase().trim();	//the complete string BEFORE parsing


		//handle situation where no words entered ...


		//P2. word replacement
		text = text.replaceAll(" into ", " in ");
		text = text.replaceAll(" rocks", " rock");
		text = text.replaceAll("pick up", "pickup");
		text = text.replaceAll("look at", "lookat");
		text = text.replaceAll("climb up", "climbup");

		String words[] = text.split(" ");

		//P3. remove all instances of "THE"
		ArrayList<String> wordlist = new ArrayList<String>(Arrays.asList(words));		//array list of words
		for(int i=0; i< wordlist.size(); i++) {
			if (wordlist.get(i).equals("the")) wordlist.remove(i--);			
		}

		//separate out into word1, word2, etc.
		// ...
		String word1 = words[0];
		String word2 = "";
		String word3 = "";
		if (words.length>1)	word2 = words[1];
		if (words.length>2)	word3 = words[2];

		/***** MAIN PROCESSING *****/
		switch(word1) {

		/**** one word commands ****/
		case "quit":
			System.out.print("Do you really want to quit the game? ");
			String ans = getCommand().toUpperCase();
			if (ans.equals("YES") || ans.equals("Y")) {
				System.out.print("Thanks for playing. Bye.");
				return false;
			}			
		case "n": case "s": case "w": case "e": case "u": case "d":
		case "north": case "south": case "west": case "east": case "up": case "down":
			moveToRoom(word1.charAt(0));	
			break;
		case "i": case "inventory":
				showInventory();
			break;
		case "sleep":
			//		sleep();			
			break;	
		case "help":
			System.out.print("North-n, East-e, West-w, Up-u, Down-d");
			break;
		case "check":
			lookAtRoom(true);
			break;
			/**** two word commands ****/		
		case "look":
			System.out.print("look where?");
			String com = getCommand().toLowerCase();
			if (com.equals("n") || com.equals("look n")) {
				System.out.print("There is " + roomList.get(currentRoom).name + " north of you");
			}
			if (com.equals("s") || com.equals ("look s")) {

			}
			break;
		case "take":
			if (word2 == "") {
				System.out.print("take what? ");			
				String comm = getCommand().toLowerCase().trim();
				word2 = comm;
//				if (word2=="key") {
//					 Item key = roomList.get("cell1").items.get(0);
//						player.inventory.add(key);
//				}
			} else {
				word2 = word2 + " " + word3;
			}
			
			//does current room contain item
			// roomList.get(currentRoom).items  < -- this is the item list for the current rooom
			
			int numItems = roomList.get(currentRoom).items.size();
			
			//find item in the room
			boolean found = false;
			for (int i = 0; i < numItems; i++ ) {
				Item item = roomList.get(currentRoom).items.get(i);
				
				//fix if statement to handle "wire clippers" <--space must be removed from name and word2 ???
				if (item.name.equalsIgnoreCase(word2)) {
					//player.inventory.add(item);
				//	 Item key = roomList.get("cell1").items.get(0);
					player.inventory.add(item);
					roomList.get(currentRoom).items.remove(item);
					System.out.println(word2 + " taken");
					found = true;
					break;
				}
			}
			
			if (!found) System.out.println("There is no " + word2 + " here.");
			
			break;		

			/**** SPECIAL COMMANDS ****/
			// ...		

		default: 
			System.out.println("Sorry, I don't understand that command");
		}
		return true;
	}	

	 void Addingitems() {
		// TODO Auto-generated method stub
		 Item axe = roomList.get("cell3").items.get(0);//there is only one item in the room, so AXE will be in position 0
			player.inventory.add(axe);
		 Item key = roomList.get("cell1").items.get(0);
			player.inventory.add(key);
			
	}

	private void showInventory() {
		//System.out.print(Arrays.toString(player.inventory));				
		if (player.inventory.size() == 0) {
			System.out.println("There is nothing in the inventory.");
			return;
		}
		System.out.println("\n-------- INVENTORY--------");
		for (Item inven: player.inventory) {
			System.out.println(inven.name);
		}
	}

	private void printHelp() {
		// TODO Auto-generated method stub

	}

	//tons of other methods go here ...		
	void lookAtRoom(boolean x) {
		String text = roomList.get(currentRoom).descr;
		System.out.println(text);

		//list items in room
		if (roomList.get(currentRoom).items.size() > 0 ) {
			System.out.println("The room contains:");
		}
		for (Item item : roomList.get(currentRoom).items) {
			System.out.println(item.name + ": " + item.descr);
		}
	}

	void moveToRoom(char c) {
		Room r = roomList.get(currentRoom);
		
		String message = "You cannot go that way.";
		
		//north
		if (c == 'n' && r.n != null) {
			currentRoom = r.n;
		}
		if(c == 'n' && r.n == null) System.out.println(message);
		
		//south
		if (c == 's' && r.s !=null) {
			currentRoom = r.s;
		}
		if(c == 's' && r.s == null) System.out.println(message);
		
		//east
		if (c == 'e' && r.e != null) {
			currentRoom = r.e;
		}
		if(c == 'e' && r.e == null) System.out.println(message);
		
		//west
		if (c == 'w' && r.w != null) {
			currentRoom = r.w;
		}
		if(c == 'w' && r.w == null) System.out.println(message);
		
		//up
		if (c == 'u' && r.u !=null) {
			currentRoom=r.u;
		}
		if(c == 'u' && r.u == null) System.out.println(message);
		
		//down
		if (c == 'd' && r.d !=null) {
			currentRoom=r.d;
		}
		if(c == 'd' && r.d == null) System.out.println(message);
		
		lookAtRoom(true);
	}

}
