package com.yeepay.challenge.game2048.lucheng;

import java.util.ArrayList;

import com.yeepay.challenge.game2048.GameBoard;

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
		Vector v = new Vector(x, y);
		return v;
	}

}

class DoubleV
{
	public Vector farthest;
	public Vector next;

	public DoubleV(Vector farthest, Vector next)
	{
		this.farthest = farthest;
		this.next = next;
	}
}

public class Smoothness
{
	private static final double smoothWeight = 0.1;
	private static final double mono2Weight = 1.0;
	private static final double emptyWeight = 32.7;
	private static final double maxWeight = 1.0;

	public static double getValue(int[][] board)
	{
		return smoothness(board) * smoothWeight + mono2Weight
				* monotonicity2(board) + emptyCells(board) * emptyWeight
				+ maxWeight * maxValue(board);
	}

	public static final Vector[] vectors = { new Vector(0, -1),
			new Vector(1, 0), new Vector(0, 1), new Vector(-1, 0) };

	public static boolean withinBounds(Vector v)
	{
		return v.x >= 0 && v.x < 4 && v.y >= 0 && v.y < 4;
	}

	public static DoubleV findFarthestPostition(Vector cell, Vector v,
			int[][] board)
	{
		Vector previous;
		do
		{
			previous = cell.clone();
			cell.x = previous.x + v.x;
			cell.y = previous.y + v.y;
		} while (withinBounds(cell) && board[cell.x][cell.y] == 0);
		return new DoubleV(previous, cell);
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
						Vector v = vectors[direction];
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

	public static void search(int depth, double alpha, double beta,
			int positions)
	{
		double bestScore;
		int bestMove = -1;
		int result;

		bestScore = alpha;

	}

	public static void mymove(int direction, int [][]board)
	{
		GameBoard gb = new GameBoard();
		gb.setNumbers(board);
		gb.action("R");
		
	}
	
	public static void move(int direction, int [][] board)
	{
		Vector cell;
		int tile;
		Vector vector = vectors[direction];
		ArrayList<Vector> traversals = buildTraversals(vector);
		boolean moved = false;
		double score = 0;
		boolean won =false;
		for (Vector x : traversals)
		{
			for (Vector y : traversals)
			{
				cell = new Vector(x.x,y.y);
				tile = board[cell.x][cell.y];
				if(tile != 0)
				{
					DoubleV position = findFarthestPostition(cell, vector, board);
					int next = board[position.next.x][position.next.y];
					if(next !=0 && next == tile)
					{
						Tile merged = new Tile(position.next, tile*2);
						merged.mergedFrom = new int[]{tile, next};
						///
					}
				}
			}
		}

	}

	public static ArrayList<Vector> buildTraversals(Vector v)
	{
		ArrayList<Vector> traversals = new ArrayList<Vector>();
		int[] x = new int[4];
		int[] y = new int[4];
		for (int i = 0; i < 4; i++)
		{
			x[i] = i;
			y[i] = i;
		}
		if (v.x == 1)
			for (int i = 0; i < 2; i++)
			{
				int temp = x[i];
				x[i] = x[4 - i + 1];
				x[4 - i + 1] = temp;

			}
		if (v.y == 1)
			for (int i = 0; i < 2; i++)
			{
				int temp = y[i];
				y[i] = y[4 - i + 1];
				y[4 - i + 1] = temp;

			}
		for (int i = 0; i < y.length; i++)
		{
			traversals.add(new Vector(x[i], y[i]));
		}
		return traversals;
	}

}

class Tile
{
	public int x;
	public int y;
	public int val;
	public Vector prevPostion;
	public Tile(Vector position, int val)
	{
		x=position.x;
		y =position.y;
		this.val = val !=0?val:2;
		mergedFrom = new int[2];
	}
	public int []mergedFrom;
}
