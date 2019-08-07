import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.lang.StringBuilder;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;

public class Lesson{

	private  Person[] players={new Person("West"),new Person("North"),new Person("East"),new Person("South")}; 
	private  ArrayList<Card> cardArray;
	private  ArrayList<Trick> trickArray;

	Lesson(){
		//players = new Person[4]; //{new Person("West"),new Person("North"),new Person("East"),new Person("South")};
		cardArray = new ArrayList<Card>(13);
		trickArray = new ArrayList<>();
	}



	void loadlesson(String lesson_number){
		
		
		
		String lesson_file = "input/input"+lesson_number+".txt";
		try{
			Scanner lesson = new Scanner(new File(lesson_file));
			System.out.println("The bidding is: ");
			String bidding = lesson.nextLine();
			String[] biddingl = bidding.split(" ");
			ArrayList<String> biddingstring = new ArrayList<>(Arrays.asList(biddingl));
			Collections.reverse(biddingstring);
			//Trick bidcard = new Trick();
			for(int j=0;j<biddingstring.size();j++){
				if(j==3){
					Trick.setBid(biddingstring.get(j));
					break;
				}
			}
			System.out.println(bidding);
			int number_of_hands=0;

			//reading hands
			while (number_of_hands<4){
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
				number_of_hands++;
				cardArray.clear();
				

			}
			//reading tricks assuming west always starts
			Trick trick;
			while(lesson.hasNextLine()){
				String trick_game = lesson.nextLine();
				if(trick_game.trim().equals("CLAIM")){
					trick = new Trick(trick_game.trim());
					trickArray.add(trick);
					break;
				}
				String[] trick_split = trick_game.split(",");
				trick = new Trick(trick_split[0],trick_split[1],trick_split[2],trick_split[3]);
				trickArray.add(trick);

			}
			//System.out.println(trickArray.size());

		} catch (IOException e) 
		{
			e.printStackTrace();
		}

	}

	public ArrayList<Card> getCardArray(){

		return cardArray;	

	}
	public ArrayList<Trick> getTrickArray(){

		return trickArray;	

	}
	public Person[] getplayers(){

		return players;	

	}


}
