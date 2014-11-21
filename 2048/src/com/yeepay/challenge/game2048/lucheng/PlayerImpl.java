package com.yeepay.challenge.game2048.lucheng;

import com.yeepay.challenge.game2048.GamePlayer;
import com.yeepay.challenge.game2048.GameConsole;

public class PlayerImpl implements GamePlayer
{

	private final String LEFT = "L";
	private final String RIGHT = "R";
	private final String UP = "U";
	private final String DOWN = "D";

	public PlayerImpl()
	{
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args)
	{
		int result = 0;
		for (int i = 0; i < 100000; i++)
		{
			result += GameConsole.start(PlayerImpl.class);
			
		}
		System.out.println(result / 100000);
	}

	@Override
	public String play(int[][] arg0)
	{
		if (leftAble(arg0))
			return LEFT;
		if (rightAble(arg0))
			return RIGHT;
		if (upAble(arg0))
			return UP;

		return DOWN;

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
