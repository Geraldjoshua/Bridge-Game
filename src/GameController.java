
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
	/**
	 * <p>Constructor that initialises the GameController object and initialises the programs' MouseListeners and adds
	 * them to the relevant buttons</p>
	 * @param ls LessonScreen
	 * @param ms MenuScreen
	 * @param xSize int
	 * @param ySize int
	 * @throws InterruptedException
	 */
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

	/**
	 * <p>Method that initialises all the mouse listeners that will be used in the program</p>
	 */
	public void initListeners(){
		//Mouse listener for the select lesson button on MenuScreen ms
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
		//MouseListener for backButton on MenuScreen ms
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
		//MouseListener for lesson button on MenuScreen ms
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
		//MouseListener for toggle help level button on MenuScreen ms
		this.toggleButtonListener = new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
				toggleButton(e,ms.getMenuButtons());
		    }

    	};
		//MouseListener for every card object on GameScreen gs
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
		//Mouse listener for exit button on GameScreen gs
		this.exitGameListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				gs.dispose();
				t.interrupt();
				currentPlayer = 0;

			}

		};
		//Mouse listener for getTips button on GameScreen gs
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
		//Mouse listener for getHints button on GameScreen gs
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
		//Mouse listener for claimListener button on GameScreen gs
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
	/**
	 * <p>Method that adds listeners to elements that have been initialised already</p>
	 */
	public void addListeners(){
		ms.addSelectLessonListener(selectButtonListener);
		addLessonListeners(ms.getLessonButtons());
		ms.addToggleHelpLevelListener(toggleButtonListener);
		ms.addBackButtonListener(backButtonListener);

	}
	/**
	 * <p>Method disposes of the LoadScreen when Load function finish executing it is finished loading and then shows the MenuScreen as well as
	 * pop up that makes user aware of toggle button functionality</p>
	 * @throws InterruptedException
	 */
	public void loadProgram()throws InterruptedException{

		ls.setVisible(true);
		load(ls.getLoadBar(),ls.getLoadBar().getWidth());
		ls.dispose();
		ms.setVisible(true);
		Thread.sleep(500);
		JOptionPane.showMessageDialog(ms,
                                    "<html><h3>Select the toggle help button to toggle the <br>help level for the lesson from No help - help level 5</h3></html>");
	}
	/**
	 * <p>Method that loads the JProgressBar with increasing speed until it has finished loading</p>
	 * @param loadBar int
	 * @param width int
	 * @throws InterruptedException
	 */
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
	/**
	 * <p>Method that initialises the lesson that is mapped to a lesson button, the GameScreen, hintQuestions and copyBestCase.</p>
	 * @param e MouseEvent
	 * @param buttons ArrayList
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void findLesson(MouseEvent e,ArrayList<JButton> buttons)throws IOException, InterruptedException{

		for(int i=0;i<buttons.size();i++){

			if(e.getSource() instanceof JButton && e.getSource() == buttons.get(i)){
				lesson = new Lesson("input/input"+(i+1)+".txt",clicks);//initialises lesson based on index of button clicked
				gs = new GameScreen(xSize,ySize,lesson);						//and difficulty level
				hintQuestions = new ArrayList<>(lesson.getHints().keySet());
				copyBestCase = lesson.getBestCase();
				gs.setVisible(true);
				gs.addExitListener(exitGameListener);		//All listeners for GameScreen components are added
				gs.addGetTipsListener(getTipsListener);		//Now that they have been initialised
				gs.addGetHintsListener(getHintsListener);
				gs.addClaimListener(claimListener);
				gs.addFlipCardsListener(flipCardsListener);
				t = new Thread(this);					//create new thread to run game on
				t.start();									//start thread thus calling run
			}
		}

	}
	/**
	 * <p>Method that handles when a person clicks the toggle help level button. The help level gets incremented each time a
	 * person clicks the button. if help level ever gets greater than 5 it resets to 0. The buttons text is also updated
	 * based on what the help level is set to</p>
	 * @param e MouseEvent
	 * @param buttons ArrayList
	 */
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
	/**
	 * <p>Method that finds the button index of the button where the MouseEvent originated from and then changes the colours of
	 * the source's text and background</p>
	 * @param e MouseEvent
	 * @param buttons ArrayList
	 */
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
	/**
	 * <p>Method that resets the buttons colour back to the default colours</p>
	 * @param b JButton
	 */
	public void resetColour(JButton b){
		b.setForeground(Color.red);
		b.setBackground(Color.white);
	}
	/**
	 * <p>Method that sets the tooltip for the lesson button that was hovered over</p>
	 * @param b JButton
	 * @param index int
	 */
	public void handleToolTip(JButton b,int index){
		b.setToolTipText("<html><h4>This is a lesson for scenario "+(index+1)+"</h4></html>");
	}
	public void addLessonListeners(ArrayList<JButton> buttons){
		for(int i=0;i<buttons.size();i++){
			ms.addLessonListener(lessonButtonListener,i);
		}
	}
	/**
	 * <p>Method that sets visibility of the JButtons in the buttons ArrayList based on what visible parameter is. </p>
	 * @param visible boolean
	 * @param buttons ArrayList
	 */
	public void showButtons(boolean visible,ArrayList<JButton> buttons){
		for(int i=0;i<buttons.size();i++){
			buttons.get(i).setVisible(visible);
		}
	}
	/**
	 * <p>Method that sets visibility of baackButton on MenuScreen based on value of visible parameter</p>
	 * @param visible boolean
	 * @param b JButton
	 */
	public void showBackButton(boolean visible,JButton b){
		b.setVisible(visible);
	}
	/**
	 * <p>Method that offsets the card JLabel's y coordinate by the amount of the offset parameter </p>
	 * @param source JLabel
	 * @param offset int
	 */
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
	/**
	 * <p>Method that verifies whether a person can actually claim at this time in the game</p>
	 * @param panels JPanel[]
	 */
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
	/**
	 * <p>Method that flips the cards of the opponent team</p>
	 * @param panels JPanel[]
	 */
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
	/**
	 * <p>Method that handles all the boolean logic for an entire game. Determines who plays a card, who wins a trick and who wins the game</p>
	 * @throws InterruptedException
	 */
	public void startGame() throws InterruptedException{
		for(;tricks<13;tricks++) {
			for (play = 0; play < 4; play++) {
				played = false;

				lesson.getPlayers().get(currentPlayer).setCanPlay(true);
				gs.getPlayerTurnJLabel().setText("<html><h3 style='color:white;'>"+lesson.getPlayers().get(currentPlayer).getPlayerName()+" is playing...</h1></html>");

				if (!lesson.getPlayers().get(currentPlayer).getPlayerName().toLowerCase().equals("south") && !lesson.getPlayers().get(currentPlayer).getPlayerName().toLowerCase().equals("north")) {
					t.sleep(2000);
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
			t.sleep(2000);
			removeCenterCards(gs.getCenterPanel());
			updateScoreBoard(gs.getScoreJLabel());
		}
		JOptionPane.showMessageDialog(gs,
				"<html><h1>"+lesson.decideGameWinner()+" won</h1></html>");
		gs.dispose();
		t.interrupt();
		currentPlayer=0;
	}
	/**
	 * <p>Method that searches through all the cards in the currentplayers hand and plays a valid card for them</p>
	 * @param panels JPanel[]
	 */
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
				System.out.println("Setting first card played to "+lesson.getPlayers().get(currentPlayer).getCard(0).toString());
				lesson.setFirstCardPlayed(lesson.getPlayers().get(currentPlayer).getCard(0).toString());
			}
			//
			System.out.println("adding "+lesson.getPlayPoints(lesson.getPlayers().get(currentPlayer).getCard(0).toString())+"points");
			lesson.getPlayers().get(currentPlayer).addPoints(lesson.getPlayPoints(lesson.getPlayers().get(currentPlayer).getCard(0).toString()));
			lesson.getPlayers().get(currentPlayer).removePlayedCard(lesson.getPlayers().get(currentPlayer).getCard(0).toString());
		}else{

			lesson.getPlayers().get(currentPlayer).getCard(i).setFlipped(false);
			((JLabel)panels[currentPlayer].getComponent(i)).setIcon(lesson.getPlayers().get(currentPlayer).getCard(i).getImageIcon());
			((JLabel)panels[currentPlayer].getComponent(i)).validate();
			((JLabel)panels[currentPlayer].getComponent(i)).repaint();
			moveCard((JLabel)panels[currentPlayer].getComponent(i),currentPlayer,panels,gs.getCenterPanel());
			if(play ==0){
				System.out.println("Setting first card played to "+lesson.getPlayers().get(currentPlayer).getCard(i).toString());
				lesson.setFirstCardPlayed(lesson.getPlayers().get(currentPlayer).getCard(i).toString());
			}
			System.out.println("adding "+lesson.getPlayPoints(lesson.getPlayers().get(currentPlayer).getCard(i).toString())+"points");
			lesson.getPlayers().get(currentPlayer).addPoints(lesson.getPlayPoints(lesson.getPlayers().get(currentPlayer).getCard(i).toString()));
			lesson.getPlayers().get(currentPlayer).removePlayedCard(lesson.getPlayers().get(currentPlayer).getCard(i).toString());

		}

	}
	/**
	 * <p>Method that adds MouseListeners to the card JLabels of the player depending on what the help level is set to</p>
	 * @param panels JPanel[]
	 */
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
	/**
	 * <p>Method that removes MouseListeners from players cards when it is not their turn</p>
	 * @param panels JPanel[]
	 */
	public void removeMouseListeners(JPanel[] panels){
		System.out.println(panels[currentPlayer].getComponents().length);
		for(int i = 0;i<panels[currentPlayer].getComponents().length;i++){
			if(panels[currentPlayer].getComponent(i) instanceof JLabel ){
				panels[currentPlayer].getComponent(i).removeMouseListener(cardListener);
				((JLabel) panels[currentPlayer].getComponent(i)).setBorder(null);
			}
		}
	}
	/**
	 * <p>Method that handles the players play depending on the help level </p>
	 * @param source JLabel
	 * @param index int
	 * @param play int
	 * @param panels JPanel
	 * @throws InterruptedException
	 */
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
	/**
	 * <p>Method that adds points to the person who played the card based on its point value, sets the firstcard played if
	 * its the first play of a trick,removes the Card object from the Person objects hand and updates the system that they have played</p>
	 * @param c Card
	 * @param card JLabel
	 * @param playerIndex int
	 */
	public void playCard(Card c,JLabel card,int playerIndex){
		c.setFlipped(false);
		card.setIcon(c.getImageIcon());
		card.validate();
		card.repaint();
		System.out.println("play is: "+play);
		if(play ==0){
			System.out.println("setting firstCard "+c.toString());
			lesson.setFirstCardPlayed(c.toString());
		}
		System.out.println("adding "+lesson.getPlayPoints(c.toString())+"points");
		lesson.getPlayers().get(playerIndex).addPoints(lesson.getPlayPoints(c.toString()));
		lesson.getPlayers().get(playerIndex).removePlayedCard(c.toString());
		played=true;

	}
	/**
	 * <p> Method to loop through the player panels to get the index of the player who attempted to play a card</p>
	 * @param source JLabel
	 * @param panels JPanel[]
	 * @return integer
	 */
	public int findPlayer(JLabel source,JPanel[] panels) throws InterruptedException {

		Container parent = source.getParent();
		for(int i=0;i<panels.length;i++){
			if(panels[i] == parent){
				return i;
			}
		}
		return -1;
	}
	/**
	 * <p>Method that removes all the cards from the centerpanel after a trick has played</p>
	 * @param centerPanel JPanel
	 * @throws InterruptedException
	 */
	public void removeCenterCards(JPanel centerPanel)throws InterruptedException{

		for(Component c:centerPanel.getComponents()){
			if(c instanceof JLabel ){
				centerPanel.remove(((JLabel)c));
				centerPanel.validate();
				centerPanel.repaint();
			}
		}
	}
	/**
	 * <p>Method that updates the score board with the trick wins of the teams</p>
	 * @param score JLabel
	 */
	public void updateScoreBoard(JLabel score){
		score.setText("<html><head><style>body{color:white;}</style></head><body><h1>N + S Score: "+(lesson.getPlayers().get(1).getTrickWins()+lesson.getPlayers().get(3).getTrickWins())+"</h1><h2>W + E Score: "+(lesson.getPlayers().get(0).getTrickWins()+lesson.getPlayers().get(2).getTrickWins())+"</h2></body></html>");
	}
	/**
	 * <p>Method that moves the card to a certain location in the centerpanel based on its the index of the player who played the card</p>
	 * @param source JLabel
	 * @param index int
	 * @param panels JPanel[]
	 * @param centerPanel JPanel
	 */
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
