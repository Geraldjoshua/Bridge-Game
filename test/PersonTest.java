
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PersonTest{
	Person tester = new Person("North");
	Person test = new Person();

	@Test
	public void checkingInHand(){
		

		//return false because the hand is empty
		System.out.println("Test: check card in hand, should be false");
		assertEquals(false,tester.inHand("4D"));
		assertEquals(false,test.inHand("4D"));
	}

	@Test
	public void checkingTrickIncrement(){
		//Person tester = new Person("North");

		// return false 
		System.out.println("Test:increment wontrick from zero to 1");
		tester.incrementTrickWins();
		assertEquals(1,tester.getTrickWins());
	}

	// @Test
	// public void checkingPrintHand(){
	// 	//Person tester = new Person("North");

	// 	// return false 
	// 	System.out.println("Test:printing hand");
	// 	tester.incrementTrickWins();
	// 	assertEquals(1,tester.getTrickWins());
	// }
}