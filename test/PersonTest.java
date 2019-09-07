
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.io.IOException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
import java.util.ArrayList;

public class PersonTest{
	
	Person tester = new Person("North");
	Person test = new Person();
	
	@BeforeClass
        public static void setUpBeforeClass() throws Exception {
		System.out.println("Testing methods in the Person Class: ");
	}

	@Test
	public void checkingTrickIncrement(){
		System.out.println("Test 1:incrementTrickWins(): increments tricks from zero to 1");
		tester.incrementTrickWins();
		assertEquals(1,tester.getTrickWins());
	}

	@Test
	public void testRemovePlayedCards() throws IOException{
		
		System.out.println("Test 2:removePlayedCard() method: should return true since the hand is empty ");
		ArrayList<Card> hand = new ArrayList<>();
		hand.add(new Card('S','A'));
		hand.add(new Card('D','5'));
		hand.add(new Card('D','2'));
		tester.setHand(hand);
		tester.removePlayedCard("AS");
		assertEquals(false,tester.inHand("AS"));
		
	}

	@Test
	public void testAddPoints(){
		System.out.println("Test 3: AddPoints() method: points for north set to 2");
		tester.addPoints(2);
		assertEquals(2,tester.getPoints());
	}

	@Test
	public void checkingInHand(){
		

		//return false because the hand is empty
		System.out.println("Test 4: check card in hand, should be false");
		assertEquals(false,tester.inHand("4D"));
		assertEquals(false,test.inHand("4D"));
	}

	@Test
	public void testPlayedCard(){
		System.out.println("Test 5: recordplayedCard(),getrecordplayedCard() methods: assert if AS is the card played ");
		tester.recordplayedCard("AS");
		assertEquals("AS",tester.getrecordplayedCard());
	}


	@Test
	public void testIncrementPoints(){
		
		//System.out.println("Test : no getter for it");
		tester.incrementPoints("AS");
		//this method is not used  and has no getter method for handstrength
	}

	@Test
	public void testNoSuit(){
		
		System.out.println("Test 6:noSuit() method: should return true since the hand is empty ");
		tester.noSuit('S');
		assertEquals(true,tester.noSuit('S'));
		
	}

	// @Test
	// public void testRemovePlayedCards() throws IOException{
		
	// 	System.out.println("Test 7:removePlayedCard() method: should return true since the hand is empty ");
	// 	ArrayList<Card> hand = new ArrayList<>();
	// 	hand.add(new Card('S','A'));
	// 	hand.add(new Card('D','S'));
	// 	hand.add(new Card('D','2'));
	// 	tester.setHand(hand);
	// 	//this method isn't used in the code.
	// 	assertEquals(true,tester.checkhand("AS"));
		
	// }



}
