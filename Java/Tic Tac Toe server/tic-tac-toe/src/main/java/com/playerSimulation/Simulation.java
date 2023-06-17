package com.playerSimulation;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Simulation {
	private static final int NUM_GAMES = 10;

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(NUM_GAMES);

		for (int i = 0; i < NUM_GAMES; i++) {

			executor.execute(() -> {
				try {
					SimulatedGame game = new SimulatedGame();
					game.simulate();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}

		executor.awaitTermination(100000, TimeUnit.SECONDS);
	}
}
