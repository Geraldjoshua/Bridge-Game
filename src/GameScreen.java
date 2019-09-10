import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameScreen extends JFrame{

	private Lesson lesson;
	private BackgroundPanel bgPanel;
	private final int xSize,ySize,framePadding = 40,ySpaceing = 15;
	private JPanel[] panels = new JPanel[4];
	private ArrayList<Component> components = new ArrayList<Component>();
	private JPanel centerPanel;
	private ArrayList<JLabel> cardLabels = new ArrayList<>(),cards=new ArrayList<JLabel>();;
	private JButton exitButton, flipCardsButton, getHintsButton, getTipsButton, claimButton;
	private JLabel score,playerTurn,bidding;

    /**
     * <p>Constructor that initialises the GameScreen to be the size of the screen</p>
     * @param xSize int
     * @param ySize int
     * @param lesson Lesson
     * @throws IOException
     */
	GameScreen(int xSize,int ySize,Lesson lesson)throws IOException{
		this.xSize = xSize;
		this.ySize = ySize;
		this.lesson = lesson;
		this.bgPanel = new BackgroundPanel(new File("images/background3.jpg"));
		bgPanel.addBackGround();
		this.setUndecorated(true);
		this.setSize(xSize,ySize);
		makeAndAddComponents();
		this.add(bgPanel);
		
	}

    /**
     * <p>Method that initialises components in the GameScreen and adds them to the screen</p>
     */
	public void makeAndAddComponents(){
		initCenterPanel();		
		makePanels();
		makeCards();
		initUserButtons();
		initLabels();
		for(Component c:components){
			this.add(c);
		}

	}

    /**
     * <p>Method that initialises the Panels that will represent each player and positions them</p>
     */
	public void makePanels(){
		
		for(int i=0;i<panels.length;i++){
            panels[i] = new JPanel(null);
            panels[i].setOpaque(false);
            components.add(panels[i]);
        }

        panels[0].setBounds(0,(int)(ySize/4),(int)(xSize/4),(int)(ySize/2));
        panels[1].setBounds(0,0,xSize,(int)(ySize/4));
        panels[2].setBounds((int)((3*xSize)/4),(int)(ySize/4),(int)(xSize/4),(int)(ySize/2));
        panels[3].setBounds(0,(int)((3*ySize)/4),xSize,(int)(ySize/4));
	}

    /**
     * <p>Method that initialises the centerpanel of the game screen</p>
     */
	private void initCenterPanel(){
		this.centerPanel = new JPanel(null);
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createLineBorder(new Color(226,172,44),5));
        centerPanel.setBounds((int)(xSize/4),(int)(ySize/4),(int)(xSize/2),(int)(ySize/2));
        components.add(centerPanel);
    }

    /**
     * <p>Method that creates card JLabels for each card in each players hand. The cards are placed on the player panel at a position depending on their index
     * in the players hand</p>
     */
	public void makeCards(){
		int x = testCardX(100,calcY(100));
		for(int i=0;i<panels.length;i++){
            for(int j=0;j<13;j++){
                //if it is not south or west
                if(!lesson.getPlayers().get(i).getPlayerName().toLowerCase().equals("south") && !lesson.getPlayers().get(i).getPlayerName().toLowerCase().equals("north")){
                    lesson.getPlayers().get(i).getCard(j).setFlipped(true);
                    lesson.getPlayers().get(i).getCard(j).resizeCard(x,(int)calcY(x),true);//show cards
                }else{
                    lesson.getPlayers().get(i).getCard(j).setFlipped(false);
                    lesson.getPlayers().get(i).getCard(j).resizeCard(x,(int)calcY(x),false);//hide cards
                }
                cards.add(lesson.getPlayers().get(i).getCard(j).getCardLabel());
                cardLabels.add(lesson.getPlayers().get(i).getCard(j).getCardLabel());
            }
            //Loop to determine where card will be placed in the panel
            int xCoord=framePadding;
            int yCoord=0;
            //if player is south or north (south maps to index 1 and north to 3
            if(i%2 !=0) {
                xCoord = panels[i].getWidth() / 2 - ((13 / 2) * x)/2 - x/2; // Set start xCoord
                yCoord=framePadding;
            }
            for(JLabel j:cardLabels){
                panels[i].add(j);
                //if the placing the card would make the card go over the boundary of the panel in the
                // x direction
                if(xCoord+j.getWidth()+framePadding>=panels[i].getWidth()){
                    xCoord = framePadding; //reset xCoord
                    yCoord+=j.getHeight()+15;   //increment y coord
                }
                j.setLocation(xCoord,yCoord);
                xCoord+=j.getWidth()/2;
            }
            cardLabels.clear(); //Clear so we can get accurate index calculation in loop

        }

	}

	/**		
     * <p> Method to determine what width a card image needs to be so that all the players 13 cards can fit on the JPanel that maps to them</p>
     * @param testX	double
     * @param testY	double
     * @return integer		
     */

	/*This method takes in the width of the card image and recursively tests to see if the card is too large to fit with all the other
	cards on the player panel given the users screen size. The method checks if the dimension
    (testX x testY) will allow the cards of the player to fit on their panel. it does this by checking the area of available space
    in the players panel in the unit of cards squared. if the area is less than 13 (the highest possible cards displayed on a
    player panel) the cards need to reduce in size else the function returns the width of the card image that will allow all
    the cards to fit on the players panel
	 */

    private int testCardX(double testX,double testY){
        int panelHeight = panels[0].getHeight()-2*ySpaceing;
        int panelWidth = panels[0].getWidth()-2*framePadding;

        /*testX is divided by 2 as only half the card will be displayed when it is placed on the screen
        as cards overlap each other on the screen
         */
        testX=Math.ceil(testX/2);
        int cardsX = (int)(Math.floor(panelWidth/testX));
        int cardsY = (int)(Math.floor((panelHeight/testY)));
        cardsY = (int)(Math.floor(((panelHeight - ySpaceing*cardsY)/testY)));
        int area = cardsX*cardsY;
        if(area<13){
            testX*=2;
            testX-=10;
            return testCardX(testX,calcY(testX));
        
        }else{
            //Need to return testX x 2 as it was divided by 2 earlier
            return (int)testX*2;
        }
    }

    /**		
     * <p> this method calculates the height the card image needs to be given a specific
     *     width, whilst maintaining its original pixel resolution</p>
     * @param x		
     * @return double		
     */
    public double calcY(double x){
        //resolution of original card image is 363 x 543
        x=Math.floor(((x/363)*543));
        return x;
    }

    /**
     * <p>Method that initialises the button components on the game screen</p>
     */
	public void initUserButtons(){
		//exitButton, flipCardsButton, getHintsButton, getTipsButton, claimButton;
		exitButton = initButton("<html><h3>EXIT</h3></html>",new Color(226,172,44),new Color(0, 134, 64),new Point(framePadding,framePadding));
		flipCardsButton = initButton("<html><h3>FLIP CARDS</h3></html>",new Color(226,172,44),new Color(0, 134, 64),new Point(framePadding,ySize*7/8));
        getHintsButton = initButton("<html><h3>GET HINTS</h3></html>",new Color(226,172,44),new Color(0, 134, 64),new Point(xSize*6/8 + 20,ySize*7/8));
		getTipsButton = initButton("<html><h3>GET TIPS</h3></html>",new Color(226,172,44),new Color(0, 134, 64),new Point(getHintsButton.getLocation().x+getHintsButton.getWidth()+ 20,ySize*7/8));
        claimButton = initButton("<html><h3>CLAIM</h3></html>",new Color(226,172,44),new Color(0, 134, 64),new Point(getHintsButton.getLocation().x ,getHintsButton.getLocation().y - exitButton.getHeight() - 20));
        claimButton.setSize(getHintsButton.getWidth()+getTipsButton.getWidth()+20,claimButton.getHeight());
		components.add(exitButton);
        components.add(flipCardsButton);
	    components.add(getHintsButton);
	    components.add(getTipsButton);
	    components.add(claimButton);
	}

    /**
     * <p>Method to initialise the button to the parameter values</p>
     * @param text String
     * @param c1 Color
     * @param c2 Color
     * @param p Point
     * @return JButton
     */
	public JButton initButton(String text, Color c1, Color c2, Point p){
		JButton b1 = new JButton(text);
		b1.setSize(b1.getPreferredSize()); //Set size to fit text
        b1.setBackground(c1);
        b1.setForeground(c2);
        b1.setLocation(p);
		b1.setFocusPainted(false);
        return b1;
	}

    /**
     * <p>Method to initialise all JLabels on the GameScreen</p>
     */
	public void initLabels(){
        score = new JLabel("<html><head><style>body{color:white;}</style></head><body><h1>N + S Score: "+(lesson.getPlayers().get(1).getTrickWins()+lesson.getPlayers().get(3).getTrickWins())+"</h1><h2>W + E Score: "+(lesson.getPlayers().get(0).getTrickWins()+lesson.getPlayers().get(2).getTrickWins())+"</h2><body></html>");
        score.setSize(score.getPreferredSize());
        score.setLocation(xSize*6/8 + 20,framePadding);
        bidding = new JLabel("<html><h3 style='color:white;'>The Bidding is: "+lesson.getBiddingString()+"</h3> <h3 style='color:white;'> The Bidding Suit is: "+ lesson.getBiddingSuit()+"</h3></html>");
        bidding.setSize(bidding.getPreferredSize());
        bidding.setLocation(centerPanel.getLocation().x+framePadding,centerPanel.getLocation().y+framePadding);
        playerTurn = new JLabel();
        playerTurn.setSize(300,20);
        playerTurn.setLocation(centerPanel.getLocation().x+framePadding,centerPanel.getLocation().y + centerPanel.getHeight() - playerTurn.getHeight() - framePadding);
        components.add(score);
        components.add(bidding);
        components.add(playerTurn);
    }

    /**
     * <p>Method to add MouseListener to card at an index</p>
     * @param ml MouseListener
     * @param index int
     */
    public void addCardListener(MouseListener ml, int index){
        cards.get(index).addMouseListener(ml);
    }

    /**
     * <p>Method to add MouseListener to the exitButton</p>
     * @param ml MouseListener
     */
    public void addExitListener(MouseListener ml){
        exitButton.addMouseListener(ml);
    }
    /**
     * <p>Method to add MouseListener to the getHintsButton</p>
     * @param ml MouseListener
     */
    public void addGetHintsListener(MouseListener ml){
        getHintsButton.addMouseListener(ml);
    }
    /**
     * <p>Method to add MouseListener to the getTipsButton</p>
     * @param ml MouseListener
     */
    public void addGetTipsListener(MouseListener ml){
        getTipsButton.addMouseListener(ml);
    }
    /**
     * <p>Method to add MouseListener to the claimButton</p>
     * @param ml MouseListener
     */
    public void addClaimListener(MouseListener ml){
        claimButton.addMouseListener(ml);
    }
    /**
     * <p>Method to add MouseListener to the flipCardsButton</p>
     * @param ml MouseListener
     */
    public void addFlipCardsListener(MouseListener ml){
        flipCardsButton.addMouseListener(ml);
    }

    /**
     * <p>Method to return the JPanel array that holds all the JPanels representing players</p>
     * @return JPanel[]
     */
    public JPanel[] getPanels(){return panels;}

    /**
     * <p>Method that returns the JLabel that shows the player whose turn it is</p>
     * @return JLabel
     */
    public JLabel getPlayerTurnJLabel(){
        return playerTurn;
    }

    /**
     * <p>Method that returns the center panel of the GameScreen</p>
     * @return JPanel
     */
    public JPanel getCenterPanel(){
        return centerPanel;
    }
    /**
     * <p>Method that returns the JLabel that shows the score of the teams</p>
     * @return JLabel
     */
    public JLabel getScoreJLabel(){
        return score;
    }
}
