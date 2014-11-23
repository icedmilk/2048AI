package com.yeepay.challenge.game2048.lucheng;

import java.util.ArrayList;

import com.yeepay.challenge.game2048.GameBoard;
import com.yeepay.challenge.game2048.GamePlayer;
import com.yeepay.challenge.game2048.GameConsole;

public class PlayerImpl implements GamePlayer
{

	private final String LEFT = "L";
	private final String RIGHT = "R";
	private final String UP = "U";
	private final String DOWN = "D";

	private final short HEIGHT = 4;
	private final short WIDTH = 4;
	private final short SIZE = 4;

	public PlayerImpl()
	{

	}

	public static void main(String[] args)
	{
		int result = 0;

		final int runTime = 100;

		for (int i = 0; i < runTime; i++)
		{
			result += GameConsole.start(PlayerImpl.class);
		}
		System.out.println(result / runTime);
	}

	private void trackGame(int[][] board)
	{
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();

	}

	public void arrayClone(int[][] cloner, int[][] inital)
	{
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				cloner[i][j] = inital[i][j];

	}

	public Simulator simulate(int[][] board, int direction)
	{
		GameBoard gb = new GameBoard();
		int[][] arr = new int[4][4];
		double score = 0.0;
		arrayClone(arr, board);

		switch (direction)
		{
		case 0:
			if (leftAble(board))
			{
				gb.setNumbers(arr);
				gb.action(LEFT);
				score = Smoothness.getValue(arr);

			}
			break;

		case 1:
			if (rightAble(board))
			{
				gb.setNumbers(arr);
				gb.action(RIGHT);
				score = Smoothness.getValue(arr);

			}
			break;
		case 2:
			if (upAble(board))
			{
				gb.setNumbers(arr);
				gb.action(UP);
				score = Smoothness.getValue(arr);

			}
			break;
		default:
			if (downAble(board))
			{
				gb.setNumbers(arr);
				gb.action(DOWN);
				score = Smoothness.getValue(arr);

			}
			break;
		}
		int[][] arr2 = new int[4][4];
		arrayClone(arr2, gb.getNumbers());
		return new Simulator(arr2, score);
	}

	class Simulator
	{
		public int[][] newboard;
		public double score;

		public Simulator(int[][] board, double score)
		{
			this.newboard = board;
			this.score = score;
		}
	}

	public double simulateMax(int[][] board)
	{
		double maxScore = 0.0;

		for (int i = 0; i < SIZE; i++)
		{
			double currentScore = simulate(board, i).score;
			if (currentScore > maxScore)
				maxScore = currentScore;
		}

		return maxScore;

	}

	public double simulateDepth(int[][] board, int depth)
	{
		double maxScore = 0.0;
//		final int finalSize = (int) Math.pow(SIZE, depth);
//		Simulator[] sim = new Simulator[finalSize];
		ArrayList<Simulator> simSave = new ArrayList<Simulator>();
		int[][] currentBoard = new int[SIZE][SIZE];
		arrayClone(currentBoard, board);
		simSave.add(new Simulator(currentBoard, depth));
		
		for (int i = 1; i <= depth; i++)
		{
			int currentSize = simSave.size();
			for (int j = 0; j < currentSize; j++)
			{

				for (int k = 0; k < SIZE; k++)// direction
				{
					
					Simulator temp = simulate(simSave.get(j).newboard, k);
					if(temp.score > 0)
						simSave.add(temp);
				}
				
			}
			for (int j = 0; j <currentSize; j++)
			{try
			{
				simSave.remove(j);
			} catch (Exception e)
			{
				System.out.println(simSave.size()+" " +currentSize);
			}
				
			}

		}
		


		
		for (int i = 0; i < simSave.size(); i++)
		{
			for (int j = 0; j < SIZE; j++)
			{
//				double currentScore2 = simulate(board, j).score;
				arrayClone(currentBoard, simSave.get(i).newboard);
				double currentScore = simulate(currentBoard, j).score;
				if (currentScore > maxScore)
					maxScore = currentScore;
			}
			
		}
		return maxScore;

	}

	@Override
	public String play(int[][] arg0)
	{

		// System.out.println(Smoothness.getValue(arg0));
		// trackGame(arg0);

		double l = 0.0, r = 0.0, u = 0.0, d = 0.0;
		Simulator sl = simulate(arg0, 0);
		l = sl.score;

		Simulator sr = simulate(arg0, 1);
		r = sr.score;

		Simulator su = simulate(arg0, 2);
		u = su.score;

		Simulator sd = simulate(arg0, 3);
		d = sd.score;

		final int Depth = 1;
		if (l > 0)
			// l = simulateMax(sl.newboard);
		{
			l = simulateDepth(sl.newboard, Depth);
//			System.out.println(simulateMax(arg0) + " " + l);
		}
			if (r > 0)
			r = simulateDepth(sr.newboard, Depth);

		if (u > 0)
			u = simulateDepth(su.newboard, Depth);

		if (d > 0)
			d = simulateDepth(sd.newboard, Depth);

		// System.out.println("R val = " + r);
		// System.out.println("L val = " +l);
		// System.out.println("U val = " +u);
		// System.out.println("D val = " +d);
		if (r >= l && r >= u && r >= d)
			return "R";
		if (r <= l && l >= u && l >= d)
			return "L";
		if (u >= l && r <= u && u >= d)
			return "U";
		return "D";

	}

	private boolean isLineFull(int[] line)
	{
		for (short i = 0; i < WIDTH; i++)
		{
			if (line[i] == 0)
				return false;
		}
		return true;
	}

	private short lineNIsNotFull(int[][] arg0)
	{
		short i = 0;
		while (i < WIDTH)
		{
			if (!isLineFull(arg0[i]))
				break;
			i++;
		}
		return i;
	}

	private boolean isUpMergeAble(int[][] arg0)
	{
		short i = lineNIsNotFull(arg0);

		for (short j = 0; j < HEIGHT && i != 0; j++)
			if (arg0[i][j] == arg0[i - 1][j])
				return true;
		return false;
	}

	private boolean isUpLeftAscend(int[][] arg0)
	{

		if (isAscend(arg0[0][0], arg0[0][1])
				&& isAscend(arg0[0][1], arg0[0][2])
				&& isAscend(arg0[0][2], arg0[0][3]) && !isLineEmpty(arg0[0]))
			return true;
		return false;
	}

	private boolean isUpLeftDescend(int[][] arg0)
	{
		if (isDescend(arg0[0][0], arg0[0][1])
				&& isDescend(arg0[0][1], arg0[0][2])
				&& isDescend(arg0[0][2], arg0[0][3]) && !isLineEmpty(arg0[0]))
			return true;
		return false;
	}

	private boolean isLineEmpty(int[] line)
	{
		for (short i = 0; i < WIDTH; i++)
		{
			if (line[i] != 0)
				return false;
		}

		return true;
	}

	private boolean isAscend(int bigger, int smaller)
	{
		if (smaller == 0 || bigger <= smaller)
			return true;
		return false;
	}

	// final speed test
	private boolean isDescend(int bigger, int smaller)
	{
		if (smaller == 0 || bigger >= smaller)
			return true;
		return false;
	}

	private boolean upAble(int[][] arg0)
	{
		boolean flag = false;
		for (int i = 0; i <= 3; i++)
		{
			flag = false;
			for (int j = 0; j <= 3; j++)
			{
				// find the first zero
				if (!flag && arg0[j][i] == 0)
					flag = true;
				else if (flag && arg0[j][i] != 0)
					return true;
			}
		}
		return false;
	}

	private boolean downAble(int[][] arg0)
	{
		boolean flag = false;
		for (int i = 3; i >= 0; i--)
		{
			flag = false;
			for (int j = 3; j >= 0; j--)
			{
				// find the first zero
				if (!flag && arg0[j][i] == 0)
					flag = true;
				else if (flag && arg0[j][i] != 0)
					return true;
			}
		}
		return false;
	}

	private boolean leftAble(int[][] arg0)
	{
		boolean flag = false;
		for (int i = 0; i <= 3; i++)
		{
			flag = false;
			for (int j = 0; j <= 3; j++)
			{
				// find the first zero
				if (!flag && arg0[i][j] == 0)
					flag = true;
				else if (flag && arg0[i][j] != 0)
					return true;
			}
		}
		return false;
	}

	private boolean rightAble(int[][] arg0)
	{
		boolean flag = false;
		for (int i = 3; i >= 0; i--)
		{
			flag = false;
			for (int j = 3; j >= 0; j--)
			{
				// find the first zero
				if (!flag && arg0[i][j] == 0)
					flag = true;
				else if (flag && arg0[i][j] != 0)
					return true;
			}
		}
		return false;
	}

}
