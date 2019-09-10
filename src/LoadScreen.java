import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoadScreen extends JFrame {
	
	private final int xSize,ySize;
	private JProgressBar loadBar;	//The components of the screen
	private BackgroundPanel bgPanel;

	/**
	 * <p>Constructor which initialises the object and all the components that will be added to it.
	 * The first argument is the screen width and the second argument is the screen height</p>
	 * @param xSize
	 * @param ySize
	 * @throws IOException
	 */
	LoadScreen(int xSize,int ySize) throws IOException {
		this.xSize = xSize;
		this.ySize = ySize;
		this.setUndecorated(true);
		this.bgPanel = new BackgroundPanel(new File("images/background.jpg"));
		this.setSize(bgPanel.getSize());
		//Position this frame at the center of the screen
		this.setLocation(xSize/2 - this.getWidth()/2,ySize/2 - this.getHeight()/2);
		//initialise all the components
		makeAndAddComponentsToPanel();
		bgPanel.addBackGround();
		this.add(bgPanel);
	}

	/**
	 * <p>Method which will initialise the JLabel and ProgressBar attributes of the class and then add them to the BackgroundPanel attribute</p>
	 * @throws IOException
	 */
	public void makeAndAddComponentsToPanel() throws IOException{
		
		JLabel l = makeAndGetLabel("<html><span style='color:white;font-size:120%;'>Welcome To</span><h1 style='color:white;font-size:150%;margin-top:0px;'>Bridge Tutor v 1.0</h1></html>");
		this.loadBar = makeAndGetProgressBar(l.getHeight(),l.getWidth());
		//add components to BackgroundPanel bgPanel
		this.bgPanel.add(loadBar);
		this.bgPanel.add(l);
		
	}

	/**
	 * <p>Method that initialises the JLabel with the text parameter and sets the size of the JLabel be big enough to fit that text.
	 * The JLabel is then positioned to the center of the object</p>
	 * @param text String
	 * @return JLabel
	 */
	public JLabel makeAndGetLabel(String text){
		JLabel l=new JLabel(text);
		l.setSize(l.getPreferredSize());
		l.setLocation(this.getWidth()/2 - l.getWidth()/2,this.getHeight()/2 - l.getHeight()/2);
		return l;
	}

	/**
	 * <p>Method that initialises the JProgressBar component so that it is white with no background. This method also sets the location of the JProgressBar
	 * so it is below the JLabel on the LoadScreen </p>
	 * @param yPos int
	 * @param width int
	 * @return JProgressBar
	 */
	public JProgressBar makeAndGetProgressBar(int yPos,int width){
		JProgressBar loadBar = new JProgressBar(SwingConstants.HORIZONTAL,0,width);
		loadBar.setSize(width,3);	
		loadBar.setLocation(this.getWidth()/2 - loadBar.getWidth()/2,this.getHeight()/2 - loadBar.getHeight()/2 + yPos - 20);
		loadBar.setOpaque(false);
		loadBar.setBorderPainted(false);
		loadBar.setValue(0);
		loadBar.setBorder(null);
		loadBar.setForeground(Color.WHITE);
		
		return loadBar;
	}

	/**
	 * <p>Method that returns the loadBar</p>
	 * @return JProgressBar
	 */
	public JProgressBar getLoadBar(){
		return loadBar;
	}

}
