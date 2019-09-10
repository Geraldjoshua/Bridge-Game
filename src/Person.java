import java.util.ArrayList;
import java.util.HashSet;

/**
 * person class to instantiate players of the game
 * @author Chris Cushway
 * @author Gerald Ngumbulu
 * @author Blessed Chitamba
 * @version 1.0
 */

public class Person{

    private ArrayList<Card> hand;
    private int points;
    private String name;
    private String playedcard;
    private boolean isWinner = false;
    private int trickWins;
    private int handStrength;
    private boolean canPlay;


    /**
     * <p>constructor for testing purposes </p>
     *
     */
    Person(){

        this.hand = new ArrayList<Card>();
        this.points = 0;
        this.name = "";
        this.trickWins = 0;
        this.handStrength = 0;
        this.canPlay = true;
    }

    /**
     * <p>constructor for testing purposes </p>
     * @param name Player's name
     *
     */
    Person(String name){

        this.hand = new ArrayList<Card>();
        this.name=name;
        this.points = 0;
        this.trickWins = 0;
        this.handStrength = 0;
        this.canPlay = false;
    }

    /**
     * <p>constructor for the person class </p>
     * @param hand An ArrayList of card that contains a player's hand
     * @param name Player's name
     *
     */
    Person(ArrayList<Card> hand,String name){
        this.hand = new ArrayList<Card>(hand);
        this.points = 0;
        this.name = name;
        this.canPlay = true;
    }

    /**
     * <p>sets the name of the the player: for testing purposes </p>
     * @param name Player's name
     *
     */
    public void setName(String name){

        this.name=name;

    }

    public void addTrickWins(int tricks){
        trickWins+=tricks;
    }

    /**
     * <p>increases trick win by one </p>
     *
     */
    public void incrementTrickWins(){
        this.trickWins++;
    }

    /**
     * <p>gets card in player's hand</p>
     * @param index Used to get a card in player's hand.
     * @return Card at that index in the hand.
     *
     */
    public Card getCard(int index){

        return hand.get(index);

    }

    /**
     * <p> set if a player can play</p>
     * @param canPlay 
     */
    public void setCanPlay(boolean canPlay){
        this.canPlay = canPlay;
    }

    /**
     * <p> set if a player can play</p>
     * @return 
     */
    public boolean getCanPlay(){
        return canPlay;
    }

    /**
     * <p>overloaded gets card in player's hand  </p>
     * @param card Used to get that specifc card in player's hand.
     * @return Card if it is in the player's hand else null
     *
     */
    public Card getCard(String card){

        for(int i=0;i<hand.size();i++){
            if(hand.get(i).toString().equals(card)){

                return this.hand.get(i);
            }
        }
        return null;
    }

    /**
     * <p>gets won trick by that person</p>
     * @return int number of tricks won
     *
     */
    public int getTrickWins(){

        return this.trickWins;

    }

    /**
     * <p>set trick wins if need be (used in case of claim) when claim is done.
     * The person wins the remainder of the tricks playable
     * eg 13(numoftrickplays) - indexClaimedOn + pointsPlayerCurrentlyHas </p>
     * @param num the num of trick wins after claim
     *
     */
    public void setTrickWins(int num){

        this.trickWins=num;

    }

    /**
     * <p>Prints hand with suit characters </p>
     *
     */
    public void printNiceHand(){
        for(Card card:hand){

            System.out.print(card.toString(true)+" ");

        }

    }

    /**
     * <p>Sets the hand </p>
     * @param hand  player's hand that contains ArrayList of cards.
     *
     */
    public void setHand(ArrayList<Card> hand){

        this.hand = new ArrayList<>(hand);

    }

    /**
     * <p>gets player's name </p>
     * @return String Contains player's name
     *
     */
    public String getPlayerName(){

        return name;

    }

    /**
     * <p>get player's hand </p>
     * @return hand containing ArrayList of player's cards
     *
     */
    public ArrayList<Card> getPlayerHand(){

        return hand;

    }

    /**
     * <p>sets the winner </p>
     * Turn boolean iswinner to true if the player is a winner of the trick else false </p>
     *
     */
    public void setWinner(){

        this.isWinner = true;

    }

    /**
     * <p>gets winner of the trick</p>
     * @return boolean returns the winner of the trick as true or false
     *
     */
    public boolean getWinner(){

        return this.isWinner;

    }

    /**
     * <p>resets winner before the start of another trick
     * sets boolean iswinner to false</p>
     *
     */
    public void resetWinner(){

        this.isWinner = false;

    }

    /**
     * <p>gets a player's points </p>
     * @return int returns player's points
     *
     */
    public int getPersonPoints(){

        return points;

    }

    /**
     * <p>Method for hand strength functionality</p>
     * @param card card played to win a trick during the game
     *
     */
    public void incrementPoints(String card){
        if(card.charAt(0)=='A'){
            handStrength+=4;
        }
        else if(card.charAt(0)=='K'){
            handStrength+=3;
        }
        else if(card.charAt(0)=='Q'){
            handStrength+=2;
        }
        else if(card.charAt(0)=='J'){
            handStrength+=1;
        }
        else{
            handStrength+=0;
        }

    }

    /**
     * <p>not too sure about this method screams redundancy </p>
     * @return points not sure what points are these will be determined during testing
     *
     */
    public int getPoints(){

        return points;

    }

    /**
     * <p>sets points: used for testing purposes </p>
     * @param number of points to be set.
     *
     */
    public void setPoints(int number){

        this.points = number;

    }

    /**
     * <p>add points to the player </p>
     * @param points to be added.
     *
     */
    public void addPoints(int points){
        this.points+=points;
    }

    /**
     * <p>records the played card </p>
     * @param card  To be recorded as already played.
     *
     */
    public void recordplayedCard(String card){
        this.playedcard = card;
    }

    /**
     * <p>gets the recorded played card </p>
     * @return String of playedcard.
     *
     */
    public String getrecordplayedCard(){
        return playedcard;
    }

    /**
     * <p>See if card is in the players hand </p>
     * @param card To be checked if it is in the player's hand.
     * @return boolean True if it is in player's hand else false.
     *
     */
    public boolean inHand(String card){
        for (Card hand1 : hand) {
            if (hand1.toString().equals(card)) {
                //hand.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * <p>checks the suit in player's hand </p>
     * @param suit Character to be compared against  the others in the player's hand
     * @return boolean False if suit is present else True
     *
     */
    public boolean noSuit(char suit){

        for (Card hand1 : hand) {
            if (hand1.getSuit() == suit) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>returns best case in the player's hand according to bestcases given in the lesson</p>
     * @param bestcard String of arraylist containing the bestcards to be played.
     * @return String for best case in player's hand else no best case
     *
     */
    public String bestCaseInHand(ArrayList<String> bestcard){
        HashSet<String> hset = new HashSet<>();
        for (Card hand1 : hand) {
            hset.add(hand1.toString());
        }

        for (String bestcard1 : bestcard) {
            if (hset.contains(bestcard1)) {
                return bestcard1;
            }
        }
        return "There is no best case";
    }


    /**
     * <p>remove the card from players hand after it has been played</p>
     * @param card To be removed
     *
     */
    public void removePlayedCard(String card ){
        for(int i=0;i<this.hand.size();i++){
            if(this.hand.get(i).toString().equals(card)){
                this.hand.remove(i);
            }
        }
    }

    /**
     * <p> removes played card</p>
     * @param index 
     */
    public void removePlayerCard(int index){
        hand.remove(index);
    }
    /**
     * <p>checks if card in hand </p>
     * @param card The card to be checked with cards in player's hand
     * @return boolean False if in hand else true
     *
     */
    public boolean checkhand(String card){
        boolean check = true;
        for (Card hand1 : hand) {
            if (hand1.toString().equals(card)) {
                check = false;
            }
        }
        return check;
    }



}
