import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
* Driver class used to run the game(Bridge game) . . .
* 
* @author Chris Cushway
* @author Gerald Ngumbulu
* @author Blessed Chitamba
*
*/

public class Game{
	public static void main(String args[]) throws IOException, InterruptedException{

		Scanner userinput = new Scanner(System.in);
		System.out.println("Enter level: ");
		String level = userinput.nextLine();
		String lesson_file = "input/input"+level+".txt";
		Lesson lesson = new Lesson(lesson_file);
		
		ArrayList<String> copyBestCase = lesson.getBestCase();
		ArrayList<String> cardsPlayed = new ArrayList<String>();
		
		System.out.println("------------------------------------------------------------------\n");
		
		int playerTurn = 0;

		//Setting up gui for game

		JFrame window = new JFrame("Bridge Tutor");
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);  
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/*A way to get the screens size will be used later
		/*For UI components
		*/
		Toolkit tk = Toolkit.getDefaultToolkit();  
		int xSize = ((int) tk.getScreenSize().getWidth());  
		int ySize = ((int) tk.getScreenSize().getHeight()); 

		//setting up panel - it is like the "glass" in the window

		JPanel contentPane = new JPanel();
		
		//Array of Jlabels to act as gui hand
		/*This is just code testing to
			see how guis work
			*/
		ArrayList<JLabel> cardLabels = new ArrayList<JLabel>();
		for(int i=0;i<13;i++){
			JLabel cardLabel = new JLabel(new ImageIcon(lesson.getPlayers().get(playerTurn).getCard(i).getCardImage()));
			cardLabel.setSize(120,80);
			cardLabels.add(cardLabel);
		}
		
		for(JLabel j:cardLabels){
    			contentPane.add(j);
			
		}
		
		
		window.getContentPane().add(BorderLayout.SOUTH,contentPane);
		window.getContentPane().setBackground(Color.green);

		window.setVisible(true);

		//-----FIRST PLAY OF THE GAME----------------------------------------------------------------------//
		System.out.println(lesson.getPlayers().get(playerTurn).getPlayerName() + " is now playing.");
		System.out.println(lesson.getPlayers().get(playerTurn).getPlayerName()+" played: "+lesson.getFirstCardPlayed());
		System.out.println(lesson.getPlayers().get(playerTurn).getPlayerName() + " completed their turn."+"\n");
		copyBestCase.remove(0);
		lesson.isValid(lesson.getFirstCardPlayed(),lesson.getPlayers().get(playerTurn));
		cardsPlayed.add(lesson.getFirstCardPlayed());
		playerTurn++;
		//-----FIRST PLAY OF THE GAME END----------------------------------------------------------------------//		
	

		//---------------------------------------------------------THE ACTUAL GAME-----------------------------------------------------///

		//Variables to be tracked
		int tricks = 0;			//tricks in the game (max is 13)	
		boolean trickWinner = false;
		boolean claim = false;		//If a person plays claim
		
		while(tricks < 13){ //max tricks is 13 unless claim played beforehand
				if(tricks==0){
					copyBestCase = lesson.getBestCase1(tricks);
					copyBestCase.remove(0);
				}
				else{
					copyBestCase = lesson.getBestCase1(tricks);
				}
				while(playerTurn<4){
					System.out.print("Cards played: ");
					for(String card:cardsPlayed){
						System.out.print(card+" ");				
					}
					System.out.print("\n\n");
					System.out.println(lesson.getPlayers().get(playerTurn).getPlayerName() + " is now playing.");
					System.out.println(lesson.getPlayers().get(playerTurn).getPlayerName() + " Please play a card by typing the number and then the suit e.g 6D \n");
					lesson.getPlayers().get(playerTurn).printNiceHand();
					System.out.println("\n");
					System.out.println("------------------------------------------------------------------");
					
					String card = userinput.nextLine();
					//If not the first turn
					if(playerTurn>0){

						while(!lesson.isValid(card,lesson.getPlayers().get(playerTurn))){
							System.out.println("That card is not a valid play please play another card");
							card = userinput.nextLine();
						}
					//else its the first turn we need to set the suit	
					}else{
						lesson.setFirstCardPlayed(card);
						while(!lesson.isValid(card,lesson.getPlayers().get(playerTurn))){

							System.out.println("That card is not a valid play please play another card");
							card = userinput.nextLine();
						}
				
						
					}
					lesson.getPlayers().get(playerTurn).removeCard(card);
					if(!card.equals(copyBestCase.get(0))){
						String bestPlay = lesson.getPlayers().get(playerTurn).bestCaseInHand(copyBestCase);
						System.out.println("The card that should have been played was: "+bestPlay);
						
					}
					//Add the points of the play to the player i.e playing an ACE adds 14 points to player
					lesson.getPlayers().get(playerTurn).addPoints(lesson.getPlayPoints(card));
					cardsPlayed.add(card);

					//Only let the person claim if it is in fact a stage in the game where claiming will actually win them the game
					if(card.equals("CLAIM") && copyBestCase.equals("CLAIM")){
						claim=true;
						break;
					}
					copyBestCase.remove(0);
					playerTurn++;
				}

			//If they claimed
			if(claim){
				lesson.getPlayers().get(playerTurn).setTrickWins(13 - tricks + lesson.getPlayers().get(playerTurn).getTrickWins());
			}
			lesson.decideWinner();
			lesson.reorderPlayers();
			cardsPlayed.clear();
			playerTurn=0;
			tricks++;
			
		}

		lesson.decideGameWinner();
						
	}
}
