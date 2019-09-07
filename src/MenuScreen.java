
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuScreen extends JFrame{
	
	private final int xSize,ySize;
	private final Dimension smallButton = new Dimension(20,20),largeButton = new Dimension(200,50);
	private int headingHeight;
	private BackgroundPanel bgPanel;
	private JButton exitButton,selectLesson;

	MenuScreen(int xSize, int ySize)throws IOException{
		this.xSize = xSize;
		this.ySize = ySize;
		this.setUndecorated(true);
		this.bgPanel = new BackgroundPanel(new File("images/background2.jpg"));
		this.setSize(bgPanel.getSize());
		addComponentsToPanel();
		bgPanel.addBackGround();		
		this.add(bgPanel);
		this.setLocation(xSize/2 - this.getWidth()/2,ySize/2 - this.getHeight()/2);
		
	}

	public void addComponentsToPanel(){
		
		initExitButton();
		initHeader();
		initSelectLessonButton();
		
	}

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

	public void initHeader(){
		JLabel header = new JLabel("<html><h1 style='color:white;font-weight:bold;'>Bridge Tutor v 1.0</h1></html>");
		header.setSize(header.getPreferredSize());
		header.setLocation(this.getWidth()/2 - header.getWidth()/2,20);
		headingHeight = header.getHeight();
		bgPanel.add(header);
	}

	public JButton initButton(JButton b,String text,Color bg,Color tc,Dimension d,Point p,boolean visible){
		b = new JButton(text);
		b.setSize(d);
		b.setLocation(p.x - b.getWidth()/2,p.y);
		b.setBackground(bg);
		b.setForeground(tc);
		b.setVisible(visible);
		return b;
	}

	public void initSelectLessonButton(){
		selectLesson=initButton(selectLesson,"<html><h3 style='color:red;'>SELECT LESSON</h3></html>",Color.white,Color.red,largeButton,new Point(this.getWidth()/2,40 + headingHeight),true);
		bgPanel.add(selectLesson);
	}

}
