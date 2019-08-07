import java.util.ArrayList;

public class Person{

	private ArrayList<Card> hand;
	private int points;
	private String name;

	Person(){
		
		this.hand = new ArrayList<Card>();
		this.points = 0;
		this.name = "";
	}

	Person(String name){

		this.hand = new ArrayList<Card>();
		this.name=name;
		this.points = 0;

	}	

	Person(ArrayList<Card> hand,String name){
		this.hand = new ArrayList<Card>(hand);
		this.points = 0;
		this.name = name;
	}

	public void setName(String name){
	
		this.name=name;
	
	}

	public void setHand(ArrayList<Card> hand){

		this.hand = new ArrayList<Card>(hand);

	}

	public String getPersonName(){

		return name;	

	}

	public ArrayList<Card> getPersonHand(){

		return hand;

	}

	public int getPersonPoints(){

		return points;	

	}

	public void incrementPoints(){
		
		points++;		

	}
	public int getPoints(){
		
		return points;		

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

	public boolean checksuit(String card,ArrayList<String> playedCards){
		boolean check = true;
		//Trick newbid = new Trick();
		int no_suit_in_card = 0;
		//System.out.println(Trick.getBid());
		String firstcard = playedCards.get(0);
		if(firstcard.charAt(1)==card.charAt(1)){	
					check = false;
		}
		else if (card.charAt(1)==Trick.getBid().charAt(1)){
			check = false;
		}
		else{
			for(int i=0; i<hand.size();i++){
				if(!(hand.get(i).toString().charAt(1)==firstcard.charAt(1))){
					no_suit_in_card++;
				}
			}
			if (hand.size()==no_suit_in_card){
				check = false;
			}

		}
		return check;
	}



}
