
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class MenuScreen extends JFrame{
	
	private final int xSize,ySize,framePadding = 100;
	private final Dimension smallButton = new Dimension(20,20),largeButton = new Dimension(200,50);
	private int headingHeight;
	private BackgroundPanel bgPanel;
	private JButton exitButton,selectLesson,toggleHelpLevel,backButton;
	private ArrayList<JButton> lessonButtons,menuButtons;

        /**
         * constructor
         * @param xSize of the menu screen
         * @param ySize of the menu screen
         * @throws IOException input output exception
         */
	MenuScreen(int xSize, int ySize)throws IOException{
		this.xSize = xSize;
		this.ySize = ySize;
		this.setUndecorated(true);
		this.bgPanel = new BackgroundPanel(new File("images/background2.jpg"));
		this.setSize(bgPanel.getSize());
		this.lessonButtons = new ArrayList<JButton>();
		this.menuButtons = new ArrayList<JButton>();
		addComponentsToPanel();
		bgPanel.addBackGround();		
		this.add(bgPanel);
		this.setLocation(xSize/2 - this.getWidth()/2,ySize/2 - this.getHeight()/2);
		
	}

        /**
         * adds all components to the panel
         */
	public void addComponentsToPanel(){
		
		initExitButton();
		initHeader();
		initSelectLessonButton();
		initToggleHelpLevelButton();
		initBackButton();
		generateLessonButtons();
	}

        /**
         * exits the game
         */
	public void initExitButton(){
		exitButton = new JButton( new AbstractAction("EXIT") {
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
         * initialize heading bridge tutor
         */
	public void initHeader(){
		JLabel header = new JLabel("<html><h1 style='color:white;font-weight:bold;'>Bridge Tutor v 1.0</h1></html>");
		header.setSize(header.getPreferredSize());
		header.setLocation(this.getWidth()/2 - header.getWidth()/2,20);
		headingHeight = header.getHeight();
		bgPanel.add(header);
	}

        /**
         * initializes the J-button
         * @param b Jbutton object
         * @param text takes a string
         * @param bg takes a color object
         * @param tc takes a color object 
         * @param d takes a dimension object
         * @param p takes a point object
         * @param visible true for buttons visible else false
         * @return Jbutton button object
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
         * method to generate lesson buttons
         */
	public void generateLessonButtons(){
		int y = headingHeight + framePadding + 50;
        int x = framePadding;
        for(int i=1;i<new File("input").listFiles().length;i++){
            JButton j= new JButton(i+"");
			
            j=initButton(j,i+"",Color.red,Color.white,new Dimension((int)(j.getPreferredSize().getWidth()+50),(int)(j.getPreferredSize().getHeight()+30)),new Point(x,y),false);
            x += (int) ((j.getWidth()) + 50);
            if(x>bgPanel.getWidth()-framePadding- i*50){
                x=framePadding;
                y+=10+j.getHeight();
            }
            j.setVisible(false);
            lessonButtons.add(j);
            bgPanel.add(j);
        }

	}

        /**
         * method to initialize select button
         */
	public void initSelectLessonButton(){
		selectLesson=initButton(selectLesson,"<html><h3 style='color:red;'>SELECT LESSON</h3></html>",Color.white,Color.red,largeButton,new Point(this.getWidth()/2,100 + headingHeight),true);
		menuButtons.add(selectLesson);
		bgPanel.add(selectLesson);
	}
	
        /**
         * method to initialize the get help level button
         */
	public void initToggleHelpLevelButton(){
		toggleHelpLevel = initButton(toggleHelpLevel,"<html><h3 style='color:red;'>NO HELP</h3></html>",Color.white,Color.red,largeButton,new Point(this.getWidth()/2,selectLesson.getLocation().y + selectLesson.getHeight() + 40),true);
		toggleHelpLevel.setFocusPainted(false);
		menuButtons.add(toggleHelpLevel);
		bgPanel.add(toggleHelpLevel);
	}

        /**
         * method to initialize the back button
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
         * 
         * @return Jbutton object
         */
	public JButton getBackButton(){
		return backButton;
	}
	
        /**
         * adds listener
         * @param ml mouselistener object
         */
	public void addSelectLessonListener(MouseListener ml){
		selectLesson.addMouseListener(ml);
	}
	
        /**
         * adds listener
         * @param ml mouselistener object
         */
	public void addBackButtonListener(MouseListener ml){
		backButton.addMouseListener(ml);
	}
        /**
         * adds listener
         * @param ml mouselistener object
         * @param index index to the button requiring mouse listener
         */
	public void addLessonListener(MouseListener ml,int index) {
		lessonButtons.get(index).addMouseListener(ml);
	}

        /**
         * adds toggling help
         * @param ml mouselistener object 
         */
	public void addToggleHelpLevel(MouseListener ml){
		toggleHelpLevel.addMouseListener(ml);
	}

        /**
         * gets the lesson buttons in an array form
         * @return array list of lesson Jbuttons
         */
	public ArrayList<JButton> getLessonButtons() {
		return lessonButtons;
	}

        /**
         * gets menu buttons in an array form
         * @return array list of menu Jbuttons
         */
	public ArrayList<JButton> getMenuButtons(){
		return menuButtons;
	}
}
