import java.util.ArrayList;

public class Person{

	private ArrayList<Card> hand;
	private int points;
	private String name;

	Person(){
		hand = new ArrayList<Card>();
		points = 0;
		name = "";
	}
	Person(ArrayList<Card> hand,name){
		this.hand = new ArrayList<Card>(hand);
		this.points = 0;
		this.name = name;
	}

}
