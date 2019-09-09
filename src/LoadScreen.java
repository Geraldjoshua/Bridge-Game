import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoadScreen extends JFrame {
	
	private final int xSize,ySize;
	private JProgressBar loadBar;
	private BackgroundPanel bgPanel;

	LoadScreen(int xSize,int ySize) throws IOException {
		this.xSize = xSize;
		this.ySize = ySize;
        	this.setUndecorated(true);
		this.bgPanel = new BackgroundPanel(new File("images/background.jpg"));
		this.setSize(bgPanel.getSize());
		this.setLocation(xSize/2 - this.getWidth()/2,ySize/2 - this.getHeight()/2);
		addComponentsToPanel();
		bgPanel.addBackGround();
		this.add(bgPanel);
	}

	public void addComponentsToPanel() throws IOException{
		
		JLabel l = makeAndGetLabel("<html><span style='color:white;font-size:120%;'>Welcome To</span><h1 style='color:white;font-size:150%;margin-top:0px;'>Bridge Tutor v 1.0</h1></html>");
		this.loadBar = makeAndGetProgressBar(l.getHeight(),l.getWidth());
		this.bgPanel.add(loadBar);
		this.bgPanel.add(l);
		
	}

	public JLabel makeAndGetLabel(String text){
		JLabel l=new JLabel(text);
		l.setSize(l.getPreferredSize());
		l.setLocation(this.getWidth()/2 - l.getWidth()/2,this.getHeight()/2 - l.getHeight()/2);
		return l;
	}

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
	
	public JProgressBar getLoadBar(){
		return loadBar;
	}

}
