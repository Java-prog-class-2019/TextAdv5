/********Break out of Prison Game*******/
/* Text Adventure Group 5: Freyja, Tianqi, Chibuke, Ite
 * 11/07/19
 */

package textAdventure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class AdventureMain {

	//instance variables
	ArrayList<Doors> doorList=new ArrayList<Doors>();
	HashMap<String,Room> roomList = new HashMap<String,Room>();
	HashMap<String, Item> itemList = new HashMap<String,Item>(); //list of all item objects

	String currentRoom;
	Player player;

	int turns = 0;
	boolean fenceOff = false;
	boolean normClothes=false;//this is a gloabl variable bc it is used in methods within methods that already return values.
	boolean cafCameraOff=false;
	boolean win=false;

	public static void main(String[]args){
		new AdventureMain();
	}

	AdventureMain() {

		boolean playing = true;
		boolean normClothes = false; //is the user wearing normal clothes or prison clothes
		String command = "";

		setup(); //create all objects needed, including map; print intro. message
		System.out.println("\n== " + roomList.get(currentRoom).name + " ==");
		lookAtRoom(false); //display information about the current room

		/***** MAIN GAME LOOP *****/
		while (playing) {

			command = getCommand();

			playing = parseCommand(command);
			
			//check to see if player has died (in whichever various ways the player can die)
			if (!playing) break;
			if (currentRoom.equals("GuardRoom")||currentRoom.equals("cafeteria")||currentRoom.equals("OutsideWorld")) {
				if (currentRoom.equals("GuardRoom")) {
					playing = false;
				}
				if (currentRoom.equals("cafeteria")&&cafCameraOff==false) {
					playing=false;
				}
				if (currentRoom.equals("OutsideWorld")){
					playing=false;
					win=true;
				}
			}
			else playing=guardClothes(playing);
		}
		//message for player printed out (different if they lose vs. win)
		endGame();
	}

	void setup() {
		String welcome="Welcome to Scottsvale Prison in Kerwood, Ontario. Your name is" +
				" Hergurt Hemsley and you are inmate 120192. You have been injustly sentenced "+
				"to three years in prison for trespassing in a toothpaste factory. Some helpful friend has managed to smuggle a key into your room and now is your chance to escape."+
				" Your goals is to break out of prison and win the freedom which has been so long denied of you.";
		String [] words=welcome.split(" ");
		//this is just so there is only a certain number of words per line.
		for (int i=0;i<words.length;i++) {
			System.out.print(words[i]+" ");
			if ((i+1)%12==0) System.out.println("");
		}
		System.out.println("");
		Room.setupRooms(roomList,doorList);
		Item.setUpItems(itemList, roomList);
		Doors.setUpDoors(doorList);
		currentRoom = "cell1";
		player = new Player();
	}
	
	boolean guardClothes(boolean playing) {
		playing=true;
		//If the player has not changed into guard clothes by 35 moves, they die. There is a 25 move warning 
		if (turns==25) {
			if(!normClothes)System.out.println("Beware: if you do not change out of your prison clothes and into \n"+
					"other clothes soon you will die");
		}
		if (turns==35) {
			if (!normClothes) {
				System.out.println("A guard has walked into the "+roomList.get(currentRoom).name+" - the same room you are in!"+
						"\nThe guard realizes that you are an escaped prisoner because you are still \n"+
						"wearing a bright orange prisoner's uniform. You die!");
				playing=false;
			}
			else {
				System.out.println("A guard enters the same room you are in, but luckily "+
						" you \nare weaing a guard uniform and there is only one guard, \nand so you go undetected. The guard leaves the room again.");
			}
		}
		return playing;
	}

	String getCommand() {
		Scanner sc = new Scanner(System.in);		
		String text = sc.nextLine();
		text = text.toLowerCase().trim();
		text = textReplacement(text);
		if (text.length() == 0) text = "qwerty"; //default command
		return text;
	}


	boolean parseCommand(String text) {

		/***** PREPROCESSING *****/
		//P1. 
		text = text.toLowerCase().trim();	//the complete string BEFORE parsing

		//P2. word replacement
		text = textReplacement(text);
		String words[] = text.split(" ");

		//P3. remove all instances of "THE"
		ArrayList<String> wordlist = new ArrayList<String>(Arrays.asList(words));		//array list of words
		for(int i=0; i< wordlist.size(); i++) {
			if (wordlist.get(i).equals("the")||wordlist.get(i).equals("in")||wordlist.get(i).equals("with")) wordlist.remove(i--);			
		}

		//P4. separate out into word1, word2, etc.
		String word1 = wordlist.get(0);
		String word2 = "";
		String word3 = "";
		if (wordlist.size()>1)	word2 = wordlist.get(1);
		if (wordlist.size()>2)	word3 = wordlist.get(2);

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
		case "help":
			printHelp();
			turns--;
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
			breakObject(word2,word3);
			break;
		case "take":
			takeItem(word2, word3);
			break;	
		case "use":
			useSleepinggas(word2, word3);break;
		case "change":
			normClothes=changeClothes(word2);
			if (normClothes) {
				this.normClothes=true;
				System.out.println("You are now wearing a guard uniform");
			}
			else System.out.println("You have no clothes to change into");
			break;
		case "cut":
			fenceOff = cutWires(word2);
			break;
		case "qwerty":
			System.out.println("Please enter a command. Type \"help\" if you need it.");
			turns--;
			break;
		default:
			System.out.println("Sorry, I don't understand that command");
			turns--; //you shouldn't get penalized for entering a command that doesn't work.
		}
		turns++;
		System.out.println("\nmoves: "+turns);
		if (currentRoom.equals("Guardroom")) {
			System.out.println("YOU LOSE!!!");
			return false;
		}
		return true;
	}	

	String textReplacement(String text) {
		text = text.replaceAll("pick up", "take");
		text = text.replaceAll("sleepingdust", "sleeping dust");
		text = text.replaceAll("blue dust", "sleeping dust");
		text = text.replaceAll("blue powder", "sleeping dust");
		text = text.replaceAll("pick up", "pickup");
		text = text.replaceAll(" rocks", " rock");
		text = text.replaceAll("look at", "lookat");
		text = text.replaceAll("climb up", "climbup");
		text = text.replaceAll("climbup","up");
		text = text.replaceAll("open", "unlock");
		text = text.replaceAll("move", ""); //so it will work if the user types move north instead of just north
		text = text.replaceAll("go", "");
		text = text.replaceAll("change into", "change");
		text = text.replaceAll("put on", "change");
		text = text.replaceAll("guard uniform", "clothes");
		text = text.replaceAll("guard's uniform", "clothes");
		text = text.replaceAll("uniform", "clothes");
		text = text.replaceAll("throw", "use");
		text = text.replaceAll(roomList.get(currentRoom).name.toLowerCase().trim(), "here"); //for look command doesn't work
		text = text.replaceAll("search", "look");
		text = text.replaceAll("gate", "door");
		text = text.replaceAll("room", "here");
		text = text.replaceAll("computer", "screen");
		text = text.replaceAll("screen screen", "screen");
		text = text.replaceAll("powder", "sleeping dust");
		text = text.replaceAll("sleeping sleeping", "sleeping dust");
		text = text.replaceAll("behind the bed", "here");
		text = text.replaceAll("behind bed", "here");
		text = text.replaceAll("clip", "cut");
		text = text.replaceAll("wear","change");
		text = text.replaceAll("pickup", "take");
		text = text.replaceAll("bottle", "sleeping dust");
		text = text.replaceAll("swipe", "unlock");
		text = text.replaceAll("use axe", "break");
		text = text.replaceAll("climb","up");
		text = text.replaceAll("use keycard", "unlock door");
		text = text.replaceAll("use key", "unlock door");
		text = text.replaceAll("show inventory", "i");
		text = text.replaceAll("wirecutpers", "wireclippers");//because of above replacement that is what wireclippers becomes This is what changes it back so that you can still take them
		return text;
	}


	private boolean cutWires(String word2) {
		//if the command it just "cut" it asks what they are talking about
		if (word2 == "") {
			System.out.println("cut what?");
			String cut = getCommand().toLowerCase().trim();
			word2 = cut;
		}
		/*checks to see if their are wireclippers in the inventory. If there are, and the player is in
		 * the electric room, then the electric fence is turned off.  
		 */
		boolean found=false;
		Room r = roomList.get(currentRoom);
		if(word2.equals("wires") || word2.equals("wire")) {
			for (int i = 0; i < player.inventory.size(); i++) {
				Item item = player.inventory.get(i);			
				if (item.name.equalsIgnoreCase("wireclippers")) {
					found = true;
					if(r.name=="Electric room") {
						System.out.println("You cut wires with wire clippers. The electric fence is now off. RUN FOR YOUR FREEDOM!");
						//change the exits in the room where the electric fence is to allow people to leave. Excercise Yard and also change the description
						Room r2 = roomList.get("ExerciseYard");
						r2.descr = "You are in a grassy field. A tall electric fence on the north side of the field separates you from the outside world. However, it seems to be turned off.";
						r2.u="OutsideWorld";
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

	//This method allows the user to put the single guard in the hallway to sleep.	
	private void useSleepinggas(String word2, String word3) {
		//handles case if the user only enters "use"
		if (word2 == "") {
			System.out.println("use what");
			String use = getCommand().toLowerCase().trim();
			word2 = use;
		}
		else {
			if (word3 != "") word2 = word2 + " " + word3;
		}
		//checks to see if the sleeping is in the inventory
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
						System.out.println("You use sleeping dust. The guard is now asleep. A keycard has fallen from the pocket of the \nguard - who is now asleep - and is lying in the section of hallway directly to the east of you.");
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
			if(roomList.get(currentRoom).name.equals("hallway5")) {
				System.out.println("...");
			}
		}


	}
	
	//handles case if the user wants to quit
	private void quitGame() {
		System.out.print("Do you really want to quit the game? ");
		String ans = getCommand().toUpperCase();
		if (ans.equals("YES") || ans.equals("Y")) {
			System.out.print("Thanks for playing. Bye.");
			System.exit(0);
		}	
	}
	
	//allows use to take items out of rooms
	private void takeItem(String word2, String word3) {
		if (word2.equals("")) {
			System.out.print("take what? ");			
			String comm = getCommand().toLowerCase().trim();
			word2 = comm;
		} else {
			if (word3 != "")	word2 = word2 + " " + word3;
		}

		int numItems = roomList.get(currentRoom).items.size();

		//find item in the room
		boolean found = false;
		for (int i = 0; i < numItems; i++ ) {
			Item item = roomList.get(currentRoom).items.get(i);
			if (item.name.equalsIgnoreCase(word2)) {
				player.inventory.add(item);
				if (item.name.equals("Wire Clippers"))roomList.get("storage1").descr="You are in a storage room.";
				if (item.name.equals("Key"))roomList.get("cell1").descr="You are in your cell. There is one door to the north.";
				roomList.get(currentRoom).items.remove(item);
				System.out.println(word2 + " taken");
				found = true;
				break;
			}
		}

		if (!found) System.out.println("There is no " + word2 + " here.");

	}
	
	//allows player to look around them or look in the room they are in
	private void look(String word2) {
		String com="";
		if (word2=="") {
			System.out.print("look where?");
			com = getCommand().toLowerCase();
			com = textReplacement(com);
		}
		else com=word2;
		if (com.equals("here")) {
			lookAtRoom(true);
		}
		if (com.equals("n") || com.equals("north")) {
			if (roomList.get(currentRoom).n != null) {
				String room=roomList.get(currentRoom).n;
				System.out.print("There is " + roomList.get(room).name + " north of you");
			}
			else System.out.println("There is no room north of you.");
		}
		if (com.equals("s") || com.equals ("south")) {
			if (roomList.get(currentRoom).s != null) {
				String room=roomList.get(currentRoom).s;
				System.out.print("There is " + roomList.get(room).name + " south of you");
			}
			else System.out.println("There is no room south of you.");
		}
		if (com.equals("e") || com.equals("east")) {
			if (roomList.get(currentRoom).e != null) {
				String room=roomList.get(currentRoom).e;
				System.out.print("There is " + roomList.get(room).name + " east of you");
			}
			else System.out.println("There is no room east of you.");
		}
		if (com.equals("w") || com.equals("west")) {
			if (roomList.get(currentRoom).w != null) {
				String room=roomList.get(currentRoom).w;
				System.out.print("There is " + roomList.get(room).name + " west of you");
			}
			else System.out.println("There is no room west of you.");
		}
		if (com.equals("u") || com.equals("up")) {
			if (roomList.get(currentRoom).u != null) {
				String room=roomList.get(currentRoom).u;
				System.out.print("There is " + roomList.get(room).name + " above you");
			}
			else System.out.println("There is no room above you.");
		}
		if (com.equals("d") || com.equals("down")) {
			if (roomList.get(currentRoom).d != null) {
				String room=roomList.get(currentRoom).d;
				System.out.print("There is " + roomList.get(room).name + " below you");
			}
			else System.out.println("There is no room below you.");
		}
	}

	//allows user to drop an item in a room
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
	
	//if the user types in "i" it shows their inventory
	private void showInventory() {				
		if (player.inventory.size() == 0) {
			System.out.println("There is nothing in the inventory.");
			return;
		}
		System.out.println("\n-------- INVENTORY--------");
		for (Item inven: player.inventory) {
			System.out.println(inven.name);
		}
	}
	
	//if the user types in help it will go to this method
	private void printHelp() {
		boolean haveKey=false;
		for (Item inven: player.inventory) {
			if(inven.name.equals("Key")) haveKey=true;
		}
		//if the user is stuck in there cell and hasn't even got the key yet.
		if (!haveKey)System.out.println("Type in \"look here\" to display items in room and tpye in \"take\" to pick up the key. \nYou can then use this to open the door.");
		System.out.print("North-n, East-e, West-w, Up-u, Down-d, inventory-i");
	}

	void breakObject(String word2, String word3) {
		if (currentRoom!="SecurityRoom") { //Ik this isn't supposed to work for strings, but it does here
			System.out.println("There is nothing to break here");
			return;
		}

		//you are in the correct room
		if (word3.equals("")){
			if(word2.equals("")||word2.equals("screen")) {
				System.out.print("Break computer Screen with what?");
				String comm = getCommand().toLowerCase().trim();
				word3=comm;
			}
		}

		//see if object is in inventory
		if (word3.equals("axe")) {
			boolean invFound = false;
			for (Item inven: player.inventory) {
				if(inven.name.equals("Axe")) invFound=true;
			}
			if (invFound) {
				System.out.print("Security Cameras and Computer screens smashed with Axe");
				cafCameraOff=true;
				roomList.get("cafeteria").descr="There is a security camera, but it is turned off. The room is filled with tables and chairs. \nThere's an avocado on one of the tables! (Crazy!)";
				roomList.get("SecurityRoom").descr="The computer screens were smashed into pieces by someone...";
			}
			else {
				System.out.println("Sorry, you don't have an axe in your inventory");
			}
		}
		else {
			System.out.println("You cannot break the computer screen with "+word2);
			return;
		}
		return;
		//you have the object and you are in the correct room:
	}
	
	void lookAtRoom(boolean showitems) {
		String text = roomList.get(currentRoom).descr;
		System.out.println(text);

		//list items in room only if the player types in look		
		if (showitems){
			if (roomList.get(currentRoom).items.size() > 0 ) {
				System.out.println("The room contains:");
			}
			for (Item item : roomList.get(currentRoom).items) {
				System.out.println(item.name + ": " + item.descr);
			}
		}

	}

	/* This method handles all movement between rooms.
	 * It takes a single character for direction (n,w,e,su,d)
	 * It returns a boolean: true means continue playing false means no longer playing
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

		if(currentRoom.equals("ExerciseYard") && c=='u') {
			boolean invFound = false;
			for (int i = 0; i < player.inventory.size(); i++) {
				Item item = player.inventory.get(i);
				if (item.name.equalsIgnoreCase("Chair")) invFound = true;					
			}
			if(invFound && fenceOff) {
				currentRoom = r.u;
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
			if(currentRoom.equals(doorList.get(0).loc1)||currentRoom.equals(doorList.get(0).loc2)) {
				if (doorList.get(0).unlocked==false) {
					System.out.print("You can not move there. Your cell door is locked.");
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
		lookAtRoom(false);
		return true;
	}
	
	//allows user to open door
	void unlock(String w2, String w3, ArrayList<Doors>doorList) {
		if (w2.equals("")) {
			System.out.print("take what? ");			
			String comm = getCommand().toLowerCase().trim();
			w2 = comm;
		}
		if (w2.equals("door")) {
			//do they have the key/keycard
			boolean haveKey=false;
			boolean haveKeyCard=false;
			for (Item inven: player.inventory) {
				if(inven.name.equals("Key")) haveKey=true;
				if (inven.name.equals("Keycard")) haveKeyCard=true;
			}
			//cell door -->need key
			if(currentRoom.equals(doorList.get(0).loc1)||currentRoom.equals(doorList.get(0).loc2)) {
				if (haveKey) {
					doorList.get(0).unlocked=true;
					System.out.print("Door unlocked");
					roomList.get(currentRoom).descr="This was your original cell";
				}
				else System.out.print("You don't have a key");
			}
			//caf door -->need keycard
			else if (currentRoom.equals(doorList.get(1).loc1)||currentRoom.equals(doorList.get(1).loc2)) {
				if (haveKeyCard) {
					doorList.get(1).unlocked=true;
					System.out.print("Door unlocked");
					roomList.get(currentRoom).descr="There is a cafeteria to the north.";
				}
				else System.out.println("You need a guard's key card to unlock this.");
			}
			//basement door --> need keycard
			else if (currentRoom.equals(doorList.get(2).loc1)||currentRoom.equals(doorList.get(2).loc2)) {
				if (haveKeyCard) {
					doorList.get(2).unlocked=true;
					System.out.print("Door unlocked");
					roomList.get(currentRoom).descr="There is hallway to the east and west.";
				}
				else System.out.println("You need a guard's key card to unlock this.");
			}
			else System.out.println("There are no doors nearbye to unlock.");
		}
		else System.out.println("Sorry, I don't understand that command");
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
	
	//allows user to change clothes so they won't die at 35 turns
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
	
	//message printed out at end of game
	void endGame() {
		if (win) {
			System.out.println("\n== " + roomList.get(currentRoom).name + " ==");
			lookAtRoom(false);
			System.out.println("Congratulations! You won the game! You are incredible, phenominal, \n"+
					"absolutely extraordinary. Annnnnddd you won in ONLY " + turns + " moves. That is truly\n"+
					"a feat to be proud of. You have escaped, now go and enjoy freedom, and say farewell to \n" +
					"your toothpaste tresspassing past. Now is your time to show the world who you really are. \n"+
					"You are going to do fantastic things in the world, all you have to do is believe in yourself.");
		}
		else System.out.println("Sorry, you lost. \\_o_/\nGuess you're going to have to try again.");
	}

}
