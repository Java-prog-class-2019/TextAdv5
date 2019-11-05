package textAdventure;

//import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/* A skeleton program for a text adventure game */
/* some other parts, like rooms, will be explained in class */

public class AdventureMain {

	static int INVSIZE = 10; //size of inventory	

	//instance variables
	ArrayList<Doors> doorList=new ArrayList<Doors>();
	//ArrayList<Room> roomList = new ArrayList<Room>();
	HashMap<String,Room> roomList = new HashMap<String,Room>();
	HashMap<String, Item> itemList = new HashMap<String,Item>(); //list of all item objects
	//ArrayList<String> inventory = new ArrayList<String>();

	String currentRoom;
	Player player;

	int turns = 0;
	boolean fenceOff = false;

	public static void main(String[]args){
		new AdventureMain();
	}

	AdventureMain() {

		boolean playing = true;
		boolean normClothes = false; //is the user wearing normal clothese or prison clothes

		String command = "";

		setup(); //create all objects needed, including map; print intro. message
		System.out.println("\n== " + roomList.get(currentRoom).name + " ==");
		lookAtRoom(true); //display information about the current room

		/***** MAIN GAME LOOP *****/
		while (playing) {


			command = getCommand();

			playing = parseCommand(command, normClothes);
			//check to see if player has died (in whichever various ways the player can die)

			//;check to see if the player has won the game
			if (currentRoom.equals("Guardroom")) {
				playing = false;
			}
		}


		// does anything need to be done after th emain game loop exits?

	}

	void setup() {
		Room.setupRooms(roomList,doorList);
		Item.setUpItems(itemList, roomList);
		Doors.setUpDoors(doorList);
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


	boolean parseCommand(String text, boolean normClothes) {

		/***** PREPROCESSING *****/
		//P1. 
		text = text.toLowerCase().trim();	//the complete string BEFORE parsing


		//handle situation where no words entered ...


		//P2. word replacement
		text = text.replaceAll(" into ", " in ");
		text = text.replaceAll("pick up", "take");
		text = text.replaceAll("sleepingdust", "sleeping dust");
		text = text.replaceAll("pick up", "pickup");
		text = text.replaceAll(" rocks", " rock");
		text = text.replaceAll("look at", "lookat");
		text = text.replaceAll("climb up", "climbup");
		text = text.replaceAll("open", "unlock");
		text = text.replaceAll("move ", ""); //so it will work if the user types move north instead of just north
		text = text.replaceAll("go", "");
		text = text.replaceAll("change into", "change");
		text = text.replaceAll("put on", "change");

		text = text.replaceAll("throw", "use");
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
			quitGame();		
		case "n": case "s": case "w": case "e": case "u": case "d":
		case "north": case "south": case "west": case "east": case "up": case "down":
			boolean b = moveToRoom(word1.charAt(0));
			if (!b) return false;
			break;
		case "i": case "inventory":
			showInventory();
			break;
		case "sleep":
			//		sleep();			
			break;	
		case "help":
			printHelp();
			break;
			/**** two word commands ****/		
		case "look":
			look(word2);
			break;
		case "unlock":
			unlock(word2, word3,doorList);
			break;
		case "drop":
			dropItem(word2, word3);
			break;

		case "break":
			breakObject(word2);
			break;
		case "take":
			takeItem(word2, word3);
			break;	
		case "use":
			useSleepinggas(word2, word3);break;
		case "change":
			normClothes=changeClothes(word2);
			if (normClothes) System.out.println("You are now wearing a guard uniform");
			else System.out.println("You have no clothes to change into");
			break;
		case "cut":
			fenceOff = cutWires(word2);
			break;
		default:
			System.out.println("Sorry, I don't understand that command");			
		}
		turns++;
		System.out.println("\nmoves: "+turns);
		if (currentRoom.equals("Guardroom")) {
			System.out.println("YOU LOSE!!!");
			return false;
		}
		return true;
	}	

	private boolean cutWires(String word2) {
		if (word2 == "") {
			System.out.println("cut what");
			String cut = getCommand().toLowerCase().trim();
			word2 = cut;
		}
		boolean found=false;
		Room r = roomList.get(currentRoom);
		if(word2.equals("wires") || word2.equals("wire")) {
			for (int i = 0; i < player.inventory.size(); i++) {
				Item item = player.inventory.get(i);			
				if (item.name.equals("Wire Clippers")) {
					found = true;
					if(r.name=="Electric room") {
						System.out.println("You cut wires with wire clippers. The electric fence is now off. RUN FOR YOUR FREEDOM!");
						//change the exits in the room where the electric fence is to allow people to leave. Excercise Yard and also change the description
						Room r2 = roomList.get("ExerciseYard");
						r2.descr = "You are in a grassy field. A tall electric fence on the north side of the field separates you from the outside world. However, it seems to be turned off.";
						r2.n="OutsideWorld";
						return true;
					}else {							
						System.out.println("There is no wire to cut here.");
						return false;
					}											
				}
			}
			if(found == false){
				System.out.println("You don't have a wire clipper to cut the wire.");
				return false;
			}
		}else {
			System.out.println("You can't cut that" );
			return false;
		}

		return false;
	}

	private void useSleepinggas(String word2, String word3) {
		if (word2 == "") {
			System.out.println("use what");
			String use = getCommand().toLowerCase().trim();
			word2 = use;
		}
		else {
			if (word3 != "") word2 = word2 + " " + word3;
		}
		boolean found=false;
		Room r = roomList.get(currentRoom);
		if(word2.equals("sleepingdust") || word2.equals("sleeping dust")) {					
			for (int i = 0; i < player.inventory.size(); i++) {
				Item item = player.inventory.get(i);			
				if (item.name.equals("Sleeping Dust")) {
					found = true;
					if(roomList.get(currentRoom).guard == Room.AWAKEGUARD) {
						r.guard = Room.SLEEPINGGUARD;			
						r.descr = "You are in a section of hallway which now contains Sleeping guard" + "walk quietly past him"	;							
						System.out.println("You use sleeping dust. The guard is now asleep. You spot a key card hanging off the guard's belt");
						player.inventory.remove(item);	
					}else {							
						if(item.name.equals("Sleeping Dust"))
							player.inventory.remove(item);							
						System.out.println("You wasted your sleeping dust. You nolonger have sleeping dust in your inventory");
					}											
				}
			}
			if(found == true){
				System.out.println(".");
			}else {
				System.out.println("you dont have sleeping dust");
			}
		}else {
			System.out.println("can't use that" );
		}
		// you use the sleeping gas, nothing happens, your gas is gone.
		if(roomList.get(currentRoom).guard == Room.SLEEPINGGUARD) {
			if(roomList.get(currentRoom).name == "hallway5") {
				System.out.println("...");
			}
		}


	}

	private void quitGame() {
		System.out.print("Do you really want to quit the game? ");
		String ans = getCommand().toUpperCase();
		if (ans.equals("YES") || ans.equals("Y")) {
			System.out.print("Thanks for playing. Bye.");
			System.exit(0);
		}	
	}

	private void takeItem(String word2, String word3) {
		if (word2 == "") {
			System.out.print("take what? ");			
			String comm = getCommand().toLowerCase().trim();
			word2 = comm;
			//				if (word2=="key") {
			//					 Item key = roomList.get("cell1").items.get(0);
			//						player.inventory.add(key);
			//				}
		} else {
			if (word3 != "")	word2 = word2 + " " + word3;
		}

		//does current room contain item
		// roomList.get(currentRoom).items  < -- this is the item list for the current rooom

		int numItems = roomList.get(currentRoom).items.size();
		//System.out.print("=="+word2+);

		//find item in the room
		boolean found = false;
		for (int i = 0; i < numItems; i++ ) {
			Item item = roomList.get(currentRoom).items.get(i);

			//fix if statement to handle "wire clippers" <--space must be removed from name and word2 ???
			if (item.name.equalsIgnoreCase(word2)) {
				player.inventory.add(item);
				if (item.name.equals("Wire Clippers"))roomList.get("storage1").descr="You are in a storage room.";
				roomList.get(currentRoom).items.remove(item);
				System.out.println(word2 + " taken");
				found = true;
				break;
			}
		}

		if (!found) System.out.println("There is no " + word2 + " here.");

	}

	private void look(String word2) {
		String com="";
		if (word2=="") {
			System.out.print("look where?");
			com = getCommand().toLowerCase();
		}
		else com=word2;


		if (com.equals("here")) {
			lookAtRoom(true);
		}

		if (com.equals("n") || com.equals("look n")) {
			if (roomList.get(currentRoom).n != null) {
				String room=roomList.get(currentRoom).n;
				System.out.print("There is " + roomList.get(room).name + " north of you");
			}
			else System.out.println("There is no room north of you.");
		}
		if (com.equals("s") || com.equals ("look s")) {
			if (roomList.get(currentRoom).s != null) {
				String room=roomList.get(currentRoom).s;
				System.out.print("There is " + roomList.get(room).name + " south of you");
			}
			else System.out.println("There is no room south of you.");
		}
		if (com.equals("e") || com.equals("look e")) {
			if (roomList.get(currentRoom).e != null) {
				String room=roomList.get(currentRoom).e;
				System.out.print("There is " + roomList.get(room).name + " east of you");
			}
			else System.out.println("There is no room east of you.");
		}
		if (com.equals("w") || com.equals("look w")) {
			if (roomList.get(currentRoom).w != null) {
				String room=roomList.get(currentRoom).w;
				System.out.print("There is " + roomList.get(room).name + " west of you");
			}
			else System.out.println("There is no room west of you.");
		}
		if (com.equals("u") || com.equals("look u")) {
			if (roomList.get(currentRoom).u != null) {
				String room=roomList.get(currentRoom).u;
				System.out.print("There is " + roomList.get(room).name + " above you");
			}
			else System.out.println("There is no room above you.");
		}
		if (com.equals("d") || com.equals("look down")) {
			if (roomList.get(currentRoom).d != null) {
				String room=roomList.get(currentRoom).d;
				System.out.print("There is " + roomList.get(room).name + " below you");
			}
			else System.out.println("There is no room below you.");
		}


	}



	private void dropItem(String word2, String word3) {
		if (word2 == "") {
			System.out.print("drop what? ");
			String comm = getCommand().toLowerCase().trim();
			word2 = comm;
		} else {
			if (word3 != "") word2 = word2 + " " + word3;
		}

		boolean invFound = false;
		for (int i = 0; i < player.inventory.size(); i++) {
			Item item = player.inventory.get(i);

			if (item.name.equalsIgnoreCase(word2)) {
				player.inventory.remove(item);
				roomList.get(currentRoom).items.add(item);
				System.out.println(word2 + " dropped");
				invFound = true;
				return;
			}
		}
		System.out.println("You don't have the " + word2);

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
		System.out.print("North-n, East-e, West-w, Up-u, Down-d");
	}
	void breakObject(String  word2) {

		//fix the != ... .equals
		if (currentRoom !="SecurityRoom" && currentRoom != "electricRoom") {
			//are there other rooms where you can break something?
			System.out.println("There is nothing to break here");
			return;
		}

		//you are in the correct room

		if(word2 =="") {
			System.out.print("Break computer Screen with what?");
			String comm = getCommand().toLowerCase().trim();
			word2=comm;
		}
		else {
			if (word2 != "")
				word2=word2 + " ";

		}

		//see if object is in inventory
		boolean invFound = false;
		for (int i = 0; i < player.inventory.size(); i++) {
			Item item = player.inventory.get(i);

			if (item.name.equalsIgnoreCase(word2)) {				
				invFound = true;			
				System.out.print("Security Cameras and Computer screens smashed with Axe and you cannot be seen");
			}
		}
		if (!invFound) {
			System.out.println("Sorry, you don't have " + word2 + " in your inventory");

		}
		return;
		//you have the object and you are in the correct room:
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

	/* This method handles all movement between rooms.
	 * It takes a single character for direction (n,w,e,su,d)
	 * It returns a boolean: true means 		false means
	 */
	boolean moveToRoom(char c) {
		Room r = roomList.get(currentRoom);

		String message = "You cannot go that way.";
		if (!checkGuard(currentRoom,c)) return false; //false = game over
		if(currentRoom.equals(doorList.get(0).loc1)||currentRoom.equals(doorList.get(0).loc2)) {
			if (doorList.get(0).unlocked==false) {
				System.out.print("You can not move there. Your cell door is locked.");
				return true;
			}
		}
		
		if(currentRoom.equals("ExerciseYard") && c=='n') {
			boolean invFound = false;
			for (int i = 0; i < player.inventory.size(); i++) {
				Item item = player.inventory.get(i);
				if (item.name.equalsIgnoreCase("Chair")) invFound = true;					
			}
			if(invFound && fenceOff) {
				currentRoom = r.n;
				lookAtRoom(true);
				return true;
			}
			if (!invFound) {
				System.out.println("You are not tall enough to climb over the fence.");
				return true;
			}
			else {
				System.out.println("The electric fence is still on.");
			}
			
		}

		//north
		if (c == 'n' && r.n != null) {
			if(currentRoom.equals(doorList.get(1).loc1)) {
				if (doorList.get(1).unlocked==false) {
					System.out.print("You can not move there. There is a blocked door blocking your way.");
					return true;
				}
			}
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
			if(currentRoom.equals(doorList.get(2).loc1)) {
				if (doorList.get(2).unlocked==false) {
					System.out.print("You can not move there. There is a blocked door blocking your way.");
					return true;
				}
			}
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
		System.out.println("\n== " + roomList.get(currentRoom).name + " ==");



//		see if you win the game
//		if(currentRoom.equals("OutsideWorld")) {
//			System.out.println("YOU WIN!");
//
//		}

		lookAtRoom(true);
		return true;
	}
	void unlock(String w2, String w3, ArrayList<Doors>doorList) {
		if (w2.equals("door")) {
			//cell door
			//are they in the cell or the hallway outside it?
			boolean haveKey=false;
			boolean haveKeyCard=false;
			for (Item inven: player.inventory) {
				if(inven.name.equals("Key")) haveKey=true;
				if (inven.name.equals("Keycard")) haveKeyCard=true;
			}
			if(currentRoom.equals(doorList.get(0).loc1)||currentRoom.equals(doorList.get(0).loc2)) {
				if (haveKey) {
					doorList.get(0).unlocked=true;
					System.out.print("Door unlocked");
				}
				else System.out.print("You don't have a key");
			}
			else if (currentRoom.equals(doorList.get(1).loc1)||currentRoom.equals(doorList.get(1).loc2)) {
				if (haveKeyCard) {
					doorList.get(1).unlocked=true;
					System.out.print("Door unlocked");
				}
				else System.out.println("You need a guard's key card to unlock this.");
			}
			else if (currentRoom.equals(doorList.get(2).loc1)||currentRoom.equals(doorList.get(2).loc2)) {
				if (haveKeyCard) {
					doorList.get(2).unlocked=true;
					System.out.print("Door unlocked");
				}
				else System.out.println("You need a guard's key card to unlock this.");
			}
			else System.out.println("There are no doors nearbye to unlock.");
		}
	}

	// check guard only
	private boolean checkGuard(String currentRoom, char c) {
		//don't look for specific room names, look if a Guard object is in the room
		//and make sure that guard is awake -->>> lose
		if(roomList.get(currentRoom).guard == Room.AWAKEGUARD && c=='e') {
			System.out.println("The guard kills you");
			return false;			
		}

		return true;		


	}

	boolean changeClothes(String w2) {
		if (w2.equals("clothes")) {
			boolean a=false;
			if (player.inventory.size()==0) a=false;
			else {
				for (Item inven: player.inventory) {
					if (inven.name.equals("Clothes")) {
						a= true;
						break;
					}
					else a=false;
				}
			}
			if(a)player.inventory.remove("Clothes");
			return a;
		}
		return false;
	}

}
