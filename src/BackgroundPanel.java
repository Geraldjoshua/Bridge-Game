import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BackgroundPanel extends JPanel{
		
	private JLabel bg;

	/**
	 * <p>Constructor that initialises the object to be the same size as the image that is passed in as a parameter</p>
	 * @param file File
	 * @throws IOException
	 */
	BackgroundPanel(File file)throws IOException{
		this.setLayout(null);
		this.bg = new JLabel(new ImageIcon(ImageIO.read(file)));
		bg.setSize(bg.getPreferredSize());
		this.setSize(bg.getSize());
		
	}

	/**
	 * <p>Method adds the image to the object</p>
	 */
	public void addBackGround(){
		this.add(bg);
	}
}
