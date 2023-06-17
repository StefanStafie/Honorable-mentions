package com.playerSimulation;

import java.io.IOException;

public class SimulatedGame {
	private Player player1;
	private Player player2;
	private String board = "---------";

	public SimulatedGame() throws IOException, InterruptedException {
		this.player1 = new Player();
		this.player2 = new Player();

		player1.createRoom();
		System.out.println("Game started: " + player1.getRoomId());
		Thread.sleep(1000);
		player2.setRoomId(player1.getRoomId());
		player2.joinRoom();
	}

	public void simulate() throws IOException, InterruptedException {

		while (!board.equals("end")) {
			board = player1.playRandomMove(board);

			if (!board.equals("end")) {
				board = player2.playRandomMove(board);
			}
		}

		System.out.println("Game  finished: " + player1.getRoomId());
	}
}
