
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Lesson{

	private ArrayList<Person> players = new ArrayList<Person>(4);
	private ArrayList<Card> cardArray = new ArrayList<Card>(13);
	private ArrayList<String[]> bestCase = new ArrayList<String[]>();
	private String firstCardPlayed;
	private char biddingSuite;

	Lesson(String filename) throws IOException{	
		players.add(new Person("West"));
		players.add(new Person("North"));
		players.add(new Person("East"));
		players.add(new Person("South"));	
		loadInput(filename);	
	}

	//function to parse in input - only goes till best plays no hints yet
	public void loadInput(String filename) throws IOException{

		Scanner lesson = new Scanner(new File(filename));
		System.out.println("The bidding is: ");
		String bidding = lesson.nextLine();
		String[] Bidding = bidding.split(" ");
		setBidding(Bidding);
		System.out.println(bidding);
		//number of hands is number of players
		int number_of_hands=0;
		//Loop to initialise number of players in the game
		while (number_of_hands<=3){
			String hand = lesson.nextLine();
			String[] hand_split = hand.split(",");
			for (String specific_suit_cards  : hand_split){
				int length = specific_suit_cards.length();
				for(int i=1; i<length;i++){
					Card card = new Card(specific_suit_cards.charAt(0),specific_suit_cards.charAt(i));
					cardArray.add(card);
				}
			}
			players.get(number_of_hands).setHand(cardArray);
			//System.out.println(players.get(number_of_hands).getPlayerHand());
			number_of_hands++;
			cardArray.clear();
		}

		firstCardPlayed = lesson.nextLine();

		//Loop to get best possible plays into an array
		int maxNumOfTricksLeft=13;
		while(maxNumOfTricksLeft>0){
			String trick = lesson.nextLine();
			String[] singleTrick = trick.split(",");
			//System.out.println(Arrays.toString(singleTrick));

			//Check if claim has been played or invalid input (Will only happen before all 13 tricks have been played)
			if(singleTrick.length==1){
				if(singleTrick[0].equals("CLAIM")){
					bestCase.add(singleTrick);
				}
				break;
			}

			bestCase.add(singleTrick);
			maxNumOfTricksLeft--;
			
		}

		/*Rest of input will be parsed here*/
	
	}

	//Method to return best case to be played
	public ArrayList<String> getBestCase(){
		ArrayList<String> BestCase = new ArrayList<String>();
		for(String[] trick:bestCase){
			for(String card:trick){
				BestCase.add(card);			
			}
		}
		return BestCase;	

	}

	public ArrayList<String> getBestCase1(int trick){
		ArrayList<String> BestCase = new ArrayList<String>();
		if(trick<bestCase.size()){
			for(String card:bestCase.get(trick)){
					BestCase.add(card);	

				}
			
		}
		else{
			BestCase.add("No tricks!!");
		}
		return BestCase;	

	}
	
	//first card of trick decides suite so we need this
	public String getFirstCardPlayed(){
		
		return this.firstCardPlayed;		

	}

	//See if play is valid
	public boolean isValid(String card , Person player){
		//play can be suite from first play or trump suite
		if(player.inHand(card) && (card.charAt(1) == getSuite() || card.charAt(1) == getBiddingSuite())){
			return true;
		}else{
			return false;
		}	

	}

	//Get points for card played
	public int getPlayPoints(String card){
		int points=0;
		if(card.charAt(1)==getBiddingSuite()){
			points+=15;
		}
		if(card.charAt(0)=='A'){
			points+=14;
		}else if(card.charAt(0)=='K'){
			points+=13;
		}else if(card.charAt(0)=='Q'){
			points+=12;
		}else if(card.charAt(0)=='J'){
			points+=11;
		}else if(card.charAt(0)=='T'){
			points+=10;
		}else{
			points+=Character.getNumericValue(card.charAt(0)); 
		}

		return points;

	}
	
	//Sets bidding suite
	public void setBidding(String[] bidding){
		//Bid always ends in 3PA so the suite just before that dictates the bid
		//Reverse the array since we can't predict the bid from the length of the array but from the 3PA at the end. 
		//So the smarter way(i think) is to reverse it first it. 
		ArrayList<String> biddingstring = new ArrayList<>(Arrays.asList(bidding));
		Collections.reverse(biddingstring);
		String card = biddingstring.get(3);
		if(card.charAt(1)=='N'){
			biddingSuite = card.charAt(1);
		}
		else{
			biddingSuite = card.charAt(1);
		}
		// if(bidding.length>4){
		// 	String card = bidding[bidding.length - 4];
		// 	biddingSuite = card.charAt(1);
			
		// }else{
		// 	//Need to do special string parse for no bidding case in format:
		// 	//S:NA NA NA NA;
		// 	//Just set to no bid for now
		// 	biddingSuite ='N';
		// }		
	
	}

	public void checkClaim(String card){
		//To Do
	}

	
	public char getBiddingSuite(){

		return this.biddingSuite;		

	}

	public void setFirstCardPlayed(String card){
		
		this.firstCardPlayed = card;

	}

	public char getSuite(){
		
		return this.firstCardPlayed.charAt(1);		
	
	}

	public ArrayList<Person> getPlayers(){

		return this.players;

	}
	
	//Winner of trick based on boolean inside person class
	//Therefore need to set it back to false after trick so we dont have more than one winner
	public void resetWinner(){
		
		for(Person player : players ){
			player.resetWinner();
		}		

	}
	//Sets winner manually if need be
	public void setWinner(String playername){

		for(Person player : players){
			if(player.getPlayerName().equals(playername)){
				player.setWinner();
				player.incrementTrickWins();
			}
		}
		
	}
	//Decides winner of trick based on points from cards played
	public void decideWinner(){
		Person winner = new Person();
		int points=0;
		for(Person player:players){
			if(player.getPoints()>points){
				points=player.getPoints();
				winner = player;			
			}
		}
		System.out.println(winner.getPlayerName() + " Wins the trick!");
		setWinner(winner.getPlayerName());
		resetPoints();
	}
	
	//Decides winner of entire game by counting trick points
	public void decideGameWinner(){
		Person winner = new Person();
		int trickWins=0;
		for(Person player:players){
			if(player.getTrickWins()>trickWins){
				trickWins=player.getTrickWins();
				winner = player;			
			}
		}
		System.out.println("\n" + winner.getPlayerName() + " IS THE WINNER OF THE GAME");

	}
	//Resets points for trick so points don't carry over
	public void resetPoints(){
		for(Person player : players){
			player.setPoints(0);
		}

	}

	public void printPlayers(){
		for(int i=0;i<players.size();i++){
			System.out.println(players.get(i).getPlayerName());
		}
	}

	public void reorderPlayers(){

		for(int i=0;i<players.size();i++){
			Person tempPlayer = players.get(0);
			if(!players.get(0).getWinner()){
				players.remove(0);
				players.add(tempPlayer);
			}
		
		}
		resetWinner();

	}

	


}


