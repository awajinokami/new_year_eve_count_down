// New Year Eve Count Down AI Game by Yifan Peng
import java.util.Scanner;
import java.util.Random;

public class Project1 {
	private static Scanner scnr = new Scanner(System.in);
	private static Scanner strScan;

	public static String coinFlip(String inEntry, Random inGen) {
		String entry, flip;
		int rand = inGen.nextInt(2);
		inEntry = inEntry.substring(0, 1).toUpperCase();
		if (inEntry.equals("H")) {
			entry = "Heads";
			flip = "Tails";
		} else {
			entry = "Tails";
			flip = "Heads";
		}
		return rand + entry + flip;
	}

	public static void main(String[] args) {
		Date[] testing;
//		final int TRIALS = 10000;
		final int TRIALS = 100000;
		final int SEED = 123;
		boolean validMove, actualCal, done;
		Random rangen = new Random(123);
		Date currMove, prevMove, lastDay = new Date("Dec", 31);
		int turnNum, myNum = 0, max, gameNum = 1;
		String entry = "", answer = "", name1 = "", name2 = "", myMonth = "", coinResult;
		Player person1 = null, person2 = null, temp, currPlayer, nextPlayer;

		int learnCounter = 0;
		boolean learnPhase = true, printThis = false;
		StartTable table = new StartTable();

		// Start AI training here
		System.out.println("Beginning AI learning phase for " + TRIALS + " games");
		do {
			if (learnCounter == TRIALS && person1 != null && person2 != null) {
				System.out.println("The running total of games won so far is:");
				System.out.println(person1.getName() + "=" + person1.getWins());
				System.out.println(person2.getName() + "=" + person2.getWins());
				
				if (!learnPhase && !printThis) {
					return;
				}
				
				learnPhase = false;
				learnCounter = 0;
				name1 = "";
				name2 = "";
				
				System.out.println("learning phase complete\n");
			}

			while (name1.length() == 0) {
				if (learnPhase) {
					name1 = "AI#1";
				} else {
					System.out.print("Enter name for one of the players: ");
					name1 = scnr.nextLine();
				}
			}
			person1 = new Player(name1);
			while (name2.length() == 0 || name1.equals(name2)) {
				if (learnPhase) {
					name2 = "AI#2";
				} else {
					System.out.print("Enter a different name for the opponent: ");
					name2 = scnr.nextLine();
				}
			}
			person2 = new Player(name2);
			done = false;
			printThis = person1.getIsHuman() || person2.getIsHuman();
			while (!done) {
				while (entry.length() == 0 || !(entry.substring(0, 1).toUpperCase().equals("H")
						|| entry.substring(0, 1).toUpperCase().equals("T"))) {
					if (printThis) {
						System.out.print(person1.getName() + ", call Heads or Tails to determine starting player: ");
					}
					if (person1.getIsHuman()) {
						entry = scnr.nextLine();
					} else {
						if (rangen.nextInt(2) == 0) {
							entry = "H";
							if (printThis) {
								System.out.println("Heads");
							}
						} else {
							entry = "T";
							if (printThis) {
								System.out.println("Tails");
							}
						}
					}
				}
				prevMove = new Date("Jan", 0); // a dummy date purely for initialization purposes
				turnNum = 1;
				coinResult = coinFlip(entry, rangen);
				if (coinResult.charAt(0) == '0') {
					if (printThis) {
						System.out.println("It came up " + coinResult.substring(1, 6) + "! " + person1.getName()
								+ " won the coin toss");
					}
					currPlayer = person1;
					nextPlayer = person2;
				} else {
					if (printThis) {
						System.out.println("It came up " + coinResult.substring(6, 11) + "! " + person1.getName()
								+ " lost the coin toss");
					}
					currPlayer = person2;
					nextPlayer = person1;
				}
				if (printThis) {
					System.out.println("\nThis is game number " + gameNum);
				}
				currMove = new Date("Jan", 0);
				validMove = false;
				while (!((currMove.equals(lastDay) && validMove) || (currPlayer.getStrikes() == 3))) {
					validMove = false;
					actualCal = false;
					while (!actualCal) {
						actualCal = true;
						if (printThis) {
							if (turnNum != 1) {
								System.out.println(nextPlayer.getName() + " chose " + prevMove + " last turn");
							}
							System.out.print("Game#" + gameNum + " Turn#" + turnNum + ": " + currPlayer.getName() + "("
									+ currPlayer.getStrikes()
									+ " strikes), enter a month followed by a space then a date: ");
						}
						if (currPlayer.getIsHuman()) {
							entry = scnr.nextLine();
						} else {
							if (turnNum == 1) {
								entry = "Jan 1";
							} else {
								testing = prevMove.legalMoves();
								if (testing.length != 0) {
									if (learnPhase) {
										currMove = table.likelyMove(testing);
									} else if (currPlayer.getName().toLowerCase().equals("ai random")) {
										currMove = testing[rangen.nextInt(testing.length)];
									} else {
										currMove = table.bestMove(testing);
									}
									entry = currMove.getName() + " " + currMove.getNum();
								}
							}
							if (printThis) {
								System.out.println(entry);
							}
						}
						if (entry.length() == 0) {
							System.out.println("No entry detected");
							actualCal = false;
						} else {
							strScan = new Scanner(entry);
							myMonth = strScan.next();
							if (myMonth.length() >= 3) {
								myMonth = myMonth.substring(0, 1).toUpperCase() + myMonth.substring(1, 3).toLowerCase();
							}
							if (myMonth.length() < 3) {
								System.out.println("Entry must be at least 3 letters to determine month.");
								actualCal = false;
							} else { // block below means first word had 3 letters properly cased
								if (Date.monthNumMax(myMonth) == 0) {
									System.out.println(
											"the first 3 letters \"" + myMonth + "\" don't match a known month");
									actualCal = false;
								}
							}
							if (!strScan.hasNext()) {
								System.out.println("You did not make a second entry for the date");
								actualCal = false;
							} else if (!strScan.hasNextInt()) {
								System.out.println("The second entry, the date, must be a number");
								entry = strScan.next(); // a dummy var, to check if there's a 3rd entry
								actualCal = false;
							} else
								myNum = strScan.nextInt();
							if (actualCal) { // block below means 2nd entry was a number AND month was legit
								max = Date.monthNumMax(myMonth) % 100;
								if (myNum > max || myNum < 1) {
									System.out.println("Your entry date of " + myNum + " is not between 1 and " + max
											+ ", the number of days in " + myMonth);
									actualCal = false;
								}
							}
							if (strScan.hasNext()) {
								System.out.println("Only two entries needed. Ignoring additional input");
							}
							strScan.close();
						}
					} // END while loop of garbage input, actualCal will from this stage onward
					currMove = new Date(myMonth, myNum);
					validMove = true;
					if (turnNum == 1) {
						if (myMonth.equals("Jan") && myNum == 1) {
							validMove = true;
						} else {
							System.out.println("First player must start with Jan 1");
							currPlayer.incStrikes();
							validMove = false;
						}
					} else {
						if (currMove.after(prevMove)) {
							if (currMove.getName().equals(prevMove.getName())) {
								validMove = true;
							} else {
								if (currMove.getNum() == prevMove.getNum()) {
									validMove = true;
								} else {
									System.out.println("If choosing a month other than " + prevMove.getName()
											+ ", then it must have a date of " + prevMove.getNum()
											+ ", the same date as " + nextPlayer.getName() + " chose last turn");
									validMove = false;
									currPlayer.incStrikes();
								}
							}
						} else {
							validMove = false;
							System.out.println("You must choose a date later than " + prevMove);
							currPlayer.incStrikes();
						}
					}
					if (validMove) {
						currPlayer.recordMove(currMove);
						temp = currPlayer;
						currPlayer = nextPlayer;
						nextPlayer = temp;
						prevMove = currMove;
						turnNum++;
					}
				} // END while loop of game still in progress
				if (printThis) {
					System.out.print("Game#" + gameNum + " is over: ");
					if (currPlayer.getStrikes() == 3) {
						System.out.println(currPlayer.getName() + " lost on turn " + turnNum + " because of 3 strikes");
					} else {
						System.out.println(
								nextPlayer.getName() + " won on turn " + (turnNum - 1) + " by reaching Dec 31!");
					}
				}
				nextPlayer.won();
				if (learnPhase) {
					table.update(currPlayer.getMoves(), currPlayer.getNumMoves(), 0);
					table.update(nextPlayer.getMoves(), nextPlayer.getNumMoves(), 1);
				}
				if (printThis) {
					System.out.println("The running total of games won so far is: \n" + person1.getName() + "="
							+ person1.getWins());
					System.out.println(person2.getName() + "=" + person2.getWins());
					System.out.print("\nDo you wish to play again (Y/N)? ");
				}
				if (person1.getIsHuman()) {
					answer = scnr.nextLine();
				} else {
					++learnCounter;
					if ((learnCounter > 0 && learnCounter % 1000 == 0 && learnPhase) ||
						(!learnPhase && !printThis && gameNum > 0 && gameNum % 1000 == 0)) {
						System.out.println(person1.getName() + " and " + person2.getName()
								+ " have played each other for " + learnCounter + " games");
					}
					if ((learnPhase && learnCounter < TRIALS) || (!learnPhase && gameNum < TRIALS)) {
						answer = "Yes";
					} else {
						answer = "No";
					}
					if (printThis) {
						System.out.println(answer);
					}
				}

				if ((answer.length() == 0) || !(answer.substring(0, 1).toLowerCase().equals("n"))) {
					done = false;
					if (!learnPhase) {
						gameNum++;
					}
					person1.reset();
					person2.reset();

				} else {
					done = true;
				}
			} // END of tournament loop
		} while (learnPhase || (!learnPhase && !printThis));

		scnr.close();
	}
}