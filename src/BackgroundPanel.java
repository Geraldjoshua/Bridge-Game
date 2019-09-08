import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BackgroundPanel extends JPanel{
		
	private JLabel bg;

	BackgroundPanel(File file)throws IOException{
		this.setLayout(null);
        	this.bg = new JLabel(new ImageIcon(ImageIO.read(file)));
		bg.setSize(bg.getPreferredSize());
		this.setSize(bg.getSize());
		
	}

	public void addBackGround(){
		this.add(bg);
	}
}
