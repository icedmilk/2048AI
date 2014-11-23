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

class DoubleVector
{
	public Vector farthest;
	public Vector next;

	public DoubleVector(Vector farthest, Vector next)
	{
		this.farthest = farthest;
		this.next = next;
	}
}

public class Evaluation
{
	private static final double SmoothWeight = 0.1;
	private static final double MonotoWeight = 1.0;
	private static final double EmptyWeight = 32.7;
	private static final double MaxWeight = 1.0;
	public static final Vector[] Vectors = { new Vector(0, -1),
			new Vector(1, 0), new Vector(0, 1), new Vector(-1, 0) };

	/**
	 * 
	 * @param board
	 * @return
	 */
	public static double getEvaluation(int[][] board)
	{
		return smoothness(board) * SmoothWeight + MonotoWeight
				* monotonicity2(board) + emptyCells(board) * EmptyWeight
				+ MaxWeight * maxValue(board);
	}

	public static boolean withinBounds(Vector coordinate)
	{
		return coordinate.x >= 0 && coordinate.x < 4 && coordinate.y >= 0
				&& coordinate.y < 4;
	}

	public static DoubleVector findFarthestPostition(Vector cell, Vector v,
			int[][] board)
	{
		Vector previous;
		do
		{
			previous = cell.clone();
			cell.x = previous.x + v.x;
			cell.y = previous.y + v.y;
		} while (withinBounds(cell) && board[cell.x][cell.y] == 0);
		return new DoubleVector(previous, cell);
	}

	public static int emptyCells(int[][] board)
	{
		int count = 0;
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				if (board[i][j] == 0)
					count += 1;

		return count;
	}

	public static int maxValue(int[][] board)
	{
		int max = 0;
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				if (board[i][j] > max)
					max = board[i][j];

		return max;
	}

	// µ¥µ÷ÐÔ
	public static int monotonicity2(int[][] board)
	{
		int[] totals = { 0, 0, 0, 0 };
		for (int i = 0; i < 4; i++)
		{
			int current = 0;
			int next = current + 1;
			while (next < 4)
			{
				while (next < 4 && board[i][next] == 0)
					next++;
				if (next >= 4)
					next--;
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

		for (int j = 0; j < 4; j++)
		{
			int current = 0;
			int next = current + 1;
			while (next < 4)
			{
				while (next < 4 && board[next][j] == 0)
					next++;
				if (next >= 4)
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

	public static int smoothness(int[][] board)
	{
		int smoothness = 0;
		for (int x = 0; x < 4; x++)
			for (int y = 0; y < 4; y++)
				if (board[x][y] != 0)
				{
					double val = Math.log(board[x][y]) / Math.log(2);
					for (int direction = 1; direction <= 2; direction++)
					{
						Vector v = Vectors[direction];
						Vector targetCell = findFarthestPostition(new Vector(x,
								y), v, board).next;
						if (withinBounds(targetCell)
								&& board[targetCell.x][targetCell.y] != 0)
						{
							int target = board[targetCell.x][targetCell.y];
							double targetValue = Math.log(target) / Math.log(2);
							smoothness -= Math.abs(val - targetValue);
						}
					}
				}
		return smoothness;
	}

}
