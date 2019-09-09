
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameController implements Runnable{
	
	private LoadScreen ls;
	private MenuScreen ms;
	private GameScreen gs;
	private Lesson lesson;
	private MouseListener selectButtonListener,lessonButtonListener,toggleButtonListener,backButtonListener,cardListener;
	private MouseListener exitGameListener,getHintsListener,getTipsListener,claimListener,flipCardsListener;
	private int clicks = 0,play=0,timesTipClicked = 0,currentPlayer,hintQuestionNumber = 0,tricks=0,timesClicked = 0;
	private ArrayList<String> copyBestCase,hintQuestions;
	private final int xSize,ySize;
	private boolean played;
	private Thread t;
	GameController(LoadScreen ls,MenuScreen ms,int xSize,int ySize)throws InterruptedException{
		this.ls = ls;
		this.ms = ms;
		this.xSize = xSize;
		this.ySize = ySize;
		this.currentPlayer = 0;
		this.played = false;
		loadProgram();
		initListeners();
		addListeners();

	}


	public void initListeners(){
		this.selectButtonListener = new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
				showButtons(false,ms.getMenuButtons());
				showButtons(true,ms.getLessonButtons());
				showBackButton(true,ms.getBackButton());
				ms.getSelectLessonHeader().setText("<html><h1 style='color:white;font-weight:bold;'>SELECT LESSON</h1></html>");
				ms.getSelectLessonHeader().setSize(ms.getSelectLessonHeader().getPreferredSize());
		    }
            
    	};
		this.backButtonListener = new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
				showButtons(true,ms.getMenuButtons());
				showButtons(false,ms.getLessonButtons());
				showBackButton(false,(JButton)e.getSource());
				ms.getSelectLessonHeader().setText("<html><h1 style='color:white;font-weight:bold;'>MAIN MENU</h1></html>");
				ms.getSelectLessonHeader().setSize(ms.getSelectLessonHeader().getPreferredSize());
		    }
            
    	};

		this.lessonButtonListener = new MouseAdapter(){
		    @Override
		    public void mouseClicked(MouseEvent e){
				try{
					findLesson(e,ms.getLessonButtons());

				}catch(InterruptedException e1){
					e1.printStackTrace();
				}catch(IOException e2){
					e2.printStackTrace();
				}
				
		    }
			@Override
			public void mouseEntered(MouseEvent e){
				findButton(e,ms.getLessonButtons());
			}

			@Override
			public void mouseExited(MouseEvent e) {
				resetColour((JButton)e.getSource());
			}
		};
		this.toggleButtonListener = new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
				toggleButton(e,ms.getMenuButtons());
		    }
            
    	};
		this.cardListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					makePlay((JLabel)e.getSource(),findPlayer((JLabel)e.getSource(),gs.getPanels()),play,gs.getPanels());
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				offSet((JLabel)e.getSource(),-20);
			}
			@Override
			public void mouseExited(MouseEvent e){
				offSet((JLabel)e.getSource(),20);
			}
		};
		this.exitGameListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				gs.dispose();
				t.interrupt();
				currentPlayer = 0;

			}

		};
		this.getTipsListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(timesTipClicked<lesson.getTips().size()){
					JOptionPane.showMessageDialog(gs,
							"Tip: " + lesson.getTipsasked(timesTipClicked) + " (click Tips for more)");
					timesTipClicked++;

				}
				else{
					JOptionPane.showMessageDialog(gs,
							"No more Tips!"+lesson.getPlayers().get(currentPlayer).getPlayerName()+" is playing...");
					((JButton)e.getSource()).setEnabled(false);
					((JButton)e.getSource()).setBackground(Color.GRAY);


				}
			}

		};
		this.getHintsListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTextPane jtp = new JTextPane();
				jtp.setEditable(false);
				jtp.setSize(new Dimension(300, 100));
				jtp.setPreferredSize(new Dimension(300,100));
				if(hintQuestionNumber<hintQuestions.size()){
					if (hintQuestionNumber==6){
						jtp.setSize(new Dimension(300, 300));
						jtp.setPreferredSize(new Dimension(300,300));
						jtp.setText(lesson.getHintasked(hintQuestions.get(hintQuestionNumber)));
						JOptionPane.showMessageDialog(gs,jtp, hintQuestions.get(hintQuestionNumber), 1);


					}
					else{
						jtp.setText(lesson.getHintasked(hintQuestions.get(hintQuestionNumber)));
						JOptionPane.showMessageDialog(gs,jtp, hintQuestions.get(hintQuestionNumber), 1);
						hintQuestionNumber++;
					}
				}

			}

		};
		this.claimListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				handleClaim(gs.getPanels());
			}

		};
		this.flipCardsListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				flipCards(gs.getPanels());
			}

		};

	}

	public void addListeners(){
		ms.addSelectLessonListener(selectButtonListener);
		addLessonListeners(ms.getLessonButtons());
		ms.addToggleHelpLevel(toggleButtonListener);
		ms.addBackButtonListener(backButtonListener);

	}

	public void loadProgram()throws InterruptedException{
		
		ls.setVisible(true);
		load(ls.getLoadBar(),ls.getLoadBar().getWidth());
		ls.dispose();		
		ms.setVisible(true);
		Thread.sleep(500);
		JOptionPane.showMessageDialog(ms,
                                    "<html><h3>Select the toggle help button to toggle the <br>help level for the lesson from No help - help level 5</h3></html>");
	}

	public void load(JProgressBar loadBar,int width)throws InterruptedException{
		
		Thread.sleep(1200);
        for(int i=0;i<width;i++){
			Thread.sleep(10);
			if(i>width/2){
				i++;
		    }else if(i>width*3/4){
				i+=2;
		    }
			    loadBar.setValue(i);
        }		

	}

	public void findLesson(MouseEvent e,ArrayList<JButton> buttons)throws IOException, InterruptedException{
		
		for(int i=0;i<buttons.size();i++){
			
			if(e.getSource() instanceof JButton && e.getSource() == buttons.get(i)){
				lesson = new Lesson("input/input"+(i+1)+".txt",clicks);
				gs = new GameScreen(xSize,ySize,lesson);
				hintQuestions = new ArrayList<>(lesson.getHints().keySet());
				copyBestCase = lesson.getBestCase();
				gs.setVisible(true);
				gs.addExitListener(exitGameListener);
				gs.addGetTipsListener(getTipsListener);
				gs.addGetHintsListener(getHintsListener);
				gs.addClaimListener(claimListener);
				gs.addFlipCardsListener(flipCardsListener);
				t = new Thread(this);
				t.start();
			}
		}

	}

	public void toggleButton(MouseEvent e,ArrayList<JButton> buttons){
		clicks++;		
		for(int i=0;i<buttons.size();i++){
			
			if(e.getSource() instanceof JButton && e.getSource() == buttons.get(i)){
				if(clicks>5){
						clicks=0;
						buttons.get(i).setText("<html><h3 style='color:red;'>NO HELP</h3></html>");
					}else if(clicks>0){
						buttons.get(i).setText("<html><h3 style='color:red;'>HELP LEVEL: "+clicks+"</h3></html>");					
					}
			}
		}
	}

	public void findButton(MouseEvent e,ArrayList<JButton> buttons){
		
		for(int i=0;i<buttons.size();i++){
			
			if(e.getSource() instanceof JButton && e.getSource() == buttons.get(i)){
				 handleToolTip(buttons.get(i),i);
			}
		}
		((JButton)e.getSource()).setForeground(Color.white);
		((JButton)e.getSource()).setBackground(Color.red);
		((JButton)e.getSource()).setCursor(new Cursor(Cursor.HAND_CURSOR));

	}

	public void resetColour(JButton b){
		b.setForeground(Color.red);
		b.setBackground(Color.white);
	}

	public void handleToolTip(JButton b,int index){
		b.setToolTipText("<html><h4>This is a lesson for scenario "+(index+1)+"</h4></html>");
	}
	public void addLessonListeners(ArrayList<JButton> buttons){
		for(int i=0;i<buttons.size();i++){
			ms.addLessonListener(lessonButtonListener,i);
		}
	}

	public void showButtons(boolean visible,ArrayList<JButton> buttons){
		for(int i=0;i<buttons.size();i++){
			buttons.get(i).setVisible(visible);
		}
	}
	
	public void showBackButton(boolean visible,JButton b){
		b.setVisible(visible);
	}

	public void offSet(JLabel source,int offset){
		Point pt = source.getLocation();
		int x = pt.x;
		int y = pt.y;
		source.setLocation(x,y+offset);
		if(offset>0){
			source.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
	}

	public void addCardListeners(ArrayList<JLabel> cards){
		System.out.println(cards.size());
		for(int i=0;i<cards.size();i++){
			gs.addCardListener(cardListener,i);
		}

	}

	public void handleClaim(JPanel[] panels){
		if(copyBestCase.get(0).equals("CLAIM")){
			JOptionPane.showMessageDialog(gs,
					"Congratulations you win the remaining "+(13-tricks)+" tricks");
			lesson.getPlayers().get(currentPlayer).addTrickWins((13-tricks));
			for(int i=0;i<panels.length;i++){
				for(Component c:panels[i].getComponents()){
					if(c instanceof JLabel){
						panels[i].remove(c);
					}
				}
			}
		}else{
			String s = (String)JOptionPane.showInputDialog(
					gs,
					"Please type the order of cards played for the remaining "+ (13-tricks)+" tricks as a coma separated list","",JOptionPane.PLAIN_MESSAGE);
			if(s!=null){
				JOptionPane.showMessageDialog(gs,
						"You won't win with that order");
			}

		}
	}

	public void flipCards(JPanel[] panels){
		int count=0;
		int j=0;
		timesClicked++;
		for(int i=0;i<3;i+=2){
			for(Component card:panels[i].getComponents()){
				if(card instanceof JLabel){
					if(timesClicked%2==0){
						lesson.getPlayers().get(i).getCard(j).setFlipped(true);

					}else{
						lesson.getPlayers().get(i).getCard(j).setFlipped(false);

					}
				}
				j++;
				count++;
			}
			j=0;
		}
	}

	public void run(){

		try {
			startGame();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void startGame() throws InterruptedException{
		for(;tricks<13;tricks++) {
			for (play = 0; play < 4; play++) {
				played = false;

				lesson.getPlayers().get(currentPlayer).setCanPlay(true);
				gs.getPlayerTurnJLabel().setText("<html><h3 style='color:white;'>"+lesson.getPlayers().get(currentPlayer).getPlayerName()+" is playing...</h1></html>");

				if (!lesson.getPlayers().get(currentPlayer).getPlayerName().toLowerCase().equals("south") && !lesson.getPlayers().get(currentPlayer).getPlayerName().toLowerCase().equals("north")) {
					//Thread.sleep(2000);
					autoPlay(gs.getPanels());
				} else {
					addMouseListeners(gs.getPanels());
					while (!played) {
						t.sleep(1);
					}
					removeMouseListeners(gs.getPanels());
				}
				if(!copyBestCase.isEmpty() && !copyBestCase.get(0).equals("CLAIM")){
					copyBestCase.remove(0);
				}
				lesson.getPlayers().get(currentPlayer).setCanPlay(false);
				currentPlayer++;
				if (currentPlayer > 3) {
					currentPlayer = 0;
				}

			}

			lesson.setWinner(lesson.decideWinner());
			if(lesson.decideWinner().toLowerCase().equals("west")){
				currentPlayer=0;
			}else if(lesson.decideWinner().toLowerCase().equals("north")){
				currentPlayer=1;
			}else if(lesson.decideWinner().toLowerCase().equals("east")){
				currentPlayer=2;
			}else{
				currentPlayer=3;
			}
			gs.getPlayerTurnJLabel().setText("<html><h3 style='color:white'>" + lesson.decideWinner() + " wins the trick</h3></html>");
			lesson.resetPoints();
			removeCenterCards(gs.getCenterPanel());
			updateScoreBoard(gs.getScoreJLabel());
		}
		JOptionPane.showMessageDialog(gs,
				"<html><h1>"+lesson.decideGameWinner()+" won</h1></html>");
		gs.dispose();
		t.interrupt();
		currentPlayer=0;
	}

	public void autoPlay(JPanel[] panels){
		boolean noValid=false;
		int i=0;

		for(Component c:panels[currentPlayer].getComponents()){
			if (!copyBestCase.isEmpty() && (play == 0 && lesson.getPlayers().get(currentPlayer).getCard(copyBestCase.get(0)) != null ) && copyBestCase.get(0) != "CLAIM") {
				lesson.setFirstCardPlayed(copyBestCase.get(0));
			}
			if(panels[currentPlayer].getComponent(i) instanceof JLabel) {

				if (!copyBestCase.isEmpty() && (lesson.getPlayers().get(currentPlayer).getCard(copyBestCase.get(0)) != null && lesson.isValid(lesson.getPlayers().get(currentPlayer).getCard(copyBestCase.get(0)).toString(), lesson.getPlayers().get(currentPlayer)))) {

					System.out.println("Best case is in our hand and valid to play and the card we are looking at is "+lesson.getPlayers().get(currentPlayer).getCard(i).getFlipped()+"and the actual card is"+ lesson.getPlayers().get(currentPlayer).getCard(copyBestCase.get(0)).getFlipped());
					if(((JLabel)c) == lesson.getPlayers().get(currentPlayer).getCard(copyBestCase.get(0)).getCardLabel()){

						noValid = false;
						break;
					}else{
						noValid=true;
					}

				} else if (lesson.isValid(lesson.getPlayers().get(currentPlayer).getCard(i).toString(), lesson.getPlayers().get(currentPlayer))) {
					noValid = false;
					break;

				} else {
					noValid = true;
				}
			}
			i++;
		}
		if(noValid){
			lesson.getPlayers().get(currentPlayer).getCard(0).setFlipped(false);
			((JLabel)panels[currentPlayer].getComponent(0)).setIcon(lesson.getPlayers().get(currentPlayer).getCard(0).getImageIcon());
			((JLabel)panels[currentPlayer].getComponent(0)).validate();
			((JLabel)panels[currentPlayer].getComponent(0)).repaint();
			moveCard((JLabel)panels[currentPlayer].getComponent(0),currentPlayer,panels,gs.getCenterPanel());
			if(copyBestCase.get(0) == "CLAIM" && play ==0){
				lesson.setFirstCardPlayed(lesson.getPlayers().get(currentPlayer).getCard(0).toString());
			}
			//
			System.out.println("Setting first card played to "+lesson.getPlayers().get(currentPlayer).getCard(0).toString());
			lesson.getPlayers().get(currentPlayer).addPoints(lesson.getPlayPoints(lesson.getPlayers().get(currentPlayer).getCard(0).toString()));
			lesson.getPlayers().get(currentPlayer).removePlayedCard(lesson.getPlayers().get(currentPlayer).getCard(0).toString());
		}else{

			lesson.getPlayers().get(currentPlayer).getCard(i).setFlipped(false);
			((JLabel)panels[currentPlayer].getComponent(i)).setIcon(lesson.getPlayers().get(currentPlayer).getCard(i).getImageIcon());
			((JLabel)panels[currentPlayer].getComponent(i)).validate();
			((JLabel)panels[currentPlayer].getComponent(i)).repaint();
			moveCard((JLabel)panels[currentPlayer].getComponent(i),currentPlayer,panels,gs.getCenterPanel());
			if(copyBestCase.get(0) == "CLAIM" && play ==0){
				lesson.setFirstCardPlayed(lesson.getPlayers().get(currentPlayer).getCard(i).toString());
			}
			System.out.println("Setting first card played to "+lesson.getPlayers().get(currentPlayer).getCard(i).toString());
			lesson.getPlayers().get(currentPlayer).addPoints(lesson.getPlayPoints(lesson.getPlayers().get(currentPlayer).getCard(i).toString()));
			lesson.getPlayers().get(currentPlayer).removePlayedCard(lesson.getPlayers().get(currentPlayer).getCard(i).toString());

		}

	}

	public void addMouseListeners(JPanel[] panels){
		System.out.println("help level: "+lesson.getHelpLevel()+" copybest "+copyBestCase.get(0));
		for (int i = 0; i < panels[currentPlayer].getComponents().length; i++) {
			if (panels[currentPlayer].getComponent(i) instanceof JLabel) {
				if (lesson.getHelpLevel() == 0 || lesson.getHelpLevel() == 1 ) {
					panels[currentPlayer].getComponent(i).addMouseListener(cardListener);
					((JLabel) panels[currentPlayer].getComponent(i)).setBorder(null);

				} else if (lesson.getHelpLevel() == 2 || lesson.getHelpLevel()==4) {
					if (play == 0) {
						panels[currentPlayer].getComponent(i).addMouseListener(cardListener);
						((JLabel) panels[currentPlayer].getComponent(i)).setBorder(BorderFactory.createLineBorder(new Color(226, 172, 44), 4));
					} else {

						if (lesson.isValid(lesson.getPlayers().get(currentPlayer).getCard(i).toString(), lesson.getPlayers().get(currentPlayer))) {
							panels[currentPlayer].getComponent(i).addMouseListener(cardListener);
							((JLabel) panels[currentPlayer].getComponent(i)).setBorder(BorderFactory.createLineBorder(new Color(226, 172, 44), 4));
						}
					}

				}else if(lesson.getHelpLevel() == 3){
					if (play == 0) {
						panels[currentPlayer].getComponent(i).addMouseListener(cardListener);
						((JLabel) panels[currentPlayer].getComponent(i)).setBorder(BorderFactory.createLineBorder(null));
					} else {
						if (lesson.isValid(lesson.getPlayers().get(currentPlayer).getCard(i).toString(), lesson.getPlayers().get(currentPlayer))) {
							panels[currentPlayer].getComponent(i).addMouseListener(cardListener);
							((JLabel) panels[currentPlayer].getComponent(i)).setBorder(null);
						}
					}
				}else if(lesson.getHelpLevel()==5){
					if(copyBestCase.get(0).equals("CLAIM")){
						panels[currentPlayer].getComponent(i).addMouseListener(cardListener);
						((JLabel) panels[currentPlayer].getComponent(i)).setBorder(BorderFactory.createLineBorder(new Color(226, 172, 44), 4));
					}else{
						if((lesson.isValid(lesson.getPlayers().get(currentPlayer).getCard(i).toString(), lesson.getPlayers().get(currentPlayer)) || play==0) && lesson.getPlayers().get(currentPlayer).getCard(i).toString().equals(copyBestCase.get(0))){
							panels[currentPlayer].getComponent(i).addMouseListener(cardListener);
							((JLabel) panels[currentPlayer].getComponent(i)).setBorder(BorderFactory.createLineBorder(new Color(226, 172, 44), 4));
						}
					}
				}
			}
		}
	}

	public void removeMouseListeners(JPanel[] panels){
		System.out.println(panels[currentPlayer].getComponents().length);
		for(int i = 0;i<panels[currentPlayer].getComponents().length;i++){
			if(panels[currentPlayer].getComponent(i) instanceof JLabel ){
				panels[currentPlayer].getComponent(i).removeMouseListener(cardListener);
				((JLabel) panels[currentPlayer].getComponent(i)).setBorder(null);
			}
		}
	}

	public void makePlay(JLabel source,int index,int play,JPanel[] panels) throws InterruptedException {
		int jIndex=0;
		if(lesson.getPlayers().get(index).getCanPlay()){
			for(Component card:panels[index].getComponents()){
				if(source == card){
					if(lesson.getHelpLevel()==1){
						if(lesson.isValid(lesson.getPlayers().get(index).getCard(jIndex).toString(),lesson.getPlayers().get(currentPlayer))){
							playCard( lesson.getPlayers().get(index).getCard(jIndex),(JLabel)card,index);
							moveCard((JLabel)card,currentPlayer,panels,gs.getCenterPanel());
						}else{
							JOptionPane.showMessageDialog(gs,
									"That is not a valid move please choose another card");
							break;
						}
					}else if(lesson.getHelpLevel()==2){
						if(lesson.isValid(lesson.getPlayers().get(index).getCard(jIndex).toString(),lesson.getPlayers().get(currentPlayer)) && play==0){
							playCard(lesson.getPlayers().get(index).getCard(jIndex),(JLabel)card,index);
							moveCard((JLabel)card,currentPlayer,panels,gs.getCenterPanel());
							break;
						}else{
							playCard(lesson.getPlayers().get(index).getCard(jIndex),(JLabel)card,index);
							moveCard((JLabel)card,currentPlayer,panels,gs.getCenterPanel());
							break;
						}
					}else if(lesson.getHelpLevel()==3){
						if(lesson.isValid(lesson.getPlayers().get(index).getCard(jIndex).toString(),lesson.getPlayers().get(currentPlayer)) && lesson.getPlayers().get(currentPlayer).getCard(jIndex).toString().equals(copyBestCase.get(0))) {
							playCard(lesson.getPlayers().get(index).getCard(jIndex), (JLabel) card, index);
							moveCard((JLabel) card, currentPlayer,panels,gs.getCenterPanel());
							break;
						}else if(copyBestCase.get(0).equals("CLAIM")){
							playCard(lesson.getPlayers().get(index).getCard(jIndex), (JLabel) card, index);
							moveCard((JLabel) card, currentPlayer,panels,gs.getCenterPanel());
							JOptionPane.showMessageDialog(gs,
									"You should claim now");
						}else{
							JOptionPane.showMessageDialog(gs,
									"That is not card you should play");
						}
					}else if(lesson.getHelpLevel()==4){
						System.out.println(lesson.isValid(lesson.getPlayers().get(index).getCard(jIndex).toString(),lesson.getPlayers().get(currentPlayer))+" " + lesson.getPlayers().get(currentPlayer).getCard(jIndex).toString()+" "+copyBestCase.get(0));
						if((lesson.isValid(lesson.getPlayers().get(index).getCard(jIndex).toString(),lesson.getPlayers().get(currentPlayer)) || play==0) && lesson.getPlayers().get(currentPlayer).getCard(jIndex).toString().equals(copyBestCase.get(0))){
							playCard( lesson.getPlayers().get(index).getCard(jIndex),(JLabel)card,index);
							moveCard((JLabel)card,currentPlayer,panels,gs.getCenterPanel());
							break;
						}else if(copyBestCase.get(0).equals("CLAIM")) {
							playCard(lesson.getPlayers().get(index).getCard(jIndex), (JLabel) card, index);
							moveCard((JLabel) card, currentPlayer,panels,gs.getCenterPanel());
							JOptionPane.showMessageDialog(gs,
									"You should claim now");
						}else{
							JOptionPane.showMessageDialog(gs,
									"That is not card you should play");
						}
					}else if(lesson.getHelpLevel()==5){
						if((lesson.isValid(lesson.getPlayers().get(index).getCard(jIndex).toString(),lesson.getPlayers().get(currentPlayer)) || play==0)&& lesson.getPlayers().get(currentPlayer).getCard(jIndex).toString().equals(copyBestCase.get(0))){
							playCard( lesson.getPlayers().get(index).getCard(jIndex),(JLabel)card,index);
							moveCard((JLabel)card,currentPlayer,panels,gs.getCenterPanel());
							break;
						}else if(copyBestCase.get(0).equals("CLAIM")){
							playCard(lesson.getPlayers().get(index).getCard(jIndex), (JLabel) card, index);
							moveCard((JLabel) card, currentPlayer,panels,gs.getCenterPanel());
							JOptionPane.showMessageDialog(gs,
									"You should claim now");
						}
					}else{
						if(lesson.isValid(lesson.getPlayers().get(index).getCard(jIndex).toString(),lesson.getPlayers().get(currentPlayer))){
							playCard( lesson.getPlayers().get(index).getCard(jIndex),(JLabel)card,index);
							moveCard((JLabel)card,currentPlayer,panels,gs.getCenterPanel());
						}
					}


				}
				jIndex++;
			}


		}
	}

	public void playCard(Card c,JLabel card,int playerIndex){
		c.setFlipped(false);
		card.setIcon(c.getImageIcon());
		card.validate();
		card.repaint();
		if(play ==0){
			System.out.println("setting firstCard "+c.toString());
			lesson.setFirstCardPlayed(c.toString());
		}
		lesson.getPlayers().get(playerIndex).addPoints(lesson.getPlayPoints(c.toString()));
		lesson.getPlayers().get(playerIndex).removePlayedCard(c.toString());
		played=true;

	}

	public int findPlayer(JLabel source,JPanel[] panels) throws InterruptedException {

		Container parent = source.getParent();
		for(int i=0;i<panels.length;i++){
			if(panels[i] == parent){
				return i;
			}
		}
		return -1;
	}

	public void removeCenterCards(JPanel centerPanel)throws InterruptedException{
		t.sleep(2000);
		System.out.println("num comps "+centerPanel.getComponents().length);

		for(Component c:centerPanel.getComponents()){
			if(c instanceof JLabel ){
				centerPanel.remove(((JLabel)c));
				centerPanel.validate();
				centerPanel.repaint();
			}
		}
	}

	public void updateScoreBoard(JLabel score){
		score.setText("<html><head><style>body{color:white;}</style></head><body><h1>N + S Score: "+(lesson.getPlayers().get(1).getTrickWins()+lesson.getPlayers().get(3).getTrickWins())+"</h1><h2>W + E Score: "+(lesson.getPlayers().get(0).getTrickWins()+lesson.getPlayers().get(2).getTrickWins())+"</h2></body></html>");
	}

	public void moveCard(JLabel source,int index,JPanel[] panels,JPanel centerPanel){
		source.setBorder(null);
		panels[index].remove(source);
		panels[index].validate();
		panels[index].repaint();
		centerPanel.add(source);
		if(index==1 || index==3){
			source.setLocation(centerPanel.getWidth()/2 - source.getWidth()/2,centerPanel.getHeight()/2 + (((index-2)*source.getWidth()/2)-source.getWidth()/2));
			source.removeMouseListener(cardListener);
		}else{
			source.setLocation(centerPanel.getWidth()/2 - source.getWidth() + index*(source.getWidth()/2),centerPanel.getHeight()/2 - source.getWidth()/2 );
		}
		centerPanel.validate();
		centerPanel.repaint();
	}
}
