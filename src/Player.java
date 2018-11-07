public class Player {
	private String name;
	private int currStrikes, gamesWon;
	private Date[] moves; //this holds this player's moves for this game
	private int numMoves; //how many moves this player has made
	private boolean isHuman;
	
	public Player(String inName){
		this.name = inName;
		this.currStrikes = 0;
		this.gamesWon = 0;
		moves = new Date[22]; // only look at entries up to index numMoves, rest are bogus!
		numMoves = 0;
		if(inName.length()<2){ //avoids substring error
			isHuman = true;
		}
		else{
			this.isHuman = !inName.substring(0,2).equals("AI");
		}
	}
	
	public void recordMove(Date inDate){
		moves[numMoves++]=inDate;
	}
	
	public Date[] getMoves(){
		return this.moves;
	}
	
	public int getNumMoves(){
		return this.numMoves;
	}
	
	public boolean getIsHuman(){
		return this.isHuman;
	}
	
	public int getWins(){
		return this.gamesWon;
	}
	public int getStrikes(){
		return this.currStrikes;
	}
	public void incStrikes(){
		System.out.println(this.name+", you now have "+ ++this.currStrikes +" strikes");
	}
	public void reset(){
		this.currStrikes = 0;
		this.numMoves = 0;
	}
	public void won(){
		this.gamesWon++;
	}
	public String getName(){
		return this.name;
	}
}