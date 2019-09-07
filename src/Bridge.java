import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Component;

public class Bridge{

	public static void main(String[] args) throws IOException  {
		//Block of code to get screen size of the screen - toolbars size
		//Toolbar position differs for different OS 
		//position of toolbar can also be set to users preference on different OS
		JFrame window = new JFrame();
		Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(window.getGraphicsConfiguration());
        	int xSize = Toolkit.getDefaultToolkit().getScreenSize().width-scnMax.right - scnMax.left; //Account for all possible toolbar positions
        	int ySize = Toolkit.getDefaultToolkit().getScreenSize().height-scnMax.bottom-scnMax.top;
			
		LoadScreen ls = new LoadScreen(xSize,ySize);
		ls.setVisible(true);
		
	}

}
