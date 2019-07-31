import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class Game{
	public static void main(String args[]){

		//Create game setup
		Person[] players = {new Person("West"),new Person("North"),new Person("East"),new Person("South")};
		ArrayList<Card> cardArray = new ArrayList<Card>(13);
		
		Scanner userinput = new Scanner(System.in);
		System.out.println("Enter level: ");
		String level = userinput.nextLine();
		String lesson_file = "input/input"+level+".txt";
		try{
			Scanner lesson = new Scanner(new File(lesson_file));
			System.out.println("The bidding is: ");
			String bidding = lesson.nextLine();
			System.out.println(bidding);
			int number_of_hands=3;

			while (number_of_hands>=0){
				String hand = lesson.nextLine();
				String[] hand_split = hand.split(",");
				for (String specific_suit_cards  : hand_split){
					int length = specific_suit_cards.length();
					for(int i=1; i<length;i++){
						Card card = new Card(specific_suit_cards.charAt(0),specific_suit_cards.charAt(i));
						cardArray.add(card);
					}
				}
				players[number_of_hands].setHand(cardArray);
				System.out.println(players[number_of_hands].getPersonHand());
				number_of_hands--;
				cardArray.clear();
				

			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}		
	
	}
}
