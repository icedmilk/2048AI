package com.yeepay.challenge.game2048.lucheng.test;

import com.yeepay.challenge.game2048.GameConsole;
import com.yeepay.challenge.game2048.lucheng.PlayerImpl;

public class TestAI
{
	public static void main(String[] args)
	{
		final int runTime = 100;

		int resultSum = 0;

		long startTime = System.currentTimeMillis();
		for (int i = 0; i < runTime; i++)
		{
			resultSum += GameConsole.start(PlayerImpl.class);
		}
		long endTime = System.currentTimeMillis() - startTime;

		System.out.println("Avg Score:\t" + resultSum / runTime);
		System.out.println("Total Time:\t" + endTime / 1000f + " s");
		System.out
				.println("Average Time:\t" + endTime / 1000f / runTime + " s");
	}

}
