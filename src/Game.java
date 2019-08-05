import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.lang.StringBuilder;

public class Game{

	public static void main(String args[]){
		
		Scanner userinput = new Scanner(System.in);
		System.out.println("Enter level: ");
		String level = userinput.nextLine();

		//created a lesson object to read lesson
		Lesson lesson_class = new Lesson();
		lesson_class.loadlesson(level);
		Person[] players = Lesson.getplayers();
		ArrayList<Card> cardArray = Lesson.getCardArray();

		//stores all the tricks obtained from reading the lesson
		ArrayList<Trick> trickArray = Lesson.getTrickArray();


		//counts a tick played by a person in a particular game(better name will be found later for it)
		int temp_trick=0;
		int player_number=0;
		//holds played cards
		ArrayList<String> playedCards = new ArrayList<>();
		//for loop to state number of tricks
		for(int j=0; j<trickArray.size();j++){
			while(temp_trick<=4){
				if(playedCards.size()==4){
					playedCards.clear();
					player_number=0;
					temp_trick=0;
					break;
				}
				
				Person player = players[player_number];
				String displayHand = displayhand(player); //displays hand to the console
				//System.out.println(displayHand);
				System.out.println("It is "+ player.getPersonName()+ " turn");
				//System.out.println("cards played:"+getplayedCards(playedCards));
				//System.out.println("Please Enter the corret suit card to play: ");
				String cardplayed="";
				//gets first card played by west when the game starts
				if((j==0)&&(temp_trick==0)){
					cardplayed = trickArray.get(j).getWest();
					System.out.println("west played first  automatically");
				}
				else{
					boolean checkuserdeck = true;
					while(checkuserdeck){
						System.out.println(player.getPersonName() +" player Please Enter the correct suit card in your hand to play: ");
						System.out.println("cards played:"+getplayedCards(playedCards));
						System.out.println(displayHand);
						cardplayed = userinput.nextLine();  //gets input from user
						checkuserdeck=player.checkhand(cardplayed.trim());
						if (!checkuserdeck){
							checkuserdeck = player.checksuit(cardplayed.trim(),playedCards);
						}
					}
				}
				//checks if the potential right card in the lesson is played by the user
				boolean checkcardtrick = Trick.checktrick(cardplayed.trim(),temp_trick,trickArray.get(j));
				if (!checkcardtrick){
					//what player could play 
					String potential = Trick.potentialplay(cardplayed.trim(),temp_trick,trickArray.get(j));
					System.out.println(potential+" was the better option");
				}
				playedCards.add(cardplayed.trim());
				player.removePlayedCard(cardplayed.trim());
				temp_trick++;
				player_number++;


			}	
		}	
	
	}

	static String displayhand(Person player){
		ArrayList<Card> hand_of_player = new ArrayList<Card>(player.getPersonHand());
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<hand_of_player.size();i++){
					
			if (hand_of_player.get(i).getSuite()=='S'){
				sb.append('\u2660');
						//System.out.println("\u2660");
			}
			if (hand_of_player.get(i).getSuite()=='C'){
				sb.append('\u2663');
						//System.out.println("\u2663");
			}
			if (hand_of_player.get(i).getSuite()=='H'){
				sb.append('\u2665');
						//System.out.println("\u2665");
			}
			if (hand_of_player.get(i).getSuite()=='D'){
				sb.append('\u2666');
						//System.out.println("\u2666");
				}
			int value = hand_of_player.get(i).getPointValue();
			if(value>0){
				if (value==4){
					sb.append('A');
						
				}
				if (value==3){
					sb.append('K');
						
				}
				if (value==2){
					sb.append('Q');
						
				}
				if (value==1){
					sb.append('J');
						
				}
			}
					
			else{
				sb.append(hand_of_player.get(i).getValue());
			}
			sb.append(" ");
					
		}
		return sb.toString();

	}


	static String getplayedCards(ArrayList<String> played){
		String temp = "";
		for(int i=0;i<played.size();i++){
			temp+=" ";
			temp+=played.get(i);
			//temp+=" ";
		}
		return temp;
	}
}
