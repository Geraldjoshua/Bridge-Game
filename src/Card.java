import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
* card class to instantiate cards used in the game
* @author Chris Cushway
* @author Gerald Ngumbulu
* @author Blessed Chitamba
* @version 1.0
*/

public class Card{

	private char suit;
	private char value;
	private int pointValue;
	private BufferedImage cardImage;

	/**
 	 * <p>constructor for the card class </p>
	 * @param suit The card's suite
	 * @param value the card's value
	 * @throws java.io.IOException when card image does not exist
	 * 
	 */
	Card(char suit, char value) throws IOException {
        this.suit = suit;
        this.value = value;
        this.cardImage = ImageIO.read(new File("cardImages/"+value+""+suit+".png"));
        setPointValue(value);
    }


	/**
 	 * <p>getter to get to the suit of a card </p>
	 * @return char Contains card's suite
	 * 
	 */
	public char getSuit(){
		return suit;	
	}


	/**
 	 * <p>getter to get the value of the card </p>
	 * @return char Contains card's value eg ('J','K')
	 * 
	 */
	public char getValue(){
		return value;
	}

	/**
 	 * <p>sets trick points for the card's value eg ('A=4','J=1') </p>
	 * @param value The card's value that needs to be translated to it equivalent points
	 * 
	 */
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


	/**
 	 * <p>get the trick points for a card </p>
	 * @return int Trick Points for the card
	 * 
	 */
	public int getPointValue(){
		return pointValue;	
	}


	/**
 	 * <p>gets the image of the card </p>
	 * @return BufferedImage of the card
	 * 
	 */
	public BufferedImage getCardImage(){
		return cardImage;
	}


	/**
 	 * <p>calls the tostring on how to display the card  </p>
	 * @return String displaying the card with its value and suit
	 * 
	 */
	public String toString(){
		return value + "" +suit;
	}


	/**
 	 * <p>Overloaded toString method to print cards with suit characters</p>
	 * @param check Boolean that enables the call of the overloaded method
	 * @return String card displayed with its suit characters 
	 * 
	 */
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
