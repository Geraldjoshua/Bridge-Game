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
	private ArrayList<JLabel> cardLabels = new ArrayList<JLabel>(),cards=new ArrayList<JLabel>();
	private JButton exitButton, flipCardsButton, getHintsButton, getTipsButton, claimButton;
	private JLabel score,playerTurn,bidding;
	
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
	
	private void initCenterPanel(){
		this.centerPanel = new JPanel(null);
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createLineBorder(new Color(226,172,44),5));
        centerPanel.setBounds((int)(xSize/4),(int)(ySize/4),(int)(xSize/2),(int)(ySize/2));
        components.add(centerPanel);
    }

	public void makeCards(){
		int x = testCardX(100,calcY(100));
		for(int i=0;i<panels.length;i++){
            for(int j=0;j<13;j++){

                if(!lesson.getPlayers().get(i).getPlayerName().toLowerCase().equals("south") && !lesson.getPlayers().get(i).getPlayerName().toLowerCase().equals("north")){
                    lesson.getPlayers().get(i).getCard(j).setFlipped(true);
                    lesson.getPlayers().get(i).getCard(j).resizeCard(x,(int)calcY(x),true);
                }else{
                    lesson.getPlayers().get(i).getCard(j).setFlipped(false);
                    lesson.getPlayers().get(i).getCard(j).resizeCard(x,(int)calcY(x),false);
                }

                cardLabels.add(lesson.getPlayers().get(i).getCard(j).getCardLabel());

                cards.add(lesson.getPlayers().get(i).getCard(j).getCardLabel());
            }
            int xCoord=framePadding;
            int yCoord=0;
            if(i%2 !=0) {
                xCoord = panels[i].getWidth() / 2 - ((13 / 2) * x)/2 - x/2;
                yCoord=framePadding;
            }
            for(JLabel j:cardLabels){
                panels[i].add(j);

                if(xCoord+j.getWidth()+framePadding>=panels[i].getWidth()){
                    xCoord = framePadding;
                    yCoord+=j.getHeight()+15;
                }
                j.setLocation(xCoord,yCoord);
                xCoord+=j.getWidth()/2;
            }
            cardLabels.clear();
        }

	}

	/**		
     * <p> handles the placement of the card on the panel</p>		
     * @param testX		
     * @param testY		
     * @return integer		
     */

    public int testCardX(double testX,double testY){

        int panelHeight = panels[0].getHeight()-2*ySpaceing;
        int panelWidth = panels[0].getWidth()-2*framePadding;
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
            System.out.println("area "+area+"new x dim is: " + (int)testX);
            return (int)testX*2;
        }
    }

    /**		
     * <p> used to resize cards to fit on the screen</p>		
     * @param x		
     * @return double		
     */
    public double calcY(double x){
        x=Math.floor(((x/363)*543));
        return x;
    }

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

	public JButton initButton(String text, Color c1, Color c2, Point p){
		JButton b1 = new JButton(text);
		b1.setSize(b1.getPreferredSize());
        b1.setBackground(c1);
        b1.setForeground(c2);
        b1.setLocation(p);
		b1.setFocusPainted(false);
        return b1;
	} 
	
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

    public void addCardListener(MouseListener ml, int index){
        cards.get(index).addMouseListener(ml);
    }

    public void addExitListener(MouseListener ml){
        exitButton.addMouseListener(ml);
    }

    public void addGetHintsListener(MouseListener ml){
        getHintsButton.addMouseListener(ml);
    }

    public void addGetTipsListener(MouseListener ml){
        getTipsButton.addMouseListener(ml);
    }

    public void addClaimListener(MouseListener ml){
        claimButton.addMouseListener(ml);
    }
    public void addFlipCardsListener(MouseListener ml){
        flipCardsButton.addMouseListener(ml);
    }

    public ArrayList<JLabel> getCards(){
        return cards;
    }

    public JPanel[] getPanels(){return panels;}

    public JLabel getPlayerTurnJLabel(){
        return playerTurn;
    }

    public void removeCardListener(MouseListener ml,int index){
        cards.get(index).removeMouseListener(ml);
    }

    public JPanel getCenterPanel(){
        return centerPanel;
    }
    public JLabel getScoreJLabel(){
        return score;
    }
}
