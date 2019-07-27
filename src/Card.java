public class Card{

	private char suite;
	private char value;
	private int pointValue;

	Card(char suite, char value){
		this.suite = suite;
		this.value = value;
		setPointValue(value);
	}

	public char getSuite(){
		return suite;	
	}

	public char getValue(){
		return value;
	}

	public void setPointValue(char value){
		//Possible better way to do this other than an if tree ? 
		if(value != 'A' && value != 'K' && value != 'Q' && value != 'J'){
			pointValue = 0;		
		}else if(value == 'A'){
			pointValue = 4;
		}else if(value == 'K'){
			pointValue = 3;		
		}else if(value == 'Q'){
			pointValue = 2;
		}else if(value == 'J'){
			pointValue = 1;
		}
	}

	public int getPointValue(){
		return pointValue;	
	}

	public String toString(){
		return suite + "" +value;
	}
}
