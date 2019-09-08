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
		public void testResetPoints(){
			System.out.println("Test 1:resetPoints() method: should reset every player's points to zero(0).");
			Lesson test = new Lesson();
			test.resetPoints();
			ArrayList<Person> players = test.getPlayers();
			assertEquals(0,players.get(1).getPersonPoints());
			 
		}

		@Test
		public void testResetWinner(){
			System.out.println("Test 2:resetWinner() method: should be false since it's winner is been reset to false.");
			Lesson test = new Lesson();
			test.resetWinner();
			ArrayList<Person> players = test.getPlayers();
			assertEquals(false,players.get(0).getWinner());
			 
		}

		@Test
		public void testsetWinner(){
			System.out.println("Test 3:setWinner() method: should be true for the player that won the trick.");
			Lesson test = new Lesson();
			ArrayList<Person> players = test.getPlayers();
			test.setWinner("North");
			assertEquals(true,players.get(1).getWinner());
			 
		}

		@Test
		public void testValidPlay(){
			System.out.println("Test 4:isValid() method: should return false if the play is not valid:");
			Lesson test = new Lesson();
			ArrayList<Person> players = test.getPlayers();
			test.setFirstCardPlayed("5D");
			assertEquals(false,test.isValid("5D",players.get(0)));
			
		    
		}

		@Test
		public void testGetPlayers(){
			System.out.println("Test 5:getPlayers() method: returns 4 players present.");
			Lesson test = new Lesson();
			ArrayList<Person> players = test.getPlayers();
			assertEquals(4,players.size());
			
			
		    
		}

		@Test
		public void testGetLeadingSuit(){
			System.out.println("Test 6:getLeadingSuit method: returns leading suit.");
			Lesson test = new Lesson();
			test.setFirstCardPlayed("5D");
			assertEquals('D',test.getLeadingSuit());
			
			
		    
		}

		@Test
		public void testReadFile() throws IOException {
			System.out.println("Test 7:LoadInput method: should return file not found:");
			try{
				Lesson test = new Lesson("input/test.txt",1);
			}catch (IOException e) {
            		System.out.println("file test.txt not found");
			}
			
		    
		}

		@Test
		public void testGameWinner(){
			System.out.println("Test 8:decideGameWinner() method: should display north won the game.");
			Lesson test = new Lesson();
			ArrayList<Person> players = test.getPlayers();
			players.get(1).setTrickWins(2);
			test.decideGameWinner();
			assertEquals(2,players.get(1).getTrickWins());
			 
		}

		@Test
		public void testReorderPlayer(){
			System.out.println("Test 9:decideGameWinner() method:tests if north starts after winning a trick.");
			Lesson test = new Lesson();
			ArrayList<Person> players = test.getPlayers();
			players.get(1).setWinner();
			test.reorderPlayers();
			players = test.getPlayers();
			assertEquals("North",players.get(0).getPlayerName());
			 
		}

		@Test
		public void testPlayedPoints(){
			System.out.println("Test 10:getPlayPoints(String card) method, checks playedcards points: asserts to true.");
			Lesson test = new Lesson();
			test.setFirstCardPlayed("5D");
			assertEquals(10,test.getPlayPoints("TD"));
			
			
		    
		}

		@Test
		public void testdecideWinner(){
			System.out.println("Test 11:decideWinner() method: should be true for the player who wins the game.");
			Lesson test = new Lesson();
			ArrayList<Person> players = test.getPlayers();
			players.get(1).setPoints(4);
			assertEquals("North",test.decideWinner());
			 
		}
		
		@Test
		public void testSetBid(){
			System.out.println("Test 12:setBidding(String[] bidding) method: assert true if bid set");
			Lesson test = new Lesson();
			String bid_str = "1S PA 2C PA 2H PA 2N PA 3H PA 4H PA PA PA";
			String[] Bidding = bid_str.split(" ");
			test.setBidding(Bidding);
			assertEquals('H',test.getBiddingSuit());	
		    
		}
		
		@Test
		public void GettingFirstCardPlayed(){
			System.out.println("Test 13:getFirstCardPlayed method: returns first card played.");
			Lesson test = new Lesson();
			test.setFirstCardPlayed("5D");
			assertEquals("5D",test.getFirstCardPlayed());	
		    
		}


}
