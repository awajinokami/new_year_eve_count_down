//Data Class
public class Date {
	private String name;
	private int num;
	private static int[] maxMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	private static String[] monNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
			"Aug", "Sep", "Oct", "Nov", "Dec"};
	public Date(String inName, int inNum){
		this.name = inName;
		this.num = inNum;
	}

	public static int dayNum(Date inDate){ // jan1 = 0, dec31 = 364
		int answer = 0;
		for(int i = 1; i<monthNumMax(inDate.getName())/100;i++){
			answer+=maxMonth[i-1];
		}
		return answer+inDate.getNum()-1;
	}

	public static int numLegal(Date inDate){
		String mon = inDate.getName();
		int monNum = monthNumMax(mon)/100;
		int ans = monthNumMax(mon)%100-inDate.getNum(); //# left in this month
		for(int i = monNum+1; i<=12; i++ ){//# other months to choose
			if (inDate.getNum()<=maxMonth[i-1]){ //making sure "nov 31" doesn't get counted
				ans++;
			}
		}
		return ans;
	}

	public Date[] legalMoves(){
		Date[] nextMoves = new Date[numLegal(this)];
		int index; //declared here so that it will be saved after loop
		int monNum = monthNumMax(this.name)/100;
		int monMax = monthNumMax(this.name)%100;
		for(index=0; index<monMax-this.num;index++){ //puts in all remaining days of curr month
			nextMoves[index] = new Date(this.name, this.num+index+1);
		}
		for(int i = monNum+1;i<=12;i++){
			if (this.num <=maxMonth[i-1]){
				nextMoves[index] = new Date(monNames[i-1], this.num);
				index++;
			}
		}
		return nextMoves;
	}

	public String getName(){
		return this.name;
	}
	public int getNum(){
		return this.num;
	}
	public boolean equals(Date other){
		return ((this.name.equals(other.getName()))&&(this.num==other.getNum()));
	}
	//divide by 100 to get monthNum, mod by 100 to get monthMax
	public static int monthNumMax(String inMon){ //note: there is a more elegant way to write this with arrays...
		switch (inMon){
		case "Jan": return 131;
		case "Feb": return 228;
		case "Mar": return 331;
		case "Apr": return 430;
		case "May": return 531;
		case "Jun": return 630;
		case "Jul": return 731;
		case "Aug": return 831;
		case "Sep": return 930;
		case "Oct": return 1031;
		case "Nov": return 1130;
		case "Dec": return 1231;
		default: return 0;
		}
	}
	public boolean after(Date prior){
		int month1 = monthNumMax(prior.getName())/100;
		int month2 = monthNumMax(this.name)/100;
		if (month2!=month1){
			return (month2>month1);
		} else {
			return (this.num>prior.num);
		}
	}
	public String toString(){
		return this.name+" "+this.num;
	}
}
