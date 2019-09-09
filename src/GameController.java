import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameController {
	
	private LoadScreen ls;
	private MenuScreen ms;
	private GameScreen gs;
	private Lesson lesson;
	private MouseListener selectButtonListener,lessonButtonListener,toggleButtonListener,backButtonListener,cardListener;
	private MouseListener exitGameListener,getHintsListener,getTipsListener,claimListener,flipCardsListener;
	private int clicks = 0,play=0,timesTipClicked = 0,currentPlayer,hintQuestionNumber = 0,tricks=0,timesClicked = 0;
	private ArrayList<String> copyBestCase,hintQuestions;
	private final int xSize,ySize;
	GameController(LoadScreen ls,MenuScreen ms,int xSize,int ySize)throws InterruptedException{
		this.ls = ls;
		this.ms = ms;
		this.xSize = xSize;
		this.ySize = ySize;
		this.currentPlayer = 0;
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
		    }
            
    	};
		this.backButtonListener = new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
				showButtons(true,ms.getMenuButtons());
				showButtons(false,ms.getLessonButtons());
				showBackButton(false,(JButton)e.getSource());
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
				/*try {
					//makePlay((JLabel)e.getSource(),findPlayer((JLabel)e.getSource()),play);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}*/
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
}
