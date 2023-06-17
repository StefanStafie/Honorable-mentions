package com.playerSimulation;

import java.util.Random;

public class RandomNumberGenerator {
	public static int[] getRandomRowCol() {
		Random random = new Random();
		int[] numbers = new int[2];
		numbers[0] = random.nextInt(3);
		numbers[1] = random.nextInt(3);
		return numbers;
	}
}
