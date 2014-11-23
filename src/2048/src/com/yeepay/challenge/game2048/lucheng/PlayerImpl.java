package com.yeepay.challenge.game2048.lucheng;

import java.util.ArrayList;

import com.yeepay.challenge.game2048.GameBoard;
import com.yeepay.challenge.game2048.GamePlayer;

class Simulator
{
	public int[][] newboard;
	public double score;

	public Simulator()
	{
	}

	public Simulator(int[][] board, double score)
	{
		this.newboard = board;
		this.score = score;
	}
}

public class PlayerImpl implements GamePlayer
{
	private final String LEFT = "L";
	private final String RIGHT = "R";
	private final String UP = "U";
	private final String DOWN = "D";

	private final short HEIGHT = 4;
	private final short WIDTH = 4;
	private final short SIZE = 4;

	// search depth
	private final int setDepth = 2;

	public PlayerImpl()
	{

	}

	@Override
	/**
	 * main function
	 */
	public String play(int[][] arg0)
	{
		// trackGame(arg0);

		/**
		 * 0-left, 1-right, 2-up, 3-down
		 */

		// get first try
		Simulator[] simulator = new Simulator[SIZE];
		for (int i = 0; i < SIZE; i++)
		{
			simulator[i] = simulate(arg0, i);
		}

		// begin to traverse the branches which is depend on the depth
		for (int i = 0; i < simulator.length; i++)
		{
			if (simulator[i].score > 0)
				simulator[i].score = simulateDepth(simulator[i].newboard,
						setDepth);
		}

		//
		int maxIndex = arrayMaxIndex(simulator);
		switch (maxIndex)
		{
		case 0:
			return LEFT;
		case 1:
			return RIGHT;
		case 2:
			return UP;
		case 3:
			return DOWN;
		}
		return DOWN;
	}

	/**
	 * show the status of current board
	 * 
	 * @param board
	 */
	@SuppressWarnings("unused")
	private void showBoard(int[][] board)
	{
		for (int i = 0; i < WIDTH; i++)
		{
			for (int j = 0; j < HEIGHT; j++)
			{
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();

	}

	/**
	 * clone the array rather than quote
	 * 
	 * @param cloned
	 * @param inital
	 */
	public void arrayClone(int[][] cloned, int[][] inital)
	{
		for (int i = 0; i < WIDTH; i++)
			for (int j = 0; j < HEIGHT; j++)
				cloned[i][j] = inital[i][j];

	}

	/**
	 * simulate the operation of the given direction
	 * 
	 * @param board
	 * @param direction
	 * @return
	 */
	public Simulator simulate(int[][] board, int direction)
	{
		GameBoard gb = new GameBoard();
		int[][] currentArray = new int[WIDTH][HEIGHT];
		double score = 0.0;
		arrayClone(currentArray, board);

		gb.setNumbers(currentArray);
		switch (direction)
		{
		case 0:
			if (isMoveable(board, LEFT))
			{
				gb.action(LEFT);
				score = Evaluation.getEvaluation(currentArray);
			}
			break;

		case 1:
			if (isMoveable(board, RIGHT))
			{
				gb.action(RIGHT);
				score = Evaluation.getEvaluation(currentArray);
			}
			break;
		case 2:
			if (isMoveable(board, UP))
			{
				gb.action(UP);
				score = Evaluation.getEvaluation(currentArray);
			}
			break;
		default:
			if (isMoveable(board, DOWN))
			{
				gb.action(DOWN);
				score = Evaluation.getEvaluation(currentArray);
			}
			break;
		}
		int[][] newArray = new int[WIDTH][HEIGHT];
		arrayClone(newArray, gb.getNumbers());
		return new Simulator(newArray, score);
	}

	/**
	 * simulate by depth, O(4^n)
	 * 
	 * @param board
	 * @param depth
	 * @return
	 */
	public double simulateDepth(int[][] board, int depth)
	{
		double maxScore = 0.0;

		ArrayList<Simulator> simSave = new ArrayList<Simulator>();
		int[][] currentBoard = new int[SIZE][SIZE];
		arrayClone(currentBoard, board);
		simSave.add(new Simulator(currentBoard, depth));

		for (int i = 1; i <= depth; i++)
		{
			int currentSize = simSave.size();
			for (int j = 0; j < currentSize; j++)
				for (int k = 0; k < SIZE; k++)// direction
				{

					Simulator temp = simulate(simSave.get(j).newboard, k);
					if (temp.score > 0)
						simSave.add(temp);
				}

			for (int j = 0; j < currentSize; j++)
				simSave.remove(0);
		}

		for (int i = 0; i < simSave.size(); i++)
		{
			for (int j = 0; j < SIZE; j++)
			{
				// double currentScore2 = simulate(board, j).score;
				arrayClone(currentBoard, simSave.get(i).newboard);
				double currentScore = simulate(currentBoard, j).score;
				if (currentScore > maxScore)
					maxScore = currentScore;
			}

		}
		return maxScore;

	}

	/**
	 * get the index of max number
	 * 
	 * @param sim
	 * @return
	 */
	private int arrayMaxIndex(Simulator[] sim)
	{
		int maxIndex = 0;
		for (int i = 1; i < sim.length; i++)
		{
			if (sim[i].score >= sim[maxIndex].score)
			{
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	/**
	 * operation is feasible
	 * 
	 * @param arg0
	 * @param direction
	 * @return
	 */
	private boolean isMoveable(int[][] arg0, String direction)
	{
		// boolean flag = false;
		// for (int i = 0; i < WIDTH; i++)
		// {
		// flag = false;
		// for (int j = 0; j < HEIGHT; j++)
		// {
		// // find the first zero
		// if (!flag && arg0[i][j] == 0)
		// flag = true;
		// else if (flag && arg0[i][j] != 0)
		// return true;
		// }
		// }
		int[][] temp = new int[WIDTH][HEIGHT];
		arrayClone(temp, arg0);
		GameBoard gb = new GameBoard();
		gb.setNumbers(temp);
		switch (direction)
		{
		case LEFT:
			gb.left();
			break;
		case RIGHT:
			gb.right();
			break;
		case UP:
			gb.up();
			break;
		case DOWN:
			gb.down();
			break;
		default:
			return false;
		}
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				if (temp[i][j] != arg0[i][j])
					return true;
			}
		}

		return false;
	}
}
