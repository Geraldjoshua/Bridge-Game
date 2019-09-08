import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 
 * class to generate background panels
 */
public class BackgroundPanel extends JPanel{
		
	private JLabel bg;

        /**
         * constructor to initialize the class
         * @param file that contains the background image icon
         * @throws IOException when image not found
         */
	BackgroundPanel(File file)throws IOException{
		this.setLayout(null);
        	this.bg = new JLabel(new ImageIcon(ImageIO.read(file)));
		bg.setSize(bg.getPreferredSize());
		this.setSize(bg.getSize());
		
	}

        /**
         * adds the background to the j-frame.
         */
	public void addBackGround(){
		this.add(bg);
	}
}
