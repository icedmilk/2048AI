package com.yeepay.challenge.game2048.lucheng;

class Vector // implements Cloneable
{
	int x;
	int y;

	Vector(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public Vector()
	{
	}

	public Vector clone()
	{
		Vector clonedVector = new Vector(x, y);
		return clonedVector;
	}

}


public class Evaluation
{
	private static final double MonotoWeight = 1.0;
	private static final double EmptyWeight = 100.0;
	private static final double MaxWeight = 1.0;

	private static final int SIZE = 4;


	/**
	 * get the score of current board
	 * 
	 * @param board
	 * @return
	 */
	public static double getEvaluation(int[][] board)
	{
		return MonotoWeight
				* monotonicity(board) + EmptyWeight
				* emptyCells(board) + MaxWeight * maxValue(board);
	}


	/**
	 * return the number of the empty cells
	 * 
	 * @param board
	 * @return
	 */
	private static int emptyCells(int[][] board)
	{
		int count = 0;
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				if (board[i][j] == 0)
					count += 1;

		return count;
	}

	/**
	 * return the max value of the cells
	 * 
	 * @param board
	 * @return
	 */
	private static int maxValue(int[][] board)
	{
		int max = 0;
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				if (board[i][j] > max)
					max = board[i][j];

		return max;
	}

	/**
	 * give the score by monotonicity, O(32)
	 * 
	 * @param board
	 * @return
	 */
	private static double monotonicity(int[][] board)
	{
		int[] totals = { 0, 0, 0, 0 };
		// calculate monotonicity by row
		for (int i = 0; i < SIZE; i++)
		{
			int current = 0;
			int next = current + 1;

			while (next < SIZE)
			{
				// find the first occupied grid
				while (next < 4 && board[i][next] == 0)
					next++;

				// out of bound
				if (next >= SIZE)
					next--;

				// calculate the current and next value
				double currentValue = board[i][current] != 0 ? Math
						.log(board[i][current]) / Math.log(2) : 0.0;
				double nextValue = board[i][next] != 0 ? Math
						.log(board[i][next]) / Math.log(2) : 0.0;

				if (currentValue > nextValue)
					totals[0] += nextValue - currentValue;
				else if (nextValue > currentValue)
					totals[1] += currentValue - nextValue;

				current = next;
				next++;
			}
		}

		// by column
		for (int j = 0; j < SIZE; j++)
		{
			int current = 0;
			int next = current + 1;

			while (next < SIZE)
			{
				while (next < SIZE && board[next][j] == 0)
					next++;

				if (next >= SIZE)
					next--;

				double currentValue = board[current][j] != 0 ? Math
						.log(board[current][j]) / Math.log(2) : 0.0;
				double nextValue = board[next][j] != 0 ? Math
						.log(board[next][j]) / Math.log(2) : 0.0;

				if (currentValue > nextValue)
					totals[2] += nextValue - currentValue;
				else if (nextValue > currentValue)
					totals[3] += currentValue - nextValue;

				current = next;
				next++;
			}
		}
		return Math.max(totals[0], totals[1]) + Math.max(totals[2], totals[3]);
	}


}
