import java.util.Random;

public class StartTable {
	private int[][] gamesData;

	public StartTable() {
		this.gamesData = new int[365][2];
		for (int i = 0; i < this.gamesData.length; ++i) {
			for (int j = 0; j < this.gamesData[i].length; ++j) {
				this.gamesData[i][j] = 1;
			}
		}
		this.gamesData[364][0] = 0;
	}

	public void update(Date[] moves, int numMoves, int winOrLoss) {
		for (int i = 0; i < numMoves; ++i) {
			int dayNum = Date.dayNum(moves[i]);
			this.gamesData[dayNum][winOrLoss] += 1;
		}
	}

	public Date likelyMove(Date[] legalMoves) {
		Random rand = new Random(123);
		double[] winRates = new double[legalMoves.length];
		double ratesSum = 0;
		for (int i = 0; i < legalMoves.length; ++i) {
			Date current = legalMoves[i];
			int dayNum = Date.dayNum(current);
			int[] rates = this.gamesData[dayNum];
			winRates[i] = (double) rates[1] / (rates[0] + rates[1]);
			ratesSum += winRates[i];
		}

		double[] winRatesPercent = new double[legalMoves.length];
		double[] winRatesPercentSum = new double[legalMoves.length];
		for (int i = 0; i < legalMoves.length; ++i) {
			double ratePercent = winRates[i] / ratesSum;
			winRatesPercent[i] = ratePercent;
			winRatesPercentSum[i] = ratePercent;
		}

		for (int i = 0; i < winRatesPercentSum.length - 1; ++i) {
			for (int j = 0; j < winRatesPercentSum.length - 1 - i; ++j) {
				if (winRatesPercentSum[j] > winRatesPercentSum[j + 1]) {
					double temp = winRatesPercentSum[j];
					winRatesPercentSum[j] = winRatesPercentSum[j + 1];
					winRatesPercentSum[j + 1] = temp;
				}
			}
		}

		double percent = winRatesPercentSum[0];
		double dice = rand.nextDouble();
		for (int i = 1; i < legalMoves.length; ++i) {
			winRatesPercentSum[i] += winRatesPercentSum[i - 1];
			if (dice > winRatesPercentSum[i - 1] && dice <= winRatesPercentSum[i]) {
				percent = winRatesPercentSum[i] - winRatesPercentSum[i - 1];
				break;
			}
		}

		int cursor = 0;
		Date[] rtn = new Date[legalMoves.length];
		for (int i = 0; i < winRatesPercent.length; ++i) {
			if (winRatesPercent[i] - percent < 0.000001) {
				rtn[cursor++] = legalMoves[i];
			}
		}
		
		return rtn[rand.nextInt(cursor)];
	}

	public Date bestMove(Date[] legalMoves) {
		Date best = null;
		double maxRate = 0;
		for (int i = 0; i < legalMoves.length; ++i) {
			Date current = legalMoves[i];
			int dayNum = Date.dayNum(current);
			int[] rates = this.gamesData[dayNum];
			double rate = (double) rates[1] / (rates[0] + rates[1]);
			if (rate > maxRate) {
				maxRate = rate;
				best = current;
			}
		}
		return best;
	}
}
