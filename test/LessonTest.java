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

public class LessonTest{

		@BeforeClass
        public static void setUpBeforeClass()  {
			System.out.println("Testing methods in the Lesson Class: ");
		}

		@Test
		public void testValidPlay(){
			System.out.println("Test 1:isValid() method: should return false:");
			Lesson test = new Lesson();
			ArrayList<Person> players = test.getPlayers();
			test.setFirstCardPlayed("5D");
			assertEquals(false,test.isValid("5D",players.get(0)));
			
		    
		}

		@Test
		public void testGetLeadingSuit(){
			System.out.println("Test 2:getLeadingSuit method: returns suit:");
			Lesson test = new Lesson();
			test.setFirstCardPlayed("5D");
			assertEquals('D',test.getLeadingSuit());
			
			
		    
		}

		@Test
		public void testReadFile() throws IOException {
			System.out.println("Test 3:LoadInput method: should return file not found:");
			try{
				Lesson test = new Lesson("input/test.txt");
			}catch (IOException e) {
            		System.out.println("file test.txt not found");
			}
			
		    
		}

		@Test
		public void testPlayedPoints(){
			System.out.println("Test 4:getPlayPoints(String card) method: asserts to true:");
			Lesson test = new Lesson();
			test.setFirstCardPlayed("5D");
			assertEquals(10,test.getPlayPoints("TD"));
			
			
		    
		}
		
		@Test
		public void testSetBid(){
			System.out.println("Test 5:setBidding(String[] bidding) method: assert true if bid set:");
			Lesson test = new Lesson();
			String bid_str = "1S PA 2C PA 2H PA 2N PA 3H PA 4H PA PA PA";
			String[] Bidding = bid_str.split(" ");
			test.setBidding(Bidding);
			assertEquals('H',test.getBiddingSuit());	
		    
		}
		
		@Test
		public void GettingFirstCardPlayed(){
			System.out.println("Test 6:getFirstCardPlayed method: returns card:");
			Lesson test = new Lesson();
			test.setFirstCardPlayed("5D");
			assertEquals("5D",test.getFirstCardPlayed());
			
			
		    
		}



		




}
