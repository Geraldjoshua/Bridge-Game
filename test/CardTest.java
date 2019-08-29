import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;

public class CardTest{

		@BeforeClass
        public static void setUpBeforeClass()  {
			System.out.println("Testing methods in the Card Class: ");
		}

		@Test
		public void testToString() throws IOException{
			System.out.println("Test 1:toString() method:produces a string character of the card.");
			Card test = new Card('S','A');

			assertEquals("AS",test.toString());
			boolean check = true;
			assertEquals("♠ A",test.toString(check));
			 
		}

		@Test
		public void testSetPointValue() throws IOException{
			System.out.println("Test 2:setPointvalue() method:sets trick points to the cards.");
			Card test = new Card('S','A');
			assertEquals(4,test.getPointValue());
			 
		}

}