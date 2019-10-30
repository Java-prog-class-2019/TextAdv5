
public class Testing {
	public static void main(String[]args) {
		
		int[] numbers = {5,6,7,9,10,8};
		
		//find out if there is a 7 in numbers
		boolean a=false;
		for(int i=0; i <numbers.length; i++) {
			if(numbers[i] == 7) {
				a=true;								
			}
		}
		if(a==true) {
			System.out.println("yes");
		}else {
			System.out.println("no");
		}
		
		//Y --> print "yes"-- only once
		//N --> print "no", but only print this once
	
	}
}
