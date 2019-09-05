import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Component;

public class GUI extends java.lang.Thread{

    private Lesson lesson;
    private final int framePadding = 40,ySpaceing=15;
    static JFrame window;
    private ArrayList<JLabel> cardLabels = new ArrayList<JLabel>(),cards=new ArrayList<JLabel>();
    private JPanel[] panels = new JPanel[4];
    private final int xSize,ySize;
    private BufferedImage bgPicture;
    private JPanel centerPanel;
    private int currentPlayer,play,tricks,hintQuestionNumber,timesTipClicked = 0,timesClicked = 0,count = 0;
    private JPanel playLog;
    private JLabel playerTurn,score,playLogHeader,biddingLabel;
    private JButton tips,hints,exit,claim;
    private boolean played;
    private ArrayList<String> copyBestCase,hintQuestions;
    private ArrayList<Component> components = new ArrayList<Component>();
    private MouseListener ml,hover;
    private JScrollPane scroller; 
    private boolean doneHints=false;
	
    /**
     * <p> constructor </p>
     * @param lesson
     * @throws IOException
     * @throws InterruptedException 
     */

    GUI(Lesson lesson) throws IOException, InterruptedException {
        this.bgPicture=ImageIO.read(new File("images/background3.jpg"));
        this.played=false;
        this.lesson=lesson;
        this.playLog = new JPanel();
        this.lesson = lesson;
        this.copyBestCase = lesson.getBestCase();
        this.currentPlayer = 0;
        this.tricks = 0;
        this.play = 0;
        this.copyBestCase = lesson.getBestCase();
        initComponents();
        Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(window.getGraphicsConfiguration());
        this.xSize = Toolkit.getDefaultToolkit().getScreenSize().width-scnMax.right - scnMax.left;
        this.ySize = Toolkit.getDefaultToolkit().getScreenSize().height-scnMax.bottom-scnMax.top;
        bgPicture = resize(bgPicture,xSize+100,ySize);
    }

    /**
     * <p> initializes components</p>
     */
    private void initComponents(){

        initFrame();
        initCardListeners();
        initHoverListener();
    }

    /**
     * <p> initializes card listeners
     */
    private void initCardListeners(){
        this.ml = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    makePlay((JLabel)e.getSource(),findPlayer((JLabel)e.getSource()),play);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                offSet((JLabel)e.getSource(),-20);
            }
            @Override
            public void mouseExited(MouseEvent e){
                offSet((JLabel)e.getSource(),20);
            }
        };
    }

    /**		
     * <p> initializes Hover on cards listeners</p>		
     */
    private void initHoverListener(){
        this.hover = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setHover((JButton)e.getSource());
                changeButtonColour(true,(JButton)e.getSource());
            }
            @Override
            public void mouseExited(MouseEvent e){
                changeButtonColour(false,(JButton)e.getSource());
            }
        };
    }

    /**		
     * <p> initializes the j-frame</p>		
     */
    private void initFrame(){
        window = new JFrame("Bridge Tutor");
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setLocationRelativeTo(null);
        window.setLayout(null);
        window.setUndecorated(true);

        this.centerPanel = new JPanel();

    }


    /**		
     * <p> changes button color</p>		
     * @param entered		
     * @param source 		
     */
    public void changeButtonColour(boolean entered,JButton source){
        if(entered){
            source.setBackground(new Color(0, 134, 64));
            source.setForeground(new Color(226,172,44));
        }else{
            if(source.isOpaque()) {
                source.setBackground(new Color(226, 172, 44));
                source.setForeground(new Color(0, 134, 64));
            }
        }
    }

    /**		
     * <p> initializes the centrePanel</p>		
     */
    private void initCenterPanel(){
        centerPanel.setLayout(null);
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createLineBorder(new Color(226,172,44),5));
        centerPanel.setBounds((int)(xSize/4),(int)(ySize/4),(int)(xSize/2),(int)(ySize/2));
        components.add(centerPanel);
    }


    		
    /**		
     * <p> initializes playerPanels</p>		
     */
    private void initPlayerPanels(){
        for(int i=0;i<panels.length;i++){
            panels[i] = new JPanel();
            panels[i].setOpaque(false);
            panels[i].setLayout(null);
            components.add(panels[i]);
        }

        panels[0].setBackground(Color.blue);
        panels[1].setBackground(Color.green);
        panels[2].setBackground(Color.yellow);
        panels[3].setBackground(Color.red);

        panels[0].setBounds(0,(int)(ySize/4),(int)(xSize/4),(int)(ySize/2));
        panels[1].setBounds(0,0,xSize,(int)(ySize/4));
        panels[2].setBounds((int)((3*xSize)/4),(int)(ySize/4),(int)(xSize/4),(int)(ySize/2));
        panels[3].setBounds(0,(int)((3*ySize)/4),xSize,(int)(ySize/4));
    }

    /**		
     * <p> sets the hover option</p>		
     * @param source 		
     */
    public void setHover(JButton source){

        source.setCursor(new Cursor(Cursor.HAND_CURSOR));

    }

    /**		
     * <p> creates the lesson screen</p>		
     * @throws IOException		
     * @throws InterruptedException 		
     */

    public void makeLessonScreen() throws IOException, InterruptedException {

        initPlayerPanels();
        initCenterPanel();
        int x = testCardX(100,calcY(100));
        for(int i=0;i<panels.length;i++){
            for(int j=0;j<13;j++){

                if(!lesson.getPlayers().get(i).getPlayerName().toLowerCase().equals("south") && !lesson.getPlayers().get(i).getPlayerName().toLowerCase().equals("north")){
                    lesson.getPlayers().get(i).getCard(j).setFlipped(true);
                    lesson.getPlayers().get(i).getCard(j).resizeCard(x,(int)calcY(x),true);
                }else{
                    lesson.getPlayers().get(i).getCard(j).setFlipped(false);
                    lesson.getPlayers().get(i).getCard(j).resizeCard(x,(int)calcY(x),false);
                }

                cardLabels.add(lesson.getPlayers().get(i).getCard(j).getCardLabel());

                cards.add(lesson.getPlayers().get(i).getCard(j).getCardLabel());
            }
            int xCoord=framePadding;
            int yCoord=0;
            if(i%2 !=0) {
                xCoord = panels[i].getWidth() / 2 - ((13 / 2) * x)/2 - x/2;
                yCoord=framePadding;
            }
            for(JLabel j:cardLabels){
                panels[i].add(j);

                if(xCoord+j.getWidth()+framePadding>=panels[i].getWidth()){
                    xCoord = framePadding;
                    yCoord+=j.getHeight()+15;
                }
                j.setLocation(xCoord,yCoord);
                xCoord+=j.getWidth()/2;
            }
            cardLabels.clear();
        }

        components.add(makeScore());components.add(makeExitButton());components.add(makeFlipCards());
        components.add(makeGetTipsButton());components.add(makeGetHintsButton());
        components.add(makePlayLog());components.add(makeClaim());components.add(makeBiddingLabel());
        
        for(Component c:components){
            if(c instanceof JButton){
                c.addMouseListener(hover);
                ((JButton) c).setBorder(BorderFactory.createLineBorder(new Color(226,172,44),2));
            }
            window.add(c);
        }
        JLabel bg = new JLabel(new ImageIcon(bgPicture));
        bg.setLayout(null);
        bg.setSize(bg.getPreferredSize());
        JPanel bgPanel = new JPanel(null);
        bgPanel.setSize(bg.getPreferredSize());
        bgPanel.setOpaque(false);
        bgPanel.setSize(xSize,ySize);
        bgPanel.add(bg);
        window.setBackground(null);
        window.getContentPane().add(bgPanel);
        window.setVisible(true);

    }



    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }

    public JFrame getWindow(){
        return this.window;
    }
    public void run(){

        try {
            startGame();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

     /**		
     * <p> starts the game</p>		
     * @throws InterruptedException 		
     */
    public void startGame() throws InterruptedException {

        for(;tricks<13;tricks++) {
            for (play = 0; play < 4; play++) {
                played = false;
                
                lesson.getPlayers().get(currentPlayer).setCanPlay(true);
                playerTurn.setText("<html><h3 style='color:white;'>"+lesson.getPlayers().get(currentPlayer).getPlayerName()+" is playing...</h1></html>");

               
                if (!lesson.getPlayers().get(currentPlayer).getPlayerName().toLowerCase().equals("south") && !lesson.getPlayers().get(currentPlayer).getPlayerName().toLowerCase().equals("north")) {
                    //Thread.sleep(2000);
                    autoPlay();
                } else {
                    addMouseListeners();
                    while (!played) {
                        Thread.sleep(1);
                    }
                    removeMouseListeners();
                }

                if(!copyBestCase.isEmpty() && !copyBestCase.get(0).equals("CLAIM")){
                    copyBestCase.remove(0);
                }

                lesson.getPlayers().get(currentPlayer).setCanPlay(false);
             
              

                currentPlayer++;
                if (currentPlayer > 3) {
                    currentPlayer = 0;
                }

            }

            lesson.setWinner(lesson.decideWinner());
            if(lesson.decideWinner().toLowerCase().equals("west")){
                currentPlayer=0;
            }else if(lesson.decideWinner().toLowerCase().equals("north")){
                currentPlayer=1;
            }else if(lesson.decideWinner().toLowerCase().equals("east")){
                currentPlayer=2;
            }else{
                currentPlayer=3;
            }
            playerTurn.setText("<html><h3>" + lesson.decideWinner() + " wins the trick</h3></html>");
            lesson.resetPoints();
            removeCenterCards();
            updateScoreBoard();
        }
        JOptionPane.showMessageDialog(window,
                                    "<html><h1>"+lesson.decideGameWinner()+" won</h1></html>");
		window.dispose();
    }

    			
    /**		
     * <p> searching for the player currently playing</p>		
     * @param source		
     * @return integer		
     * @throws InterruptedException 		
     */
    public int findPlayer(JLabel source) throws InterruptedException {

        Container parent = source.getParent();
        for(int i=0;i<panels.length;i++){
            if(panels[i] == parent){
                return i;
            }
        }
        return -1;
    }


    /**		
     * <p> handles the playing of cards</p>		
     * @param source		
     * @param index		
     * @param play		
     * @throws InterruptedException 		
     */
    public void makePlay(JLabel source,int index,int play) throws InterruptedException {
        int jIndex=0;
        if(lesson.getPlayers().get(index).getCanPlay()){
            for(Component card:panels[index].getComponents()){
                if(source == card){
                    if(lesson.getHelpLevel()==1){
                        if(lesson.isValid(lesson.getPlayers().get(index).getCard(jIndex).toString(),lesson.getPlayers().get(currentPlayer))){
                            playCard( lesson.getPlayers().get(index).getCard(jIndex),(JLabel)card,index);
                            moveCard((JLabel)card,currentPlayer);
                        }else{
                            JOptionPane.showMessageDialog(window,
                                    "That is not a valid move please choose another card");
                            break;
                        }
                    }else if(lesson.getHelpLevel()==2){
                        if(lesson.isValid(lesson.getPlayers().get(index).getCard(jIndex).toString(),lesson.getPlayers().get(currentPlayer)) && play==0){
                            playCard(lesson.getPlayers().get(index).getCard(jIndex),(JLabel)card,index);
                            moveCard((JLabel)card,currentPlayer);
                            break;
                        }else{
							playCard(lesson.getPlayers().get(index).getCard(jIndex),(JLabel)card,index);
                            moveCard((JLabel)card,currentPlayer);
							break;
						}
                    }else if(lesson.getHelpLevel()==3){
                        if(lesson.isValid(lesson.getPlayers().get(index).getCard(jIndex).toString(),lesson.getPlayers().get(currentPlayer)) && lesson.getPlayers().get(currentPlayer).getCard(jIndex).toString().equals(copyBestCase.get(0))) {
                            playCard(lesson.getPlayers().get(index).getCard(jIndex), (JLabel) card, index);
                            moveCard((JLabel) card, currentPlayer);
                            break;
                        }else if(copyBestCase.get(0).equals("CLAIM")){
                            playCard(lesson.getPlayers().get(index).getCard(jIndex), (JLabel) card, index);
                            moveCard((JLabel) card, currentPlayer);
                            JOptionPane.showMessageDialog(window,
                                "You should claim now");
                        }else{
                            JOptionPane.showMessageDialog(window,
                                    "That is not card you should play");
                        }
                    }else if(lesson.getHelpLevel()==4){
                        System.out.println(lesson.isValid(lesson.getPlayers().get(index).getCard(jIndex).toString(),lesson.getPlayers().get(currentPlayer))+" " + lesson.getPlayers().get(currentPlayer).getCard(jIndex).toString()+" "+copyBestCase.get(0));
                        if((lesson.isValid(lesson.getPlayers().get(index).getCard(jIndex).toString(),lesson.getPlayers().get(currentPlayer)) || play==0) && lesson.getPlayers().get(currentPlayer).getCard(jIndex).toString().equals(copyBestCase.get(0))){
                            playCard( lesson.getPlayers().get(index).getCard(jIndex),(JLabel)card,index);
                            moveCard((JLabel)card,currentPlayer);
                            break;
                        }else if(copyBestCase.get(0).equals("CLAIM")) {
                            playCard(lesson.getPlayers().get(index).getCard(jIndex), (JLabel) card, index);
                            moveCard((JLabel) card, currentPlayer);
                            JOptionPane.showMessageDialog(window,
                                    "You should claim now");
                        }else{
                            JOptionPane.showMessageDialog(window,
                                    "That is not card you should play");
                        }
                    }else if(lesson.getHelpLevel()==5){
                        if((lesson.isValid(lesson.getPlayers().get(index).getCard(jIndex).toString(),lesson.getPlayers().get(currentPlayer)) || play==0)&& lesson.getPlayers().get(currentPlayer).getCard(jIndex).toString().equals(copyBestCase.get(0))){
                            playCard( lesson.getPlayers().get(index).getCard(jIndex),(JLabel)card,index);
                            moveCard((JLabel)card,currentPlayer);
                            break;
                        }else if(copyBestCase.get(0).equals("CLAIM")){
                            playCard(lesson.getPlayers().get(index).getCard(jIndex), (JLabel) card, index);
                            moveCard((JLabel) card, currentPlayer);
                            JOptionPane.showMessageDialog(window,
                                "You should claim now");
                        }
                    }else{
                        if(lesson.isValid(lesson.getPlayers().get(index).getCard(jIndex).toString(),lesson.getPlayers().get(currentPlayer))){
                            playCard( lesson.getPlayers().get(index).getCard(jIndex),(JLabel)card,index);
                            moveCard((JLabel)card,currentPlayer);
                        }
                    }


                }
                jIndex++;
            }


        }
    }


    public void playCard(Card c,JLabel card,int playerIndex){
        c.setFlipped(false);
        card.setIcon(c.getImageIcon());
        card.validate();
        card.repaint();
        if(play ==0){
            lesson.setFirstCardPlayed(c.toString());
        }
        lesson.getPlayers().get(playerIndex).addPoints(lesson.getPlayPoints(c.toString()));
        lesson.getPlayers().get(playerIndex).removePlayedCard(c.toString());
        played=true;

    }

    /**		
     * <p> handles the placement of the card on the panel</p>		
     * @param testX		
     * @param testY		
     * @return integer		
     */

    public int testCardX(double testX,double testY){

        int panelHeight = panels[0].getHeight()-2*ySpaceing;
        int panelWidth = panels[0].getWidth()-2*framePadding;
	
        testX=Math.ceil(testX/2);
        
        int cardsX = (int)(Math.floor(panelWidth/testX));
	
        int cardsY = (int)(Math.floor((panelHeight/testY)));

        cardsY = (int)(Math.floor(((panelHeight - ySpaceing*cardsY)/testY)));
	System.out.println("x: "+testX+" y: "+testY+" cardX "+cardsX+" cardsY "+cardsY);
        int area = cardsX*cardsY;
        if(area<13){
            testX*=2;
            testX-=10;
            System.out.println(panelWidth+"x"+panelHeight+" "+"testx -10: " +testX+" calc: "+calcY(testX)+"cardsX :"+cardsX+" cardsY: "+cardsY+"area is: "+area);
            return testCardX(testX,calcY(testX));
        
        }else{
            System.out.println("area "+area+"new x dim is: " + (int)testX);
            return (int)testX*2;
        }

    }

    /**		
     * <p> used to resize cards to fit on the screen</p>		
     * @param x		
     * @return double		
     */
    public double calcY(double x){
        x=Math.floor(((x/363)*543));
        return x;
    }

    /**		
     * <p> handles automatic play for west and east hand</p>		
     * @throws InterruptedException 		
     */
    public void autoPlay() throws InterruptedException {

        boolean noValid=false;
        int i=0;

        for(Component c:panels[currentPlayer].getComponents()){
            if (!copyBestCase.isEmpty() && (play == 0 && lesson.getPlayers().get(currentPlayer).getCard(copyBestCase.get(0)) != null )) {
                lesson.setFirstCardPlayed(copyBestCase.get(0));
            }
            if(panels[currentPlayer].getComponent(i) instanceof JLabel) {

                if (!copyBestCase.isEmpty() && (lesson.getPlayers().get(currentPlayer).getCard(copyBestCase.get(0)) != null && lesson.isValid(lesson.getPlayers().get(currentPlayer).getCard(copyBestCase.get(0)).toString(), lesson.getPlayers().get(currentPlayer)))) {


                   // System.out.println("Best case is in our hand and valid to play and the card we are looking at is "+lesson.getPlayers().get(currentPlayer).getCard(i).getFlipped()+"and the actual card is"+ lesson.getPlayers().get(currentPlayer).getCard(copyBestCase.get(0)).getFlipped());
                    if(((JLabel)c) == lesson.getPlayers().get(currentPlayer).getCard(copyBestCase.get(0)).getCardLabel()){

                        noValid = false;
                        break;
                    }else{
                        noValid=true;
                    }

                } else if (lesson.isValid(lesson.getPlayers().get(currentPlayer).getCard(i).toString(), lesson.getPlayers().get(currentPlayer))) {
                    noValid = false;
                    break;

                } else {
                    noValid = true;
                }
            }
            i++;
        }
        if(noValid){
            lesson.getPlayers().get(currentPlayer).getCard(0).setFlipped(false);

            ((JLabel)panels[currentPlayer].getComponent(0)).setIcon(lesson.getPlayers().get(currentPlayer).getCard(0).getImageIcon());
            ((JLabel)panels[currentPlayer].getComponent(0)).validate();
            ((JLabel)panels[currentPlayer].getComponent(0)).repaint();
            moveCard((JLabel)panels[currentPlayer].getComponent(0),currentPlayer);
            lesson.getPlayers().get(currentPlayer).addPoints(lesson.getPlayPoints(lesson.getPlayers().get(currentPlayer).getCard(0).toString()));
            lesson.getPlayers().get(currentPlayer).removePlayedCard(lesson.getPlayers().get(currentPlayer).getCard(0).toString());
        }else{

            lesson.getPlayers().get(currentPlayer).getCard(i).setFlipped(false);

            ((JLabel)panels[currentPlayer].getComponent(i)).setIcon(lesson.getPlayers().get(currentPlayer).getCard(i).getImageIcon());
            ((JLabel)panels[currentPlayer].getComponent(i)).validate();
            ((JLabel)panels[currentPlayer].getComponent(i)).repaint();
            moveCard((JLabel)panels[currentPlayer].getComponent(i),currentPlayer);
            lesson.getPlayers().get(currentPlayer).addPoints(lesson.getPlayPoints(lesson.getPlayers().get(currentPlayer).getCard(i).toString()));
            lesson.getPlayers().get(currentPlayer).removePlayedCard(lesson.getPlayers().get(currentPlayer).getCard(i).toString());

        }


       
    }
    
    /**		
     * <p> handles score update</p>		
     */
    public void updateScoreBoard(){
        score.setText("<html><h1>N + S Score: "+(lesson.getPlayers().get(1).getTrickWins()+lesson.getPlayers().get(3).getTrickWins())+"</h1><h2>W + E Score: "+(lesson.getPlayers().get(0).getTrickWins()+lesson.getPlayers().get(2).getTrickWins())+"</h2></html>");
    }

    /**		
     * <p> adds mouse listeners on elements</p>		
     */

    public void addMouseListeners() {
        System.out.println("help level: "+lesson.getHelpLevel()+" copybest "+copyBestCase.get(0));
        for (int i = 0; i < panels[currentPlayer].getComponents().length; i++) {
            if (panels[currentPlayer].getComponent(i) instanceof JLabel) {
                if (lesson.getHelpLevel() == 0 || lesson.getHelpLevel() == 1 ) {
                    panels[currentPlayer].getComponent(i).addMouseListener(ml);
                    ((JLabel) panels[currentPlayer].getComponent(i)).setBorder(null);

                } else if (lesson.getHelpLevel() == 2 || lesson.getHelpLevel()==4) {
                    if (play == 0) {
                        panels[currentPlayer].getComponent(i).addMouseListener(ml);
                        ((JLabel) panels[currentPlayer].getComponent(i)).setBorder(BorderFactory.createLineBorder(new Color(226, 172, 44), 4));
                    } else {
                        if (lesson.isValid(lesson.getPlayers().get(currentPlayer).getCard(i).toString(), lesson.getPlayers().get(currentPlayer))) {
                            panels[currentPlayer].getComponent(i).addMouseListener(ml);
                            ((JLabel) panels[currentPlayer].getComponent(i)).setBorder(BorderFactory.createLineBorder(new Color(226, 172, 44), 4));
                        }
                    }

                }else if(lesson.getHelpLevel() == 3){
                    if (play == 0) {
                        panels[currentPlayer].getComponent(i).addMouseListener(ml);
                        ((JLabel) panels[currentPlayer].getComponent(i)).setBorder(BorderFactory.createLineBorder(null));
                    } else {
                        if (lesson.isValid(lesson.getPlayers().get(currentPlayer).getCard(i).toString(), lesson.getPlayers().get(currentPlayer))) {
                            panels[currentPlayer].getComponent(i).addMouseListener(ml);
                            ((JLabel) panels[currentPlayer].getComponent(i)).setBorder(null);
                        }
                    }
                }else if(lesson.getHelpLevel()==5){
                    if(copyBestCase.get(0).equals("CLAIM")){
                        panels[currentPlayer].getComponent(i).addMouseListener(ml);
                        ((JLabel) panels[currentPlayer].getComponent(i)).setBorder(BorderFactory.createLineBorder(new Color(226, 172, 44), 4));
                    }else{
                        if((lesson.isValid(lesson.getPlayers().get(currentPlayer).getCard(i).toString(), lesson.getPlayers().get(currentPlayer)) || play==0) && lesson.getPlayers().get(currentPlayer).getCard(i).toString().equals(copyBestCase.get(0))){
                            panels[currentPlayer].getComponent(i).addMouseListener(ml);
                            ((JLabel) panels[currentPlayer].getComponent(i)).setBorder(BorderFactory.createLineBorder(new Color(226, 172, 44), 4));
                        }
                    }
                }
            }
        }
    }

    /**		
     * <p> removes mouseListeners on elements</p>		
     */
    public void removeMouseListeners(){
        System.out.println(panels[currentPlayer].getComponents().length);
        for(int i = 0;i<panels[currentPlayer].getComponents().length;i++){
            if(panels[currentPlayer].getComponent(i) instanceof JLabel ){
                panels[currentPlayer].getComponent(i).removeMouseListener(ml);
                ((JLabel) panels[currentPlayer].getComponent(i)).setBorder(null);
            }
        }
    }
    
    /**		
     * <p> handles removing of cards from center panel</p>		
     * @throws InterruptedException 		
     */
    public void removeCenterCards() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("num comps "+centerPanel.getComponents().length);

        for(Component c:centerPanel.getComponents()){
            if(c instanceof JLabel ){
                centerPanel.remove(((JLabel)c));
                centerPanel.validate();
                centerPanel.repaint();
            }
        }
    }

    /**		
     * <p> handles moving of the card to the center</p>		
     * @param source		
     * @param index 		
     */
    public void moveCard(JLabel source,int index){
        source.setBorder(null);
        panels[index].remove(source);
        panels[index].validate();
        panels[index].repaint();
        centerPanel.add(source);
        if(index==1 || index==3){
            source.setLocation(centerPanel.getWidth()/2 - source.getWidth()/2,centerPanel.getHeight()/2 + (((index-2)*source.getWidth()/2)-source.getWidth()/2));
            source.removeMouseListener(ml);
        }else{
            source.setLocation(centerPanel.getWidth()/2 - source.getWidth() + index*(source.getWidth()/2),centerPanel.getHeight()/2 - source.getWidth()/2 );
        }
        centerPanel.validate();
        centerPanel.repaint();

    }


    /**		
     * <p> handles flipping of cards</p>		
     * @return JButton		
     */
    private JButton makeFlipCards(){
        JButton flip = new JButton( new AbstractAction("FLIP") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                count=0;
                int j=0;
                timesClicked++;
                for(int i=0;i<3;i+=2){
                    for(Component card:panels[i].getComponents()){
                        if(card instanceof JLabel){
                            if(timesClicked%2==0){
                                lesson.getPlayers().get(i).getCard(j).setFlipped(true);

                            }else{
                                lesson.getPlayers().get(i).getCard(j).setFlipped(false);

                            }
                        }

                        j++;
                        count++;
                    }
                    j=0;
                }
            }
        });
        flip.setText("<html><h3>Flip Cards</h3></html>");
        flip.setLayout(null);
        flip.setSize(flip.getPreferredSize());
        flip.setBackground(new Color(226,172,44));
        flip.setForeground(new Color(0, 134, 64));
        flip.setLocation(exit.getLocation().x+exit.getWidth()+framePadding/2,framePadding);
        return flip;
    }

    /**		
     * <p> displays bidding string</p>		
     * @return JLabel 		
     */
    private JLabel makeBiddingLabel(){
        biddingLabel=new JLabel();
        biddingLabel.setLayout(null);
        biddingLabel.setText("<html><h3 style='color:white;'>The Bidding is: "+lesson.getBiddingString()+"</h3> <h3 style='color:white;'> The Bidding Suit is: "+ lesson.getBiddingSuit()+"</h3></html>");

        biddingLabel.setSize(biddingLabel.getPreferredSize());
        biddingLabel.setLocation(centerPanel.getLocation().x + 5, centerPanel.getLocation().y +5);
        return biddingLabel;
    }


    /**		
     * <p> displays hints</p>		
     * @return JButton		
     */
    private JButton makeGetHintsButton (){
        JTextPane jtp = new JTextPane();
        jtp.setEditable(false);
        jtp.setSize(new Dimension(300, 100));
        jtp.setPreferredSize(new Dimension(300,100));
        hints = new JButton( new AbstractAction("HINTS") {
            
            @Override
            public void actionPerformed( ActionEvent e ) {

                if(hintQuestionNumber<hintQuestions.size()&& doneHints==false){
                    if (hintQuestionNumber==6){
                        jtp.setSize(new Dimension(300, 300));
                        jtp.setPreferredSize(new Dimension(300,300));
                        jtp.setText(lesson.getHintasked(hintQuestions.get(hintQuestionNumber)));
                        JOptionPane.showMessageDialog(window,jtp, hintQuestions.get(hintQuestionNumber), 1);
                        
                        
                    }
                    else{
                        jtp.setText(lesson.getHintasked(hintQuestions.get(hintQuestionNumber)));
                        JOptionPane.showMessageDialog(window,jtp, hintQuestions.get(hintQuestionNumber), 1);
                        hintQuestionNumber++;
                    }
                }
                else{
                    JOptionPane.showMessageDialog(window,"No more Hints to give!");
                    doneHints=true;
                    
                }
            }
        });
        hints.setText("<html><h3>GETS HINTS</h3></html>");
        hints.setLayout(null);
        hints.setSize(hints.getPreferredSize());
        hints.setBackground(new Color(226,172,44));
        hints.setForeground(new Color(0, 134, 64));
        hints.setLocation((int) tips.getLocation().x-hints.getWidth()-framePadding/2,ySize - hints.getHeight() - 2*framePadding);
        return hints;
    }
    /**		
     * <p> displays Tips on clicking</p>		
     * @return JButton		
     */
    private JButton	makeGetTipsButton(){
        tips = new JButton( new AbstractAction("TIPS") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                
                if(timesTipClicked<lesson.getTips().size()){
                        JOptionPane.showMessageDialog(window,
                        "Tip: " + lesson.getTipsasked(timesTipClicked) + " (click Tips for more)");
                        timesTipClicked++;
                        
                }
                else{
                    JOptionPane.showMessageDialog(window,
                    "No more Tips!"+lesson.getPlayers().get(currentPlayer).getPlayerName()+" is playing...");
                    tips.setEnabled(false);
                    tips.setBackground(Color.GRAY);
                    
                    
                }

            }
        });
        tips.setText("<html><h3>GETS TIPS</h3></html>");
        tips.setLayout(null);
        tips.setSize(tips.getPreferredSize());
        tips.setBackground(new Color(226,172,44));
        tips.setForeground(new Color(0, 134, 64));
        tips.setLocation(xSize - (int)tips.getPreferredSize().getWidth()-framePadding,ySize - tips.getHeight() - 2*framePadding);
        return tips;
    }



    public void handleClaim(){
        if(copyBestCase.get(0).equals("CLAIM")){
            JOptionPane.showMessageDialog(window,
                    "Congratulations you win the remaining "+(13-tricks)+" tricks");
            lesson.getPlayers().get(currentPlayer).addTrickWins((13-tricks));
            for(int i=0;i<panels.length;i++){
                for(Component c:panels[i].getComponents()){
                    if(c instanceof JLabel){
                        panels[i].remove(c);
                    }
                }
            }
        }else{
            String s = (String)JOptionPane.showInputDialog(
                    window,
                    "Please type the order of cards played for the remaining "+ (13-tricks)+" tricks as a coma separated list","",JOptionPane.PLAIN_MESSAGE);
            if(s!=null){
                JOptionPane.showMessageDialog(window,
                        "You won't win with that order");
            }

        }
    }

    /**		
     * <p> claim remaining of the game</p>		
     * @return JButton		
     */

    private JButton makeClaim(){
        claim = new JButton( new AbstractAction("CLAIM") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                    handleClaim();
            }
        });
        claim.setText("<html><h3>CLAIM</h3></html>");
        claim.setLayout(null);
        claim.setBounds(hints.getLocation().x, hints.getLocation().y - (int)claim.getPreferredSize().getHeight() - framePadding/2,tips.getWidth()+hints.getWidth()+framePadding/2,tips.getHeight());
        claim.setBackground(new Color(226,172,44));
        claim.setForeground(new Color(0, 134, 64));

        return claim;
    }

    /**		
     * <p> sets the layout and how the score is displayed</p>		
     * @return JLabel		
     */
    private JLabel makeScore(){
        score = new JLabel("<html><h1>N + S Score: "+(lesson.getPlayers().get(1).getTrickWins()+lesson.getPlayers().get(3).getTrickWins())+"</h1><h2>W + E Score: "+(lesson.getPlayers().get(0).getTrickWins()+lesson.getPlayers().get(2).getTrickWins())+"</h2></html>");
        score.setLayout(null);
        score.setSize(score.getPreferredSize());
        score.setForeground(Color.white);
        score.setLocation((3*xSize)/4 + 30,framePadding);
        return score;
    }

    /**		
     * <p>exit lesson</p>		
     * @return JBUtton		
     */
    private JButton makeExitButton(){

        exit = new JButton( new AbstractAction("EXIT") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                window.setVisible(false);
            }
        });
        exit.setText("<html><h2>EXIT</h2></html>");
        exit.setOpaque(false);
        exit.setContentAreaFilled(false);
        exit.setLayout(null);
        exit.setSize(exit.getPreferredSize());
        exit.setBorder(BorderFactory.createLineBorder(new Color(226,172,44),2));
        exit.setForeground(new Color(226,172,44));
        exit.setFocusPainted(false);
        exit.setLocation(framePadding,framePadding);
        return exit;
    }



    /**		
     * <p> corrects display on different monitors</p>		
     * @param source		
     * @param offset 		
     */
    public void offSet(JLabel source,int offset){
        Point pt = source.getLocation();
        int x = pt.x;
        int y = pt.y;
        source.setLocation(x,y+offset);
        if(offset>0){
            source.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    /**		
     * <p> makes the play log scroll-able and shows the progress of the game</p>		
     * @return JScrollPane		
     * @throws IOException 		
     */
    public JLabel makePlayLog() throws IOException{
        
        playerTurn = new JLabel();
        playerTurn.setForeground(Color.white);
	playerTurn.setSize(300,50);
        playerTurn.setLocation(centerPanel.getLocation().x +30 ,centerPanel.getLocation().y + centerPanel.getHeight() - playerTurn.getHeight() - 30);
             
        
        return playerTurn;
    }


}

