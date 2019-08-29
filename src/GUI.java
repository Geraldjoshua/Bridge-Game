import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class GUI{
	
	private Lesson lesson;
	private final int framePadding = 40;
	private JFrame window;
	private ArrayList<JLabel> cardLabels = new ArrayList<JLabel>();
	private JPanel[] panels = new JPanel[4];
	private final int xSize;
	private final int ySize;
	private JPanel centerPanel;
	private int currentPlayer;
	private JPanel playLog;
	private JLabel playerTurn;
	private JLabel score;
	private JButton getHints;
	private JButton getTips;
	private JLabel playLogHeader;
	private JButton flipCards;
	private ArrayList<JLabel> cards = new ArrayList<JLabel>();
	private int timesClicked = 0;
	private int count=0;

	GUI(Lesson lesson) throws IOException{
		
		this.lesson = lesson;
		this.window = new JFrame("Bridge Tutor");
		Toolkit tk = Toolkit.getDefaultToolkit();
		this.xSize = ((int) tk.getScreenSize().getWidth());
		this.ySize = ((int) tk.getScreenSize().getHeight());
		this.centerPanel = new JPanel();
		this.playLog = new JPanel();
		this.currentPlayer = 0;
		centerPanel.setLayout(null);
		centerPanel.setPreferredSize(new Dimension(600,600));
		centerPanel.setMinimumSize(new Dimension(600, 600));
		centerPanel.setOpaque(false);
		centerPanel.setBorder(BorderFactory.createLineBorder(new Color(226,172,44),2));
	}

	public void makeLessonScreen() throws IOException{
	
		
		Image img = ImageIO.read(new File("icons/exitIcon.png"));
    		Image newimg = img.getScaledInstance( 100, 100,  Image.SCALE_SMOOTH ) ; 
		

		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.getContentPane().setSize(xSize,ySize);
		window.setLocationRelativeTo(null);
		System.out.println("wind size" + window.getContentPane().getWidth());
		window.setUndecorated(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		for(int i=0;i<panels.length;i++){
		    panels[i] = new JPanel();
		    panels[i].setOpaque(false);
		    panels[i].setLayout(null);
		    if(i%2 != 0) {
		        panels[i].setPreferredSize(new Dimension(xSize,200));
		    } else{
		        panels[i].setPreferredSize(new Dimension(400, ySize));
		    }

		}

		for(int i=0;i<panels.length;i++){
		    for(int j=0;j<13;j++){
			JLabel cardLabel;
			if(i<3){
				lesson.getPlayers().get(i).getCard(j).setFlipped(true);
				cardLabel = lesson.getPlayers().get(i).getCard(j).getCardLabel();
			}else{
				cardLabel = lesson.getPlayers().get(i).getCard(j).getCardLabel();
			}
		        
		        cardLabel.setSize(72,96);
		        cardLabel.addMouseListener(new MouseAdapter() {
				
				
		            	@Override
		            	public void mouseEntered(MouseEvent evt) {
		                	Point pt = cardLabel.getLocation();
		                	int x = pt.x;
		                	int y = pt.y;
		                	cardLabel.setLocation(x,y-20);
					cardLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		            	}

		            	@Override
		            	public void mouseExited(MouseEvent evt) {
		                	Point pt = cardLabel.getLocation();
		                	int x = pt.x;
		                	int y = pt.y;
		                	cardLabel.setLocation(x,y+20);
		            	}
				
				@Override
				public void mouseClicked(MouseEvent evt){
					

					findPlayer((JLabel)evt.getSource());					
					
				}

		        });
			
			if(lesson.getPlayers().get(i).getCard(j).toString().equals(lesson.getFirstCardPlayed())){
				lesson.getPlayers().get(i).getCard(j).setFlipped(false);
				cardLabel.setIcon(lesson.getPlayers().get(i).getCard(j).getImageIcon());
				centerPanel.add(cardLabel);
				
				//cardLabel.setLocation((int)(centerPanel.getPreferredSize().getWidth()/2),(int)(centerPanel.getPreferredSize().getHeight()/2));
				cardLabel.setLocation(400,250);
				System.out.print("dimensions of screen: ("+xSize+","+ ySize+")");
				System.out.println("panel width in loop:" + centerPanel.getWidth() + " panel height in loop: " +centerPanel.getHeight());
			}else{
				cardLabels.add(cardLabel);
			}
		        cards.add(cardLabel);
		    }
		    double panelWidth = panels[i].getPreferredSize().getWidth();
		    
		    int xDim;
		    int startMargin=60;
		    int marginX;
		    if(i%2 != 0){
		        startMargin=(int)panelWidth/2 - (50*13)/2 - 50;
		        xDim = 50;
		    }else{
		        xDim=40;
		    }
		    marginX=startMargin;
		    System.out.println(marginX);
		    int marginY = 30;
		    for(JLabel j:cardLabels){
		        panels[i].add(j);
		        j.setLocation(marginX,marginY);
		        marginX+=xDim;
		        if(marginX+120>=panelWidth){

		            marginX=startMargin;
		            marginY+=130;

		        }
		    }
		    cardLabels.clear();
		
		}
		System.out.println("size" + cards.size());
				
		

		score = makeScore();
		window.add(score);
		score.setLocation(xSize - 200 - (int)score.getPreferredSize().getWidth(),framePadding);		

		JButton exit = makeExitButton();
		window.add(exit);
		exit.setLocation(40,40);

		getHints = makeGetHintsButton();
		window.add(getHints);
		getHints.setLocation(xSize-(int)getHints.getPreferredSize().getWidth()-120,770 - getHints.getHeight());
		
		flipCards = makeFlipCards();
		window.add(flipCards);
		flipCards.setLocation(100,50);

		getTips = makeGetTipsButton();
		window.add(getTips);
		getTips.setLocation(xSize - (int)getHints.getPreferredSize().getWidth()-120 - 15- (int)getTips.getPreferredSize().getWidth(),770 - getTips.getHeight());
	
		playLog = makePlayLog();
		window.add(playLog);
		playLog.setLocation(framePadding,ySize - 200 - framePadding);

		playLogHeader = new JLabel("<html><h1 style='color:white;text-decoration:underline;' >Play Log</h1></html>");
		playLogHeader.setLayout(null);
		playLogHeader.setSize(playLogHeader.getPreferredSize());
		window.add(playLogHeader);
		playLogHeader.setLocation(playLog.getX(),playLog.getY() - (int)playLogHeader.getPreferredSize().getHeight());
		
		window.add(panels[1], BorderLayout.NORTH);
		window.add(panels[0], BorderLayout.WEST);
		window.add(centerPanel, BorderLayout.CENTER);
		window.add(panels[2], BorderLayout.EAST);
		window.add(panels[3], BorderLayout.SOUTH);
		System.out.println("panel width after add:" + centerPanel.getWidth() + " panel height after add: " +centerPanel.getHeight());
		
		window.pack();
		System.out.println("panel width after pack:" + centerPanel.getWidth() + " panel height after pack: " +centerPanel.getHeight());
		
		window.getContentPane().setBackground(new Color(0, 134, 64));
		System.out.println("wind size h " + window.getContentPane().getHeight());
		//window.repaint();
		window.setVisible(true);		

	}

	private JButton makeFlipCards(){
		JButton flip = new JButton( new AbstractAction("EXIT") {
			
			@Override
			public void actionPerformed( ActionEvent e ) {
					count=0;
					int j=0;
					timesClicked++;
					System.out.println("times: " +timesClicked);
					for(int i=0;i<3;i++){
						
						for(Component card:panels[i].getComponents()){
						
							if(timesClicked%2==0){
								
								lesson.getPlayers().get(i).getCard(j).setFlipped(true);
								//cards.get(count).setIcon(lesson.getPlayers().get(i).getCard(j).getImageIcon());
								if(card instanceof JLabel){
									((JLabel)card).setIcon(lesson.getPlayers().get(i).getCard(j).getImageIcon());
								}
								
								 
							}else{
								
								lesson.getPlayers().get(i).getCard(j).setFlipped(false);
								//cards.get(count).setIcon(lesson.getPlayers().get(i).getCard(j).getImageIcon());
								if(card instanceof JLabel){
									((JLabel)card).setIcon(lesson.getPlayers().get(i).getCard(j).getImageIcon());
								}
								
							}
							j++;
							count++;							
						}
						j=0;
					}
						
			}
		});
		flip.setText("<html><h3>Flip Cards</h3></html>");
		flip.setLayout(null);
		flip.setSize(flip.getPreferredSize());
		flip.setBackground(new Color(226,172,44));
		flip.setForeground(new Color(0, 134, 64));
		return flip;

	}

	private JButton	makeGetHintsButton(){
		JButton hints = new JButton( new AbstractAction("EXIT") {
			@Override
			public void actionPerformed( ActionEvent e ) {
					
			}
		});
		hints.setText("<html><h3>GETS HINTS</h3></html>");
		hints.setLayout(null);
		hints.setSize(hints.getPreferredSize());
		hints.setBackground(new Color(226,172,44));
		hints.setForeground(new Color(0, 134, 64));
		return hints;
	}

	private JButton	makeGetTipsButton(){
		JButton tips = new JButton( new AbstractAction("EXIT") {
			@Override
			public void actionPerformed( ActionEvent e ) {
					
			}
		});
		tips.setText("<html><h3>GETS TIPS</h3></html>");
		tips.setLayout(null);
		tips.setSize(tips.getPreferredSize());
		tips.setBackground(new Color(226,172,44));
		tips.setForeground(new Color(0, 134, 64));
		return tips;
	}

	private JLabel makeScore(){
		JLabel scr = new JLabel("<html><h1>N + S Score: "+lesson.getPlayers().get(currentPlayer).getTrickWins()+"</h1><h2>W + E Score: "+lesson.getPlayers().get(currentPlayer).getTrickWins()+"</h2></html>");
		scr.setLayout(null);
		scr.setSize(scr.getPreferredSize());
		scr.setForeground(Color.white);
		return scr;
	}

	private JButton makeExitButton(){
		
		JButton exit = new JButton( new AbstractAction("EXIT") {
			@Override
			public void actionPerformed( ActionEvent e ) {
					
			}
		});
		exit.setText("<html><h2>EXIT</h2></html>");
		exit.setOpaque(false);
		exit.setContentAreaFilled(false);
		exit.setLayout(null);
		exit.setSize(exit.getPreferredSize());
		exit.setBorder(BorderFactory.createLineBorder(new Color(226,172,44),2));
		exit.setForeground(new Color(226,172,44));	
		exit.setFocusPainted(false);
			
		return exit;
	}

	public void findPlayer(JLabel source){

		int index=0;
		Container parent = source.getParent();
		for(int i=0;i<panels.length;i++){
			if(panels[i] == parent){
				index=i;
				
				break;
			}
		}

		
		if(lesson.getPlayers().get(index).getCanPlay()){
			
			parent.remove(source);
			parent.validate();
			parent.repaint();
			centerPanel.add(source);
			source.setLocation(0,0);
			lesson.getPlayers().get(index).setCanPlay(false);
			
			nextPlayerTurn();		
		}

						
	}

	public void nextPlayerTurn(){

		if(currentPlayer<4){
			currentPlayer++;
			playerTurn.setText("<html><p> It is "+lesson.getPlayers().get(currentPlayer).getPlayerName()+"'s turn </p></html>");
		}else{
			currentPlayer = 0;
			playerTurn.setText("<html><p> The trick is over </p></html>");		
		}
		
		

	}

	

	public JPanel makePlayLog() throws IOException{
		playLog.setOpaque(false);
		playLog.setLayout(null);
		playLog.setBorder(BorderFactory.createLineBorder(new Color(226,172,44),2));
		playerTurn = new JLabel("<html><p>"+lesson.getPlayers().get(currentPlayer).getPlayerName()+" Played a " + lesson.getFirstCardPlayed()+"</p></html>");
		playerTurn.setSize(300,50);
		playerTurn.setForeground(Color.white);
		playerTurn.setLocation(20,20);
		playLog.setSize(300,150);
		playLog.add(playerTurn);
		
		return playLog;
	}


}
