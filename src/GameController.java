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
	private MouseListener selectListener;
	//private Lesson lesson;	
	
	GameController(LoadScreen ls,MenuScreen ms)throws InterruptedException{
		this.ls = ls;
		this.ms = ms;
		loadProgram();
		initListeners();
		ms.addSelectLessonListener(selectListener);
		//this.leson = lesson;
	}

	public void initListeners(){
		this.selectListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showButtons(false,ms.getMenuButtons());
				showButtons(true,ms.getLessonButtons());
            }
            
        };
	}

	public void loadProgram()throws InterruptedException{
		
		ls.setVisible(true);
		load(ls.getLoadBar(),ls.getLoadBar().getWidth());
		ls.dispose();		
		ms.setVisible(true);
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

	public void showButtons(boolean visible,ArrayList<JButton> buttons){
		for(int i=0;i<buttons.size();i++){
			buttons.get(i).setVisible(visible);
		}
	}

}
