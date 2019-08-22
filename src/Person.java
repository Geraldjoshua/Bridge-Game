import java.util.ArrayList;
import java.util.HashSet;

public class Person{

	private ArrayList<Card> hand;
	private int points;
	private String name;
	private String playedcard;
	private boolean isWinner = false;
	private int trickWins;
	private int handStrength;

	Person(){
		
		this.hand = new ArrayList<Card>();
		this.points = 0;
		this.name = "";
		this.trickWins = 0;
		this.handStrength = 0;
	}

	Person(String name){

		this.hand = new ArrayList<Card>();
		this.name=name;
		this.points = 0;
		this.trickWins = 0;
		this.handStrength = 0;

	}	

	Person(ArrayList<Card> hand,String name){
		this.hand = new ArrayList<Card>(hand);
		this.points = 0;
		this.name = name;
	}

	public void setName(String name){
	
		this.name=name;
	
	}
	//increases trick win by one
	public void incrementTrickWins(){
		this.trickWins++;
	}

	public Card getCard(int index){
		
		return hand.get(index);

	}

	public Card getCard(String card){

		for(int i=0;i<hand.size();i++){
			if(hand.get(i).toString().equals(card)){
				
				return hand.get(i);
			}
		}	
		return null;
	}

	public int getTrickWins(){

		return this.trickWins;	

	}
	
	//set trick wins if need be (used in case of claim)
	//When claim is done
	//The person wins the remainder of the tricks playable
	//i.e 13 - indexClaimedOn + pointsPlayerCurrentlyHas
	public void setTrickWins(int num){
		
		this.trickWins=num;	

	}
	//Prints hand with suite characters
	public void printNiceHand(){
		for(Card card:hand){

			System.out.print(card.toString(true)+" ");		
	
		}

	}
	//Sets the hand
	public void setHand(ArrayList<Card> hand){

		this.hand = new ArrayList<Card>(hand);

	}

	public String getPlayerName(){

		return name;	

	}

	public ArrayList<Card> getPlayerHand(){

		return hand;

	}
	
	public void setWinner(){

		this.isWinner = true;

	}

	public boolean getWinner(){
		
		return this.isWinner;	

	}

	public void resetWinner(){
		
		this.isWinner = false;		

	}

	public int getPersonPoints(){

		return points;	

	}
	//Method to be used later for hand strength functionality
	public void incrementPoints(String card){
		if(card.charAt(0)=='A'){
			handStrength+=4;
		}
		else if(card.charAt(0)=='K'){
			handStrength+=3;
		}
		else if(card.charAt(0)=='Q'){
			handStrength+=2;
		} 
		else if(card.charAt(0)=='J'){
			handStrength+=1;
		}
		else{
			handStrength+=0;
		}		

	}
	public int getPoints(){
		
		return points;		

	}

	public void setPoints(int number){
		
		this.points = number;

	}
	
	public void addPoints(int points){
		this.points+=points;
	}

	public void recordplayedCard(String card){
		this.playedcard = card;
	}

	public String getrecordplayedCard(){
		return playedcard;
	}
	
	//See if card is in the players hand
	public boolean inHand(String card){
		for(int i=0;i<hand.size();i++){
			if(hand.get(i).toString().equals(card)){
				hand.remove(i);
				return true;
			}
		}
		return false;
	}
	public String bestCaseInHand(ArrayList<String> bestcard){
		HashSet<String> hset = new HashSet<>();
		for(int i=0;i< hand.size();i++){
			hset.add(hand.get(i).toString());
		}
		//bestcardarray
		for(int i=0;i<bestcard.size();i++){
			if(hset.contains(bestcard.get(i))){
				return bestcard.get(i);
			}
			
		}
		return "There is no best case";
	}

	

	public void removePlayedCard(String card ){
		//ArrayList<Card> hand = getPersonHand();
		for(int i=0;i<hand.size();i++){
			if(hand.get(i).toString().equals(card)){
				hand.remove(i);
			}
		}
	}
	public boolean checkhand(String card){
		boolean check = true;
		for(int i=0;i<hand.size();i++){
			if(hand.get(i).toString().equals(card))
				check = false;
		}
		return check;
	}

	

}
