
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;

public class PersonTest{
	
	Person tester = new Person("North");
	Person test = new Person();
	
	@BeforeClass
        public static void setUpBeforeClass() throws Exception {
		System.out.println("Testing methods in the Person Class: ");
	}

	@Test
	public void checkingTrickIncrement(){
		//Person tester = new Person("North");

		// return false 
		System.out.println("Test 1:increment wontrick from zero to 1");
		tester.incrementTrickWins();
		assertEquals(1,tester.getTrickWins());
	}

	@Test
	public void checkingInHand(){
		

		//return false because the hand is empty
		System.out.println("Test 2: check card in hand, should be false");
		assertEquals(false,tester.inHand("4D"));
		assertEquals(false,test.inHand("4D"));
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
