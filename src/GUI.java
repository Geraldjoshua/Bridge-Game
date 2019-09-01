import java.sql.SQLOutput;
import java.util.ArrayList;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Component;

public class GUI{

    private Lesson lesson;
    private final int framePadding = 40;
    private JFrame window;
    private ArrayList<JLabel> cardLabels = new ArrayList<JLabel>();
    private JPanel[] panels = new JPanel[4];
    private final int xSize;
    private final int ySize;
    private JPanel centerPanel;
    private int currentPlayer;
    private JPanel playLog;
    private JLabel playerTurn;
    private JButton tips;
    private volatile boolean played;
    private JButton hints;
    private int play;
    private ArrayList<String> copyBestCase;
    private JButton exit;
    private JButton claim;
    private JLabel score;
    private ArrayList<Component> components = new ArrayList<Component>();
    private ArrayList<JLabel> cards = new ArrayList<JLabel>();
    private int timesClicked = 0;
    private int count=0;
    private MouseListener ml;
    private MouseListener hover;
    private int tricks;


    GUI(Lesson lesson) throws IOException, InterruptedException {
        this.played=false;
        this.lesson = lesson;
        this.copyBestCase = lesson.getBestCase();
        this.playLog = new JPanel();
        this.currentPlayer = 0;
        this.tricks = 0;
        this.play = 0;
        initComponents();
        //initPlayerPanels();
        Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(window.getGraphicsConfiguration());
        this.xSize = Toolkit.getDefaultToolkit().getScreenSize().width-scnMax.right - scnMax.left;
        this.ySize = Toolkit.getDefaultToolkit().getScreenSize().height-scnMax.bottom-scnMax.top;
        //System.out.println("r"+scnMax.right+" l"+scnMax.left+" t"+scnMax.top+" b"+scnMax.bottom);

        makeLessonScreen();
    }

    private void initComponents(){
        initFrame();
        initCardListeners();
        initHoverListener();
    }

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

    private void initFrame(){
        this.window = new JFrame("Bridge Tutor");
        this.window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.window.setLocationRelativeTo(null);
        this.window.setLayout(null);
        this. window.setUndecorated(true);
        this.centerPanel = new JPanel();

    }

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

    private void initCenterPanel(){
        centerPanel.setLayout(null);
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createLineBorder(new Color(226,172,44),2));
        centerPanel.setBounds((int)(xSize/4),(int)(ySize/4),(int)(xSize/2),(int)(ySize/2));
        components.add(centerPanel);
    }


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

    public void setHover(JButton source){

        source.setCursor(new Cursor(Cursor.HAND_CURSOR));

    }

    private void makeLessonScreen() throws IOException, InterruptedException {
        initPlayerPanels();
        initCenterPanel();
        for(int i=0;i<panels.length;i++){
            for(int j=0;j<13;j++){
                JLabel cardLabel;
                cardLabel = lesson.getPlayers().get(i).getCard(j).getCardLabel();
                if(!lesson.getPlayers().get(i).getPlayerName().toLowerCase().equals("south") && !lesson.getPlayers().get(i).getPlayerName().toLowerCase().equals("north")){
                    lesson.getPlayers().get(i).getCard(j).setFlipped(false);
                    cardLabel.setIcon(lesson.getPlayers().get(i).getCard(j).getImageIcon());
                }else{
                    lesson.getPlayers().get(i).getCard(j).setFlipped(false);
                    cardLabel.setIcon(lesson.getPlayers().get(i).getCard(j).getImageIcon());
                }

                cardLabel.setSize(72,96);


                cardLabels.add(cardLabel);

                cards.add(cardLabel);
            }
            double panelWidth = panels[i].getWidth();
            int xDim;
            int startMargin=60;
            int marginX;
            if(i%2 != 0){
                startMargin=(int)panelWidth/2 - (50*13)/2 - 50;
                xDim = 50;
            }else{
                xDim=40;
            }
            marginX=startMargin;
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

        components.add(makeScore());components.add(makePlayLog());components.add(makeExitButton());components.add(makeFlipCards());
        components.add(makeGetTipsButton());components.add(makeGetHintsButton());
        components.add(makePlayLogHeader());components.add(makeClaim());

        for(Component c:components){
            if(c instanceof JButton){
                c.addMouseListener(hover);
                ((JButton) c).setBorder(BorderFactory.createLineBorder(new Color(226,172,44),2));
            }
            window.add(c);
        }
        window.getContentPane().setBackground(new Color(0, 134, 64));
        window.setVisible(true);
        startGame();

    }

    public void startGame() throws InterruptedException {

        for(int i=0;i<13;i++) {
            for (play = 0; play < 4; play++) {
                played = false;
                System.out.println(lesson.getPlayers().get(currentPlayer).getPlayerHand() + " before");
                lesson.getPlayers().get(currentPlayer).setCanPlay(true);

                System.out.println(lesson.getPlayers().get(currentPlayer).getPlayerName() + " can play");
                if (!lesson.getPlayers().get(currentPlayer).getPlayerName().toLowerCase().equals("south") && !lesson.getPlayers().get(currentPlayer).getPlayerName().toLowerCase().equals("north")) {
                    autoPlay();
                } else {
                    addMouseListeners();
                    while (!played) {
                        Thread.sleep(1);
                    }
                    removeMouseListeners();
                }

                if(!copyBestCase.isEmpty()){
                    copyBestCase.remove(0);
                }

                lesson.getPlayers().get(currentPlayer).setCanPlay(false);
                System.out.println(lesson.getPlayers().get(currentPlayer).getPlayerName() + " has played");
                //System.out.println("The leading suit is " + lesson.getLeadingSuit());
                System.out.println(lesson.getPlayers().get(currentPlayer).getPlayerHand() + " after");

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
            playerTurn.setText("<html><h2>" + lesson.decideWinner() + "wins the trick</h2></html>");
            lesson.resetPoints();
            removeCenterCards();
            updateScoreBoard();
        }
    }

    public int findPlayer(JLabel source) throws InterruptedException {

        Container parent = source.getParent();
        for(int i=0;i<panels.length;i++){
            if(panels[i] == parent){
                return i;
            }
        }
        return -1;
    }

    public void makePlay(JLabel source,int index,int play) throws InterruptedException {
        int jIndex=0;
        if(lesson.getPlayers().get(index).getCanPlay()){
            for(Component card:panels[index].getComponents()){
                if(source == card){
                    lesson.getPlayers().get(index).getCard(jIndex).setFlipped(false);
                    ((JLabel)card).setIcon(lesson.getPlayers().get(index).getCard(jIndex).getImageIcon());
                    ((JLabel)card).validate();
                    ((JLabel)card).repaint();
                    moveCard((JLabel)card,currentPlayer);

                    if(play ==0){
                        lesson.setFirstCardPlayed(lesson.getPlayers().get(index).getCard(jIndex).toString());
                    }
                    lesson.getPlayers().get(index).addPoints(lesson.getPlayPoints(lesson.getPlayers().get(index).getCard(jIndex).toString()));

                    lesson.getPlayers().get(index).removePlayedCard(lesson.getPlayers().get(index).getCard(jIndex).toString());
                }
                jIndex++;
            }


            played=true;
        }
    }

    public void autoPlay() throws InterruptedException {

        boolean noValid=false;
        int i=0;

        for(Component c:panels[currentPlayer].getComponents()){
            if (!copyBestCase.isEmpty()&& (play == 0 && lesson.getPlayers().get(currentPlayer).getCard(copyBestCase.get(0)) != null )) {
                lesson.setFirstCardPlayed(copyBestCase.get(0));
            }
            if(panels[currentPlayer].getComponent(i) instanceof JLabel) {

                if (!copyBestCase.isEmpty() && (lesson.getPlayers().get(currentPlayer).getCard(copyBestCase.get(0)) != null && lesson.isValid(lesson.getPlayers().get(currentPlayer).getCard(copyBestCase.get(0)).toString(), lesson.getPlayers().get(currentPlayer)))) {
                    System.out.println("Best case is in our hand and valid to play and the card we are looking at is "+lesson.getPlayers().get(currentPlayer).getCard(i).getFlipped());

                    /*((JLabel)card).setIcon(lesson.getPlayers().get(index).getCard(jIndex).getImageIcon());
                    ((JLabel)card).validate();
                    ((JLabel)card).repaint();*/
                    if(c == lesson.getPlayers().get(currentPlayer).getCard(copyBestCase.get(0)).getCardLabel()){
                        System.out.println("Best case is in our hand and valid to play and its at index "+i);

                        noValid = false;
                        break;
                    }else{
                        System.out.println("But not at this index, the card here is "+lesson.getPlayers().get(currentPlayer).getCard(i).toString()+" at index "+i);
                        noValid=true;
                    }

                } else if (lesson.isValid(lesson.getPlayers().get(currentPlayer).getCard(i).toString(), lesson.getPlayers().get(currentPlayer))) {
                    System.out.println("Best case is not in our hand or not valid to play and card at index "+i+" is valid card is "+lesson.getPlayers().get(currentPlayer).getCard(i).toString());
                    noValid = false;
                    break;

                } else {
                    System.out.println("Best case is not in our hand or not valid to play and card at index "+i+" is valid");
                    noValid = true;
                }
            }
            i++;
        }
        System.out.println("INDEX OF CARD BEING PLAYED IS "+i);
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


        System.out.println("removing from hand of "+lesson.getPlayers().get(currentPlayer).getPlayerName()+ "with "+lesson.getPlayers().get(currentPlayer).getPoints()+"points");

    }
    public void updateScoreBoard(){
        score.setText("<html><h1>N + S Score: "+(lesson.getPlayers().get(1).getTrickWins()+lesson.getPlayers().get(3).getTrickWins())+"</h1><h2>W + E Score: "+(lesson.getPlayers().get(0).getTrickWins()+lesson.getPlayers().get(2).getTrickWins())+"</h2></html>");
    }

    public void addMouseListeners(){
        for(int i =0;i<panels[currentPlayer].getComponents().length;i++){
            if(panels[currentPlayer].getComponent(i) instanceof JLabel){
                if(play==0){
                    panels[currentPlayer].getComponent(i).addMouseListener(ml);
                    ((JLabel) panels[currentPlayer].getComponent(i)).setBorder(BorderFactory.createLineBorder(new Color(226,172,44),4));
                }else{
                    if(lesson.isValid(lesson.getPlayers().get(currentPlayer).getCard(i).toString(),lesson.getPlayers().get(currentPlayer))){
                        panels[currentPlayer].getComponent(i).addMouseListener(ml);
                        ((JLabel) panels[currentPlayer].getComponent(i)).setBorder(BorderFactory.createLineBorder(new Color(226,172,44),4));
                    }
                }

            }
        }
    }

    public void removeMouseListeners(){
        for(int i =0;i<panels[currentPlayer].getComponents().length;i++){
            if(panels[currentPlayer].getComponent(i) instanceof JLabel ){
                panels[currentPlayer].getComponent(i).removeMouseListener(ml);
                ((JLabel) panels[currentPlayer].getComponent(i)).setBorder(null);
            }
        }
    }
    public void removeCenterCards() throws InterruptedException {
        Thread.sleep(3000);
        System.out.println(centerPanel.getComponents().length);
        for(Component c:centerPanel.getComponents()){
            if(c instanceof JLabel ){
                centerPanel.remove(((JLabel)c));
                centerPanel.validate();
                centerPanel.repaint();
            }
        }
    }

    public void moveCard(JLabel source,int index){
        source.setBorder(null);
        panels[index].remove(source);
        panels[index].validate();
        panels[index].repaint();
        centerPanel.add(source);
        if(index==1 || index==3){
            source.setLocation(centerPanel.getWidth()/2 - 36,centerPanel.getHeight()/2 + (((index-2)*36)-36)-20);
            source.removeMouseListener(ml);
        }else{
            source.setLocation(centerPanel.getWidth()/2 - 72 + index*36,centerPanel.getHeight()/2 - 36 );
        }
        centerPanel.validate();
        centerPanel.repaint();

    }
    public void nextPlayerTurn() throws InterruptedException {
        /*currentPlayer++;
        if(currentPlayer<4){
            lesson.getPlayers().get(currentPlayer).setCanPlay(true);
            if(!lesson.getPlayers().get(currentPlayer).getPlayerName().toLowerCase().equals("south")) {
                playerTurn.setText("<html><p> It is " + lesson.getPlayers().get(currentPlayer).getPlayerName() + "'s turn </p></html>");
                Thread.sleep(3000);
                autoPlay();
            }
        }else{
            currentPlayer = 0;
            playerTurn.setText("<html><p> The trick is over </p></html>");
        }*/
    }

    private JLabel makePlayLogHeader(){
        JLabel playLogH = new JLabel("<html><h1 style='color:white;text-decoration:underline;' >Play Log</h1></html>");
        playLogH.setLayout(null);
        playLogH.setSize(playLogH.getPreferredSize());
        playLogH.setLocation(playLog.getX(),playLog.getY() - (int)playLogH.getPreferredSize().getHeight());
        return playLogH;
    }

    private JButton makeFlipCards(){
        JButton flip = new JButton( new AbstractAction("FLIP") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                count=0;
                int j=0;
                timesClicked++;
                for(int i=0;i<3;i+=2){
                    for(Component card:panels[i].getComponents()){
                        if(timesClicked%2==0){
                            lesson.getPlayers().get(i).getCard(j).setFlipped(true);
                            if(card instanceof JLabel){
                                ((JLabel)card).setIcon(lesson.getPlayers().get(i).getCard(j).getImageIcon());
                                ((JLabel)card).validate();
                                ((JLabel)card).repaint();
                            }
                        }else{
                            lesson.getPlayers().get(i).getCard(j).setFlipped(false);
                            //cards.get(count).setIcon(lesson.getPlayers().get(i).getCard(j).getImageIcon());
                            if(card instanceof JLabel){
                                ((JLabel)card).setIcon(lesson.getPlayers().get(i).getCard(j).getImageIcon());
                                ((JLabel)card).validate();
                                ((JLabel)card).repaint();
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

    private JButton	makeGetHintsButton(){
        hints = new JButton( new AbstractAction("HINTS") {
            @Override
            public void actionPerformed( ActionEvent e ) {

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

    private JButton	makeGetTipsButton(){
        tips = new JButton( new AbstractAction("TIPS") {
            @Override
            public void actionPerformed( ActionEvent e ) {

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

    private JButton makeClaim(){
        claim = new JButton( new AbstractAction("CLAIM") {
            @Override
            public void actionPerformed( ActionEvent e ) {

            }
        });
        claim.setText("<html><h3>CLAIM</h3></html>");
        claim.setLayout(null);
        claim.setBounds(hints.getLocation().x, hints.getLocation().y - (int)claim.getPreferredSize().getHeight() - framePadding/2,tips.getWidth()+hints.getWidth()+framePadding/2,tips.getHeight());
        claim.setBackground(new Color(226,172,44));
        claim.setForeground(new Color(0, 134, 64));

        return claim;
    }

    private JLabel makeScore(){
        score = new JLabel("<html><h1>N + S Score: "+(lesson.getPlayers().get(1).getTrickWins()+lesson.getPlayers().get(3).getTrickWins())+"</h1><h2>W + E Score: "+(lesson.getPlayers().get(0).getTrickWins()+lesson.getPlayers().get(2).getTrickWins())+"</h2></html>");
        score.setLayout(null);
        score.setSize(score.getPreferredSize());
        score.setForeground(Color.white);
        score.setLocation(xSize - 200 - (int)score.getPreferredSize().getWidth(),framePadding);
        return score;
    }

    private JButton makeExitButton(){

        exit = new JButton( new AbstractAction("EXIT") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                System.exit(0);
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



    public void offSet(JLabel source,int offset){
        Point pt = source.getLocation();
        int x = pt.x;
        int y = pt.y;
        source.setLocation(x,y+offset);
        if(offset>0){
            source.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    public JPanel makePlayLog() throws IOException{
        playLog.setOpaque(false);
        playLog.setLayout(null);
        playLog.setBorder(BorderFactory.createLineBorder(new Color(226,172,44),2));
        playerTurn = new JLabel();
        playerTurn.setSize(300,50);
        playerTurn.setForeground(Color.white);
        playerTurn.setLocation(20,20);
        playLog.setSize(300,150);
        playLog.add(playerTurn);
        playLog.setLocation(framePadding,ySize - 200 - framePadding);
        return playLog;
    }


}
