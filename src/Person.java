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



}
