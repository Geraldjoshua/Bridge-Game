import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class GUI{
	
	private Lesson lesson;
	private final int framePadding = 40;
	private JFrame window;
	private JFrame window2 = new JFrame();
	


	GUI(Lesson lesson) throws IOException{
		
		this.lesson = lesson;
		this.window = new JFrame("Bridge Tutor");
		window2.setSize(200,200);
	}

	public void makeLessonScreen() throws IOException{
	
		
		Image img = ImageIO.read(new File("icons/exitIcon.png"));
    		Image newimg = img.getScaledInstance( 100, 100,  Image.SCALE_SMOOTH ) ; 
		

		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			/*A way to get the screens size will be used later
			/*For UI components
			*/
		Toolkit tk = Toolkit.getDefaultToolkit();
		int xSize = ((int) tk.getScreenSize().getWidth());
		int ySize = ((int) tk.getScreenSize().getHeight());
		//window.setSize(xSize,ySize);


		ArrayList<JLabel> cardLabels = new ArrayList<JLabel>();
		JPanel[] panels = new JPanel[4];
		for(int i=0;i<panels.length;i++){
		    panels[i] = new JPanel();
		    panels[i].setOpaque(false);
		    panels[i].setLayout(null);
		    if(i%2 == 0) {

		        panels[i].setPreferredSize(new Dimension(xSize,200));
		    } else{
		        panels[i].setPreferredSize(new Dimension(400, ySize));
		    }

		}

		for(int i=0;i<panels.length;i++){
		    for(int j=0;j<13;j++){
		        final JLabel cardLabel = new JLabel(new ImageIcon(lesson.getPlayers().get(i).getCard(j).getCardImage()));
		        cardLabel.setSize(72,96);
		        cardLabel.addMouseListener(new MouseAdapter() {

		            @Override
		            public void mouseEntered(MouseEvent evt) {
		                Point pt = cardLabel.getLocation();
		                int x = pt.x;
		                int y = pt.y;
		                cardLabel.setLocation(x,y-20);
				cardLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		            }

		            @Override
		            public void mouseExited(MouseEvent evt) {
		                Point pt = cardLabel.getLocation();
		                int x = pt.x;
		                int y = pt.y;
		                cardLabel.setLocation(x,y+20);
		            }


		        });
		        cardLabels.add(cardLabel);
		    }
		    double panelWidth = panels[i].getPreferredSize().getWidth();
		    System.out.println(panelWidth);
		    int xDim;
		    int startMargin=60;
		    int marginX;
		    if(i%2 == 0){
		        startMargin=(int)panelWidth/2 - (50*13)/2 - 50;
		        xDim = 50;
		    }else{
		        xDim=40;
		    }
		    marginX=startMargin;
		    System.out.println(marginX);
		    int marginY = 30;
		    for(JLabel j:cardLabels){
		        panels[i].add(j);
		        j.setLocation(marginX,marginY);
		        marginX+=xDim;
		        if(marginX+120>=panelWidth){

		            marginX=startMargin;
		            marginY+=130;

		        }
		    }
		    cardLabels.clear();
		}

		JPanel centerPanel = new JPanel();

		centerPanel.setLayout(null);
		centerPanel.setPreferredSize(new Dimension(600,600));
		centerPanel.setMinimumSize(new Dimension(600, 600));
		centerPanel.setOpaque(false);
		centerPanel.setBorder(BorderFactory.createLineBorder(new Color(226,172,44),2));
		JButton exit = makeExitButton();
		window.add(exit);
		exit.setLocation(40,40);
		window.add(panels[2], BorderLayout.NORTH);
		window.add(panels[3], BorderLayout.WEST);
		window.add(centerPanel, BorderLayout.CENTER);
		window.add(panels[1], BorderLayout.EAST);
		window.add(panels[0], BorderLayout.SOUTH);
		
		window.pack();
		window.setLocationRelativeTo(null);

		window.getContentPane().setBackground(new Color(0, 134, 64));

		//window.repaint();
		window.setVisible(true);		

	}

	private JButton makeExitButton(){
		
		JButton exit = new JButton( new AbstractAction("EXIT") {
			@Override
			public void actionPerformed( ActionEvent e ) {
				window.setVisible(false);
				window2.setVisible(true);	
			}
		});
		exit.setText("<html><h1>EXIT</h1></html>");
		exit.setOpaque(false);
		exit.setContentAreaFilled(false);
		exit.setLayout(null);
		exit.setSize(exit.getPreferredSize());
		exit.setBorder(BorderFactory.createLineBorder(new Color(226,172,44),2));
		exit.setForeground(new Color(226,172,44));	
		exit.setFocusPainted(false);
			
		return exit;
	}

	


}
