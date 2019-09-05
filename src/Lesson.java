
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.LinkedHashMap;


/**
 * lesson class to instantiate lessons(tutor) of the bridgeGame
 * @author Chris Cushway
 * @author Gerald Ngumbulu
 * @author Blessed Chitamba
 * @version 1.0
 */

public class Lesson{

    private ArrayList<Person> players = new ArrayList<Person>(4);
    private ArrayList<Card> cardArray = new ArrayList<Card>(13);
    private ArrayList<String[]> bestCase = new ArrayList<String[]>();
    private String firstCardPlayed;
    private char leadingSuit;
    private char biddingSuit;
    private LinkedHashMap<String,String> hints;
    private LinkedHashMap<Integer,String> tips;
    private int helpLevel;
    private String [] questions = {"How many winners/losers do you have?","What threats do you see?",
            "What opportunities do you see?","Is there a danger suit?",
            "Is there a danger hand?","What do you know from the bidding?","What is your plan?"};
    private String biddingString;


    /**
     * <p>constructor</p>
     * @param filename the file to be parsed
     *
     * @throws java.io.IOException when the file does not match the lesson files
     *
     */
    Lesson(String filename,int level) throws IOException{
        players.add(new Person("West"));
        players.add(new Person("North"));
        players.add(new Person("East"));
        players.add(new Person("South"));
        loadInput(filename);
        loadTips("input/tips.txt");

        this.helpLevel=level;

    }

    /**
     * <p>another constructor for testing purposes</p>
     *
     */
    Lesson(){
        players.add(new Person("West"));
        players.add(new Person("North"));
        players.add(new Person("East"));
        players.add(new Person("South"));
    }

    /**
     * <p>setter to set hints</p>
     * @param hints Map that contains hints and their corresponding questions
     *
     *
     */
    public void setHints(LinkedHashMap<String,String> hints){
        this.hints = hints;

    }

    /**
     * <p>getter to get hints</p>
     * @return hints Map that contains hints and their corresponding questions
     *
     *
     */
    public LinkedHashMap<String,String> getHints(){
        return hints;
    }

    /**
     * <p> Obtains hints a player needs for a lesson</p>
     * @param key
     * @return String
     */

    public String getHintasked(String key){
        String hint = hints.get(key);
        return hint;
    }

    /**
     * <p>Obtains tips a player needs to remember during the game</p>
     * @param key
     * @return String
     */
    public String getTipsasked(int key){
        String tip = tips.get(key);
        return tip;
    }

    /**
     * <p>setter to set tips</p>
     * @param tips Map that contains tips
     *
     *
     */
    public void setTips(LinkedHashMap<Integer,String> tips){
        this.tips = tips;

    }

    /**
     * <p>getter to get tips</p>
     * @return tips Map that contains tips
     *
     *
     */
    public LinkedHashMap<Integer,String> getTips(){
        return tips;
    }

    public void setBiddingString(String bidding){
        this.biddingString = bidding;
    }

    public String getBiddingString(){
        return this.biddingString;
    }
    /**
     * <p>function to parse in input</p>
     * @param filename the file to be parsed
     *
     * @throws java.io.IOException when the file does not match the lesson files
     *
     */
    private void loadInput(String filename) throws IOException{

        Scanner lesson = new Scanner(new File(filename));
        System.out.println("The bidding is: ");
        String bidding = lesson.nextLine();

        setBiddingString(bidding);

        String[] Bidding = bidding.split(" ");
        setBidding(Bidding);
        System.out.println(bidding);
        
        int number_of_hands=0;
        
        while (number_of_hands<=3){
            String hand = lesson.nextLine();
            String[] hand_split = hand.split(",");
            for (String specific_Suit_cards  : hand_split){
                int length = specific_Suit_cards.length();
                for(int i=1; i<length;i++){
                    Card card = new Card(specific_Suit_cards.charAt(0),specific_Suit_cards.charAt(i));
                    cardArray.add(card);
                }
            }
            players.get(number_of_hands).setHand(cardArray);
            number_of_hands++;
            cardArray.clear();
        }

        setFirstCardPlayed(lesson.nextLine());

        //Loop to get best possible plays into an array
        int maxNumOfTricksLeft=13;
        while(maxNumOfTricksLeft>0){
            String trick = lesson.nextLine();
            String[] singleTrick = trick.split(",");
           

            //Check if claim has been played or invalid input (Will only happen before all 13 tricks have been played)
            if(singleTrick.length==1){
                if(singleTrick[0].equals("CLAIM")){
                    bestCase.add(singleTrick);
                }
                break;
            }

            bestCase.add(singleTrick);
            maxNumOfTricksLeft--;

        }
      
       
        LinkedHashMap<String,String> hint = new LinkedHashMap<>();
        for (String question : questions) {
            hint.put(question, lesson.nextLine());
        }
        setHints(hint);



    }

    /**
     * <p>function to parse in tip input</p>
     * @param filename the file to be parsed
     *
     * @throws java.io.IOException when the file does not match the lesson files
     *
     */
    private void loadTips(String filename) throws IOException{
        LinkedHashMap<Integer,String> tip = new LinkedHashMap<>();
        Scanner tipfile = new Scanner(new File(filename));
        int counter = 0;
        while(tipfile.hasNext()){
            tip.put(counter,tipfile.nextLine());
            counter++;
        }
        setTips(tip);

    }



    /**
     * <p>Method to return best case to be played</p>
     * @return ArrayList String
     */
    public ArrayList<String> getBestCase(){
        ArrayList<String> BestCase = new ArrayList<>();
        for(String[] trick:bestCase){
            for(String card:trick){
                BestCase.add(card);
            }
        }
        return BestCase;

    }

    /**
     * <p>sets leading suit</p>
     *
     * @param Suit
     */
    public void setLeadingSuit(char Suit){
        this.leadingSuit = Suit;
    }

    /**
     * <p>gets the leading suit of the trick which decides the suit for that trick</p>
     * @return char Contains the first card's suit played for that specific trick
     *
     */
    public char getLeadingSuit(){
        return leadingSuit;
    }


    /**
     * <p>gets the first card of the trick which decides the suit for that trick</p>
     * @return String Contains the first card played for that specific trick
     *
     */
    public String getFirstCardPlayed(){

        return this.firstCardPlayed;

    }


    /**
     * <p>checks if the play is valid </p>
     * @param card The card that needs to be checked if it is in player's hand
     * @param player the player who played that card
     * @return boolean That states if the play is valid(true) or not(false)
     *
     */
    public boolean isValid(String card , Person player){
        //play can be Suit from first play or trump Suit
        if(player.inHand(card) && (card.charAt(1) == getSuit())){
            //For gui purposes
            return true;
        }else return (!player.getPlayerHand().isEmpty())&&(player.noSuit(getLeadingSuit()));

    }

    /**
     * <p>translates a card to it equivalent points: Get points for card played </p>
     * @param card The card that needs to be translated to it equivalent points
     * @return integer Points for the played card
     *
     */
    public int getPlayPoints(String card){
        int points=0;
        if(card.charAt(1)==getBiddingSuit()){
            points+=15;
        }
        if(card.charAt(0)=='A'){
            points+=14;
        }else if(card.charAt(0)=='K'){
            points+=13;
        }else if(card.charAt(0)=='Q'){
            points+=12;
        }else if(card.charAt(0)=='J'){
            points+=11;
        }else if(card.charAt(0)=='T'){
            points+=10;
        }else{
            points+=Character.getNumericValue(card.charAt(0));
        }

        return points;

    }

    /**
     * <p>Sets bidding Suit by reversing the array
     * and sets bid equal to third element in the array</p>
     * @param bidding The string that contains the bid
     *
     *
     */
    public void setBidding(String[] bidding){
        ArrayList<String> biddingstring = new ArrayList<>(Arrays.asList(bidding));
        Collections.reverse(biddingstring);
        String card = biddingstring.get(3);
        biddingSuit = card.charAt(1);


    }


    public void checkClaim(String card){
        //To Do
    }

    /**
     * <p>getter to get the bidding suit from the start of the game  </p>
     * @return char containing the bidding suit
     *
     */
    public char getBiddingSuit(){

        return this.biddingSuit;

    }

    /**
     * <p>sets the first card played
     * and the leading suit</p>
     * @param card sets the first card played
     *
     *
     */
    public void setFirstCardPlayed(String card){
        setLeadingSuit(card.charAt(1));
        this.firstCardPlayed = card;

    }


    /**
     * <p>getter to get the suit from the first card played the first card played </p>
     * @return char containing the starting suit in a trick
     *
     */
    public char getSuit(){

        return this.firstCardPlayed.charAt(1);

    }

    /**
     * <p>getter to get the suit from the first card played the first card played </p>
     * @return ArrayList Person  containing all the players
     *
     */
    public ArrayList<Person> getPlayers(){

        return this.players;

    }



    /**
     * <p>gets Winner of trick based on boolean inside person class
     * and set it back to false after trick to prevent more than one winner scenario</p>
     *
     */
    public void resetWinner(){

        for(Person player : players ){
            player.resetWinner();
        }

    }

    /**
     * <p>Sets winner and incrementing that players trick wins </p>
     * @param playername of the player who won.
     */
    public void setWinner(String playername){

        for(Person player : players){
            if(player.getPlayerName().equals(playername)){
                player.setWinner();
                player.incrementTrickWins();
            }
        }

    }


    /**
     * <p>Decides winner of trick based on points from cards played </p>
     *
     * @return String
     */
    public String decideWinner(){
        Person winner = new Person();
        int points=0;
        for(Person player:players){
            if(player.getPoints()>points){
                points=player.getPoints();
                winner = player;
            }
        }

        // System.out.println(winner.getPlayerName() + " Wins the trick!");
        //setWinner(winner.getPlayerName());

        return winner.getPlayerName();
    }

    /**
     * <p>Decides winner of entire game by counting trick points </p>
     *
     */
    public String decideGameWinner(){
        Person winner = new Person();
        int trickWins=0;
        for(Person player:players){
            if(player.getTrickWins()>trickWins){
                trickWins=player.getTrickWins();
                winner = player;
            }
        }
        return winner.getPlayerName();
    }


    public void setHelpLevel(int level){
        this.helpLevel=level;
    }

    public int getHelpLevel(){
        return helpLevel;
    }
    /**
     * <p>Resets points for a trick so points don't carry over</p>
     *
     */
    public void resetPoints(){
        for(Person player : players){
            player.setPoints(0);
        }

    }

    /**
     * <p>displays all players in the game</p>
     *
     */
    public void printPlayers(){
        for (Person player : players) {
            System.out.println(player.getPlayerName());
        }
    }


    /**
     * <p>reorder players based on the who won the previous trick</p>
     *
     */
    public void reorderPlayers(){

        for(int i=0;i<players.size();i++){
            Person tempPlayer = players.get(0);
            if(!players.get(0).getWinner()){
                players.remove(0);
                players.add(tempPlayer);
            }

        }
        resetWinner();

    }




}



