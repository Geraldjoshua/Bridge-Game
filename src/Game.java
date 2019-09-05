
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Driver class used to run the game(Bridge game).
 * @author Chris Cushway
 * @author Gerald Ngumbulu
 * @author Blessed Chitamba
 * @version 1.0
 */

public class Game{
    private JFrame menuScreen;
    //private JButton freePlay,playLessons,getHelp;
    private static final int framePadding = 75;
    private int xSize,ySize;
    private ArrayList<JButton> lessonButtons = new ArrayList<JButton>(),optionButtons = new ArrayList<JButton>();
    private MouseListener lessonML;
    private JLabel heading;
    private int clicks = 1;
    private int helpLevel = 1;

    Game(){
        initGUIComponents();
        Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(menuScreen.getGraphicsConfiguration());
        this.xSize = Toolkit.getDefaultToolkit().getScreenSize().width-scnMax.right - scnMax.left;
        this.ySize = Toolkit.getDefaultToolkit().getScreenSize().height-scnMax.bottom-scnMax.top;
    }

    private void initGUIComponents(){
        menuScreen=new JFrame();
        initFrame(menuScreen);
        initLessonListeners();
    }

    private void loadMenuScreenComponents() throws IOException {
        JLabel bg = new JLabel(new ImageIcon(ImageIO.read(new File("images/background2.jpg"))));
        initLabel(bg,bg.getPreferredSize(),0,0);
        bg.setSize(bg.getPreferredSize());
        menuScreen.setSize(bg.getSize());
        menuScreen.setLocation(xSize/2 - menuScreen.getWidth()/2,ySize/2 -menuScreen.getHeight()/2);
        //menuScreen.add(bg);
        final JButton exit = new JButton( new AbstractAction("EXIT") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                System.exit(0);
            }
        });
        initButton(exit,exit.getPreferredSize(),Color.white,Color.white,"<html><span style='font-size:150%;font-weight:bold;'>&#10005;</span></html>");
        exit.setContentAreaFilled(false);
        exit.setBorderPainted(false);
        exit.setLocation(menuScreen.getWidth()-exit.getWidth()-20,20);
        JPanel bgPanel =new JPanel(null);
        bgPanel.setSize(bg.getSize());
        menuScreen.add(exit);
        heading = new JLabel("<html><h1 style='color:white;font-weight:bold;'>Bridge Tutor v 1.0</h1></html>");
        heading.setLayout(null);
        heading.setSize(heading.getPreferredSize());
        initLabel(heading,heading.getPreferredSize(),menuScreen.getWidth()/2 - heading.getWidth()/2,framePadding);
        bgPanel.add(bg);
        generateLessonButtons();
        menuScreen.add(heading);
        JButton backButton = new JButton(new AbstractAction("BACK") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                hideMenuButtons(true);
                showLessonButtons(false);
            }
        });
        initButton(backButton,backButton.getPreferredSize(),Color.white,Color.white,"<html><span style='font-size:300%;font-weight:bold;'>&larr;</span></html>");
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setLocation(20,20);
        menuScreen.add(backButton);
        backButton.setVisible(false);
        lessonButtons.add(backButton);
        JButton selectLesson = new JButton(new AbstractAction("EXIT") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                hideMenuButtons(false);
                showLessonButtons(true);
            }
        });
        optionButtons.add(selectLesson);
        JButton toggleHelp = new JButton(new AbstractAction("TOGGLE") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                toggle((JButton)e.getSource());
            }
        });
        optionButtons.add(toggleHelp);
        initButton(selectLesson,heading.getSize(),Color.red,Color.white,"<html><h3 style='color:red;'>SELECT LESSON</h3></html>");
        initButton(toggleHelp,heading.getSize(),Color.blue,Color.white,"<html><h3>HELP LEVEL:1</h3></html>");
        menuScreen.setBackground(null);
        menuScreen.add(selectLesson);
        menuScreen.add(toggleHelp);
        selectLesson.setLocation(menuScreen.getWidth()/2-selectLesson.getWidth()/2,heading.getHeight()+100);
        toggleHelp.setLocation(selectLesson.getLocation().x,selectLesson.getLocation().y + 20 + selectLesson.getHeight());
        menuScreen.getContentPane().add(bgPanel);
        menuScreen.setVisible(true);
    }

    public void initLabel(JLabel l,Dimension d,int xCoord, int yCoord){
        l.setLayout(null);
        l.setSize(d);
        l.setLocation(xCoord,yCoord);
    }

    public void initButton(JButton b,Dimension d,Color text,Color background,String stringText){
        b.setLayout(null);
        b.setText(stringText);
        b.setBackground(background);
        b.setForeground(text);
        b.setSize(d);
        b.setFocusPainted(false);
    }

    //Method that will determine how much to space the buttons out by
    public Dimension calcFreeSpace(Dimension d,int numButtons,int padding,JFrame frame){
        double remainingSpaceX=(frame.getWidth()-padding)/d.getWidth();
        double remainingSpaceY=(frame.getHeight()-padding)/d.getHeight();
        int rows = (int)remainingSpaceY;
        if(numButtons%rows !=0){

        }
        return new Dimension(60,60);
    }

    public void toggle(JButton b){

        clicks++;
        if(clicks>5){
            clicks=0;
        }
        helpLevel=clicks;
        if(clicks==0){
            initButton(b,b.getSize(),new Color(59, 223, 255),Color.white,"<html><h3>NO HELP</h3></html>");
        }else{
            initButton(b,b.getSize(),new Color(59, 223, 255),Color.white,"<html><h3>HELP LEVEL:"+clicks+"</h3></html>");
        }

    }

    private void initLessonListeners(){
        this.lessonML = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    findLesson(e);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        };
    }

    public void hideMenuButtons(boolean visible){
        heading.setVisible(false);
        for(JButton b:optionButtons){
            b.setVisible(visible);
        }
    }

    public void generateLessonButtons(){
        int y = heading.getHeight() + framePadding + 50;
        int x = framePadding;
        for(int i=1;i<new File("input").listFiles().length;i++){
            JButton j= new JButton(i+"");
            j.addMouseListener(lessonML);
            initButton(j,new Dimension((int)(j.getPreferredSize().getWidth()+50),(int)(j.getPreferredSize().getHeight()+30)),Color.red,Color.white,i+"");
            j.setLocation(x,y);
            x += (int) ((j.getWidth()) + 50);
            if(x>menuScreen.getWidth()-framePadding- i*50){
                x=framePadding;
                y+=10+j.getHeight();
            }
            j.setVisible(false);
            lessonButtons.add(j);
            menuScreen.add(j);
        }
    }

    public void findLesson(MouseEvent e) throws IOException, InterruptedException {
        int index=1;
        for(JButton b:lessonButtons){
            if(b==e.getSource()){
                Lesson lesson = new Lesson("input/input"+index+".txt",helpLevel);
                GUI g = new GUI(lesson);
                g.makeLessonScreen();
                g.start();
            }
            index++;
        }

    }

    public void showLessonButtons(boolean visible){
        for(int i=0;i<lessonButtons.size();i++){
            lessonButtons.get(i).setVisible(visible);
        }
    }

    public void initWelcomeScreen() throws IOException, InterruptedException {
        JFrame welcomeScreen = new JFrame();
        JPanel bgPanel =new JPanel(null);
        bgPanel.setOpaque(false);
        JLabel bg = new JLabel(new ImageIcon(ImageIO.read(new File("images/background.jpg"))));
        initLabel(bg,bg.getPreferredSize(),0,0);
        JLabel wm = new JLabel("<html><span style='color:white;font-size:120%;'>Welcome To</span><h1 style='color:white;font-size:150%;margin-top:0px;'>Bridge Tutor v 1.0</h1></html>");
        initLabel(wm,wm.getPreferredSize(),0,0);
        wm.setOpaque(false);wm.setSize(wm.getPreferredSize());
        bg.setSize(bg.getPreferredSize());
        bgPanel.setSize(bg.getSize());
        initFrame(welcomeScreen);
        welcomeScreen.setSize(bg.getSize());
        wm.setLocation(welcomeScreen.getWidth()/2 - wm.getWidth()/2, welcomeScreen.getHeight()/2-wm.getHeight()/2);
        welcomeScreen.setLocation(xSize/2 - welcomeScreen.getWidth()/2,ySize/2 - welcomeScreen.getHeight()/2);
        JProgressBar progressBar = new JProgressBar(SwingConstants.HORIZONTAL,0,wm.getWidth());

        progressBar.setBackground(null);progressBar.setForeground(Color.WHITE);progressBar.setBorderPainted(false);progressBar.setBorder(null);
        progressBar.setLayout(null);progressBar.setSize(wm.getWidth(),3);
        progressBar.setValue(1);progressBar.setLocation(wm.getLocation().x,wm.getLocation().y+wm.getHeight()+5);

        bgPanel.add(bg);
        welcomeScreen.add(wm);
        welcomeScreen.add(bgPanel);
        welcomeScreen.add(progressBar);

        welcomeScreen.setVisible(true);
        loadBar(progressBar,wm.getWidth());
        welcomeScreen.dispose();
        loadMenuScreenComponents();
    }

    public void loadBar(JProgressBar b,int end) throws InterruptedException {
        Thread.sleep(1200);
        for(int i=0;i<end;i++){
            Thread.sleep(10);
            if(i>end/2){
                i++;
            }else if(i>end*3/4){
                i+=2;
            }
            b.setValue(i);
        }

    }

    public void initFrame(JFrame frame){
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setUndecorated(true);
    }

    public static void main(String args[]) throws IOException, InterruptedException{

        /*Scanner userinput = new Scanner(System.in);
        System.out.println("Enter level: ");
        String level = userinput.nextLine();
        String lesson_file = "input/input"+level+".txt";
        Lesson lesson = new Lesson(lesson_file);*/


        Game g = new Game();
        g.initWelcomeScreen();
        g.loadMenuScreenComponents();
        //game.menuScreen.setVisible(true);
        // ArrayList<String> copyBestCase = lesson.getBestCase();
        // ArrayList<String> cardsPlayed = new ArrayList<String>();



        //Setting up gui for game

        // GUI gui = new GUI(lesson);
        // gui.makeLessonScreen();

        //gui.startGame();

       /* //-----FIRST PLAY OF THE GAME----------------------------------------------------------------------//
        System.out.println(lesson.getPlayers().get(playerTurn).getPlayerName() + " is now playing.");
        System.out.println(lesson.getPlayers().get(playerTurn).getPlayerName()+" played: "+lesson.getFirstCardPlayed());
        System.out.println(lesson.getPlayers().get(playerTurn).getPlayerName() + " completed their turn."+"\n");
        copyBestCase.remove(0);
        lesson.isValid(lesson.getFirstCardPlayed(),lesson.getPlayers().get(playerTurn));
        cardsPlayed.add(lesson.getFirstCardPlayed());
        playerTurn++;
        //-----FIRST PLAY OF THE GAME END----------------------------------------------------------------------//


        //---------------------------------------------------------THE ACTUAL GAME-----------------------------------------------------///

        //Variables to be tracked
        int tricks = 0;			//tricks in the game (max is 13)
        boolean trickWinner = false;
        boolean claim = false;		//If a person plays claim

        while(tricks < 13){ //max tricks is 13 unless claim played beforehand
            if(tricks==0){
                copyBestCase = lesson.getBestCase1(tricks);
                copyBestCase.remove(0);
            }
            else{
                copyBestCase = lesson.getBestCase1(tricks);
            }
            while(playerTurn<4){
                System.out.print("Cards played: ");
                for(String card:cardsPlayed){
                    System.out.print(card+" ");
                }
                System.out.print("\n\n");
                System.out.println(lesson.getPlayers().get(playerTurn).getPlayerName() + " is now playing.");
                System.out.println(lesson.getPlayers().get(playerTurn).getPlayerName() + " Please play a card by typing the number and then the suit e.g 6D \n");
                lesson.getPlayers().get(playerTurn).printNiceHand();
                System.out.println("\n");
                System.out.println("------------------------------------------------------------------");

                String card = userinput.nextLine();
                //If not the first turn
                if(playerTurn>0){

                    while(!lesson.isValid(card,lesson.getPlayers().get(playerTurn))){
                        System.out.println("That card is not a valid play please play another card");
                        card = userinput.nextLine();
                    }
                    //else its the first turn we need to set the suit
                }else{
                    lesson.setFirstCardPlayed(card);
                    while(!lesson.isValid(card,lesson.getPlayers().get(playerTurn))){

                        System.out.println("That card is not a valid play please play another card");
                        card = userinput.nextLine();
                    }


                }
                lesson.getPlayers().get(playerTurn).removePlayedCard(card);
                if(!card.equals(copyBestCase.get(0))){
                    String bestPlay = lesson.getPlayers().get(playerTurn).bestCaseInHand(copyBestCase);
                    System.out.println("The card that should have been played was: "+bestPlay);

                }
                //Add the points of the play to the player i.e playing an ACE adds 14 points to player
                lesson.getPlayers().get(playerTurn).addPoints(lesson.getPlayPoints(card));
                cardsPlayed.add(card);

                //Only let the person claim if it is in fact a stage in the game where claiming will actually win them the game
                if(card.equals("CLAIM") && copyBestCase.equals("CLAIM")){
                    claim=true;
                    break;
                }
                copyBestCase.remove(0);
                playerTurn++;
            }

            //If they claimed
            if(claim){
                lesson.getPlayers().get(playerTurn).setTrickWins(13 - tricks + lesson.getPlayers().get(playerTurn).getTrickWins());
            }
            lesson.decideWinner();
            lesson.reorderPlayers();
            cardsPlayed.clear();
            playerTurn=0;
            tricks++;

        }

        lesson.decideGameWinner();
        */
    }
}

