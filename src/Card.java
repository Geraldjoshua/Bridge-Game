public class Card{

	private char suit;
	private char value;
	private int pointValue;

	Card(char suit, char value){
		this.suit = suit;
		this.value = value;
		setPointValue(value);
	}

	public char getSuit(){
		return suit;	
	}

	public char getValue(){
		return value;
	}

	public void setPointValue(char value){
		//Possible better way to do this other than an if tree ? 
		if(value == 'A'){
			pointValue = 4;
		}
		else if(value == 'K'){
			pointValue = 3;		
		}
		else if(value == 'Q'){
			pointValue = 2;
		}
		else if(value == 'J'){
			pointValue = 1;
		}
		else{
			pointValue=0;
		}
	}

	public int getPointValue(){
		return pointValue;	
	}

	public String toString(){
		return value + "" +suit;
	}

	//To string method to print cards with suite chracters
	public String toString(boolean check){
		
		if(suit == 'S'){
			return '\u2660'+" "+value;
		}else if(suit == 'D'){
			return '\u2666'+" "+value;
		}else if(suit == 'C'){
			return '\u2663'+" "+value;
		}else {
			return '\u2665'+ " "+value;
		}		

	}
}
