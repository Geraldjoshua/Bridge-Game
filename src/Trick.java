

public class Trick{

	private String west;
	private String north;
	private String east;
	private String south;
	private String claim;


	Trick(String west,String north,String east,String south){
		this.west=west;
		this.north=north;
		this.east=east;
		this.south=south;
	}
	Trick(String claim){
		this.claim=claim;
	}
	public String getWest(){
		return west.trim();
	}


	public static boolean checktrick(String cardplayed,int place,Trick trick){
		if(place==0){
			return cardplayed.equals(trick.west.trim());
		}
		if(place==1){
			return cardplayed.equals(trick.north.trim());
		}
		if(place==2){
			return cardplayed.equals(trick.east.trim());
		}
		else{
			return cardplayed.equals(trick.south.trim());
		}
	}

	public static boolean checkclaim(String word){
		return word.trim().equals("CLAIM");
	}

}