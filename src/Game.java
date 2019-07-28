import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;


public class Game{
	
	
	public static void main(String[] args){
		ArrayList<Card> cardArray = new ArrayList<>();
		Scanner userinput = new Scanner(System.in);
		System.out.println("Enter level: ");
		String level = userinput.nextLine();
		String lesson_file = "input"+level+".txt";
		try{
			Scanner lesson = new Scanner(new File(lesson_file));
			System.out.println("The bidding is: ");
			String bidding = lesson.nextLine();
			System.out.println(bidding);
			int number_of_hands=4;
			
			while (number_of_hands>0){
				String hand = lesson.nextLine();
				String[] hand_split = hand.split(",");
				for (String specific_suit_cards  : hand_split){
					int length = specific_suit_cards.length();
					for(int i=1; i<length;i++){
						Card card = new Card(specific_suit_cards.charAt(0),specific_suit_cards.charAt(i));
						cardArray.add(card);
						//System.out.println(card.toString());
					}
				}
				switch(number_of_hands) {
					case 4:
						Person west = new Person(cardArray,"west");
						break;
					case 3:
						Person north = new Person(cardArray,"north");
						break;
					case 2:
						Person east = new Person(cardArray,"east");
						break;
					case 1:
						Person south = new Person(cardArray,"south");
						break;
						}
				number_of_hands--;
				cardArray.clear();
				
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}		
		
		
	
	}


}
