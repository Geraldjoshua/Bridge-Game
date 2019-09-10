
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuScreen extends JFrame{
	
	private final int xSize,ySize,framePadding = 100;
	private final Dimension largeButton = new Dimension(200,50);
	private int headingHeight;
	private BackgroundPanel bgPanel;
	private JLabel selectLessonHeader;
	private JButton selectLesson,toggleHelpLevel,backButton;
	private ArrayList<JButton> lessonButtons,menuButtons;

	/**
	 * <p>Constructor that initialises menu screen and its components</p>
	 * @param xSize int
	 * @param ySize int
	 * @throws IOException
	 */
	MenuScreen(int xSize, int ySize)throws IOException{
		this.xSize = xSize;
		this.ySize = ySize;
		this.setUndecorated(true);
		this.bgPanel = new BackgroundPanel(new File("images/background2.jpg"));
		this.setSize(bgPanel.getSize());
		this.lessonButtons = new ArrayList<JButton>();
		this.menuButtons = new ArrayList<JButton>();
		makeAndAddComponentsToPanel();
		bgPanel.addBackGround();		
		this.add(bgPanel);
		//Center the JFrame to the center of the screen
		this.setLocation(xSize/2 - this.getWidth()/2,ySize/2 - this.getHeight()/2);
		
	}

	/**
	 * <p>Method that initialises and adds the swing components that will be on the MenuScreen</p>
	 */
	public void makeAndAddComponentsToPanel(){
		
		initExitButton();
		initHeader();
		initSelectLessonHeader();
		initSelectLessonButton();
		initToggleHelpLevelButton();
		initBackButton();
		generateLessonButtons();
	}

	/**
	 * <p>Method that initialises the exit button and gives it an abstract action listener that will close the software and adds it to the MenuScreen</p>
	 */
	public void initExitButton(){
		JButton exitButton = new JButton( new AbstractAction("EXIT") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                System.exit(0);
		    }
		});
		exitButton.setText("<html><span style='font-size:200%;font-weight:bold;color:white;'>&#10005;</span></html>");
		exitButton.setSize(exitButton.getPreferredSize());
		exitButton.setContentAreaFilled(false);
   		exitButton.setBorderPainted(false);
    	exitButton.setLocation(this.getWidth()-exitButton.getWidth()-20,20);
		exitButton.setFocusPainted(false);
		bgPanel.add(exitButton);
	}

	/**
	 * <p>Method that initialises the header JLabel and adds it to the MenuScreen</p>
	 */
	public void initHeader(){
		JLabel header = new JLabel("<html><h1 style='color:white;font-weight:bold;'>Bridge Tutor v 1.0</h1></html>");
		header.setSize(header.getPreferredSize());
		header.setLocation(this.getWidth()/2 - header.getWidth()/2,20);
		headingHeight = header.getHeight();
		bgPanel.add(header);
	}

	/**
	 * <p>Method that initialises the selectLessonHeader and places it on the screen</p>
	 */
	public void initSelectLessonHeader(){

		selectLessonHeader = new JLabel("<html><h1 style='color:white;font-weight:bold;'>MAIN MENU</h1></html>");
		selectLessonHeader.setSize(selectLessonHeader.getPreferredSize());
		selectLessonHeader.setLocation(this.getWidth()/2 - selectLessonHeader.getWidth()/2,framePadding );
		bgPanel.add(selectLessonHeader);
	}

	/**
	 * <p>Method that initialises button b with all the attributes passed into the method</p>
	 * @param b JButton
	 * @param text String
	 * @param bg Color
	 * @param tc Color
	 * @param d Dimension
	 * @param p Point
	 * @param visible Boolean
	 * @return JButton
	 */
	public JButton initButton(JButton b,String text,Color bg,Color tc,Dimension d,Point p,boolean visible){
		b = new JButton(text);
		b.setSize(d);
		b.setLocation(p.x - b.getWidth()/2,p.y);
		b.setBackground(bg);
		b.setForeground(tc);
		b.setVisible(visible);
		return b;
	}


	/**
	 * <p>Method that generates a JButton for each input file in the input folder and attaches it to the MenuScreen</p>
	 */
	private void generateLessonButtons(){
		int y = headingHeight + framePadding + 50;
        int x = framePadding;
        //Loop generates buttons based on number of input files in input folder
        for(int i=1;i<new File("input").listFiles().length;i++){
            JButton j= new JButton("<html><span style='font-size:150%;font-weight:bold;'>"+i+"</span></html>");
            //Call to method that gives button these attributes
            j=initButton(j,
					i+"",
					Color.white,Color.red,
					new Dimension((int)(j.getPreferredSize().getWidth()+50),
					(int)(j.getPreferredSize().getHeight()+30)),
					new Point(x,y),
					false);

            j.setBorder(BorderFactory.createLineBorder(Color.red,1));
            x +=  ((j.getWidth()) + 50);
            if(x>bgPanel.getWidth()-framePadding){
                x=framePadding;
                y+=10+j.getHeight();
            }
            lessonButtons.add(j);
            bgPanel.add(j);
        }

	}

	/**
	 * <p>Method that initialises selectLessonButton</p>
	 */
	public void initSelectLessonButton(){
		selectLesson=initButton(selectLesson,"<html><h3 style='color:red;'>SELECT LESSON</h3></html>",Color.white,Color.red,largeButton,new Point(this.getWidth()/2,selectLessonHeader.getLocation().y + selectLessonHeader.getHeight() + 20),true);
		menuButtons.add(selectLesson);
		bgPanel.add(selectLesson);
	}

	/**
	 * <p>Method that initialises toggleHelpLevelButton</p>
	 */
	public void initToggleHelpLevelButton(){
		toggleHelpLevel = initButton(toggleHelpLevel,"<html><h3 style='color:red;'>NO HELP</h3></html>",Color.white,Color.red,largeButton,new Point(this.getWidth()/2,selectLesson.getLocation().y + selectLesson.getHeight() +20),true);
		toggleHelpLevel.setFocusPainted(false);
		menuButtons.add(toggleHelpLevel);
		bgPanel.add(toggleHelpLevel);
	}

	/**
	 * <p>Method that initialises the backButton</p>
	 */
	public void initBackButton(){
		backButton = initButton(backButton,"<html><span style='font-size:300%;font-weight:bold;'>&larr;</span></html>",Color.white,Color.white,new Dimension(20,20),new Point(20,20),false);
		backButton.setContentAreaFilled(false);
   		backButton.setBorderPainted(false);
		backButton.setFocusPainted(false);
		backButton.setSize(backButton.getPreferredSize());
		bgPanel.add(backButton);
	}

	/**
	 * <p>Method that gets the backButton</p>
	 * @return JButton
	 */
	public JButton getBackButton(){
		return backButton;
	}

	/**
	 * <p>Method that adds a MouseListener to the selectLessonButton</p>
	 * @param ml MouseListener
	 */
	public void addSelectLessonListener(MouseListener ml){
		selectLesson.addMouseListener(ml);
	}

	/**
	 * <p>Method that adds a MouseListener to the backButton</p>
	 * @param ml MouseListener
	 */
	public void addBackButtonListener(MouseListener ml){
		backButton.addMouseListener(ml);
	}
	/**
	 * <p>Method that adds a MouseListener to the lessonButton</p>
	 * @param ml MouseListener
	 */
	public void addLessonListener(MouseListener ml,int index) {
		lessonButtons.get(index).addMouseListener(ml);
	}
	/**
	 * <p>Method that adds a MouseListener to the toggleHelpLevelButton</p>
	 * @param ml MouseListener
	 */
	public void addToggleHelpLevelListener(MouseListener ml){
		toggleHelpLevel.addMouseListener(ml);
	}

	/**
	 * <p>Method that returns ArrayList containing all lesson buttons</p>
	 * @return ArrayList
	 */
	public ArrayList<JButton> getLessonButtons() {
		return lessonButtons;
	}
	/**
	 * <p>Method that returns ArrayList containing all menu buttons</p>
	 * @return ArrayList
	 */
	public ArrayList<JButton> getMenuButtons(){
		return menuButtons;
	}
	/**
	 * <p>Method that returns JLabel selectLessonHeader</p>
	 * @return ArrayList
	 */
	public JLabel getSelectLessonHeader(){
		return selectLessonHeader;
	}
}
