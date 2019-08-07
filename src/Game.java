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
		Person[] players = lesson_class.getplayers();
		ArrayList<Card> cardArray = lesson_class.getCardArray();

		//stores all the tricks obtained from reading the lesson
		ArrayList<Trick> trickArray = lesson_class.getTrickArray();


		//counts a tick played by a person in a particular game(better name will be found later for it)
		int temp_trick=0;
		//int[] player_to_start = {0,1,2,3};
		int player_number=0;
		//holds played cards
		ArrayList<String> playedCards = new ArrayList<>();
		//for loop to state number of tricks
		for(int j=0; j<trickArray.size();j++){
			while(temp_trick<=4){
				if(playedCards.size()==4){
					players = countpoints(playedCards,players);
					System.out.println(players[0].getPersonName()+" player won the previous trick");
					playedCards.clear();
					players[0].incrementPoints();
					//System.out.println(players[0].getPoints());
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
						System.out.println("cards played:"+getplayedCards(playedCards));
						System.out.println(player.getPersonName() +" player Please Enter the correct suit card from your hand to play: ");
						System.out.println(displayHand);
						cardplayed = userinput.nextLine();  //gets input from user
						checkuserdeck=player.checkhand(cardplayed.trim());
						if ((!checkuserdeck)&&(playedCards.size()>0)){
							checkuserdeck = player.checksuit(cardplayed.trim(),playedCards);
						}
					}
				}
				//checks if the potential right card in the lesson is played by the user
				Trick trick = new Trick();
				boolean checkcardtrick = trick.checktrick(cardplayed.trim(),temp_trick,trickArray.get(j));
				if (!checkcardtrick){
					//what player could play 
					String potential = trick.potentialplay(cardplayed.trim(),temp_trick,trickArray.get(j));
					System.out.println(potential+" was the better option");
				}
				playedCards.add(cardplayed.trim());
				player.removePlayedCard(cardplayed.trim());
				//player_number=player_to_start[temp_trick];
				temp_trick++;
				//if(temp_trick<=3){
				player_number++;//player_to_start[temp_trick];}
				


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

	public static Person[] countpoints(ArrayList<String> playedcards,Person[] players){
		int[] temparray = new int[4];
		Person[] new_person_array = new Person[4];
		ArrayList<Integer> temporder = new ArrayList<>();
		for (int i=0;i<playedcards.size();i++){
			if(playedcards.get(i).substring(1,2).trim().charAt(0)==Trick.getBid().charAt(1)){
				temporder.add(15);
			}
			else if(playedcards.get(i).substring(0,1).trim().equals("K")){
				temporder.add(13);
			}
			else if(playedcards.get(i).substring(0,1).trim().equals("Q")){
				temporder.add(12);
			}
			else if(playedcards.get(i).substring(0,1).trim().equals("J")){
				temporder.add(11);
			}
			else if(playedcards.get(i).substring(0,1).trim().equals("T")){
				temporder.add(10);
			}
			else if(playedcards.get(i).substring(0,1).trim().equals("A")){
				temporder.add(14);
			}
			else{
				temporder.add(Integer.parseInt(playedcards.get(i).substring(0,1)));
			}
		}
		int played=0;
		int rem=0;
		int max=0;
		int index=0;
		while(played<4){
				max = temporder.get(0);
				index = 0;
				for (int i=0;i<temporder.size();i++){
					if(max<temporder.get(i)){
						max=temporder.get(i);
						index=i;
					}
				}
			index+=played;
			if(index<4){
				temparray[played]=index;
				new_person_array[played]=players[index];
				
			}
			else{
				rem=index-4;
				temparray[played]=rem;
				new_person_array[played]=players[rem];

			}
			played++;
		}
		return new_person_array;


	}
}
