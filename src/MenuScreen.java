
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuScreen extends JFrame{
	
	private final int xSize,ySize;
	private BackgroundPanel bgPanel;
	private JButton exitButton;

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

}
