import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 
 * class to display the game screen
 */

public class GameScreen extends JFrame{

	private Lesson lesson;
	private BackgroundPanel bgPanel;
	private final int xSize,ySize;
	private JPanel[] panels = new JPanel[4];
	private ArrayList<Component> components = new ArrayList<Component>();
	
        /**
         * constructor
         * @param xSize x size of the game screen
         * @param ySize y size of the game screen
         * @param lesson lesson plan to display
         * @throws IOException input output exception
         */
	GameScreen(int xSize,int ySize,Lesson lesson)throws IOException{
		this.xSize = xSize;
		this.ySize = ySize;
		this.lesson = lesson;
		this.bgPanel = new BackgroundPanel(new File("images/background3.jpg"));
		bgPanel.addBackGround();
		this.setUndecorated(true);
		this.setSize(xSize,ySize);
		makeAndAddComponents();
		this.add(bgPanel);
		
	}

        /**
         * makes and add components
         */
	public void makeAndAddComponents(){
		makePanels();
		for(Component c:components){
			this.add(c);
		}
	}

        /**
         * makes the panels
         */
	public void makePanels(){
		for(int i=0;i<panels.length;i++){
            panels[i] = new JPanel();
            panels[i].setOpaque(false);
            panels[i].setLayout(null);
            components.add(panels[i]);
        }

        panels[0].setBounds(0,(int)(ySize/4),(int)(xSize/4),(int)(ySize/2));
        panels[1].setBounds(0,0,xSize,(int)(ySize/4));
        panels[2].setBounds((int)((3*xSize)/4),(int)(ySize/4),(int)(xSize/4),(int)(ySize/2));
        panels[3].setBounds(0,(int)((3*ySize)/4),xSize,(int)(ySize/4));
	}
	
}
