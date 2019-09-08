import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameController {
	
	private LoadScreen ls;
	private MenuScreen ms;
	private MouseListener selectButtonListener,lessonButtonListener,toggleButtonListener,backButtonListener;	
	private int clicks = 0;
	GameController(LoadScreen ls,MenuScreen ms)throws InterruptedException{
		this.ls = ls;
		this.ms = ms;
		loadProgram();
		initListeners();
		ms.addSelectLessonListener(selectButtonListener);
		addLessonListeners(ms.getLessonButtons());
		ms.addToggleHelpLevel(toggleButtonListener);
		ms.addBackButtonListener(backButtonListener);
	}

	public void initListeners(){
		this.selectButtonListener = new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
				showButtons(false,ms.getMenuButtons());
				showButtons(true,ms.getLessonButtons());
				showBackButton(true,ms.getBackButton());
		    }
            
    	};
		this.backButtonListener = new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
				showButtons(true,ms.getMenuButtons());
				showButtons(false,ms.getLessonButtons());
				showBackButton(false,(JButton)e.getSource());
		    }
            
    	};

		this.lessonButtonListener = new MouseAdapter(){
		    @Override
		    public void mouseClicked(MouseEvent e){
				try{
					findLesson(e,ms.getLessonButtons());
				}catch(InterruptedException e1){
					e1.printStackTrace();
				}catch(IOException e2){
					e2.printStackTrace();
				}
				
		    }
			@Override
			public void mouseEntered(MouseEvent e){
				findButton(e,ms.getLessonButtons());
			}
            
    	};
		this.toggleButtonListener = new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
				toggleButton(e,ms.getMenuButtons());
		    }
            
    	};
	}

	public void loadProgram()throws InterruptedException{
		
		ls.setVisible(true);
		load(ls.getLoadBar(),ls.getLoadBar().getWidth());
		ls.dispose();		
		ms.setVisible(true);
		Thread.sleep(500);
		JOptionPane.showMessageDialog(ms,
                                    "<html><h3>Select the toggle help button to toggle the <br>help level for the lesson from No help - help level 5</h3></html>");
	}

	public void load(JProgressBar loadBar,int width)throws InterruptedException{
		
		Thread.sleep(1200);
        for(int i=0;i<width;i++){
			Thread.sleep(10);
			if(i>width/2){
				i++;
		    }else if(i>width*3/4){
				i+=2;
		    }
			    loadBar.setValue(i);
        }		

	}

	public void findLesson(MouseEvent e,ArrayList<JButton> buttons)throws IOException, InterruptedException{
		
		for(int i=0;i<buttons.size();i++){
			
			if(e.getSource() instanceof JButton && e.getSource() == buttons.get(i)){
				Lesson l = new Lesson("input/input"+(i+1)+".txt",clicks);
				GUI g = new GUI(l);
				g.makeLessonScreen();
				g.start();
			}
		}

	}

	public void toggleButton(MouseEvent e,ArrayList<JButton> buttons){
		clicks++;		
		for(int i=0;i<buttons.size();i++){
			
			if(e.getSource() instanceof JButton && e.getSource() == buttons.get(i)){
				if(clicks>5){
						clicks=0;
						buttons.get(i).setText("<html><h3 style='color:red;'>NO HELP</h3></html>");
					}else if(clicks>0){
						buttons.get(i).setText("<html><h3 style='color:red;'>HELP LEVEL: "+clicks+"</h3></html>");					
					}
			}
		}
	}

	public void findButton(MouseEvent e,ArrayList<JButton> buttons){
		
		for(int i=0;i<buttons.size();i++){
			
			if(e.getSource() instanceof JButton && e.getSource() == buttons.get(i)){
				 handleToolTip(buttons.get(i),i);
			}
		}

	}

	public void handleToolTip(JButton b,int index){
		b.setToolTipText("<html><h4>This is a lesson for scenario "+(index+1)+"</h4></html>");
	}
	public void addLessonListeners(ArrayList<JButton> buttons){
		for(int i=0;i<buttons.size();i++){
			ms.addLessonListener(lessonButtonListener,i);
		}
	}

	public void showButtons(boolean visible,ArrayList<JButton> buttons){
		for(int i=0;i<buttons.size();i++){
			buttons.get(i).setVisible(visible);
		}
	}
	
	public void showBackButton(boolean visible,JButton b){
		b.setVisible(visible);
	}

}
