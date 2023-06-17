package com.playerSimulation;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Player {
	private String apiUrl = "http://localhost:8080/api";
	private int roomId;
	private String player;

	public Player(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public Player() {
	}

	public Player(int roomId) {
		this.roomId = roomId;
	}

	public int getRoomId() {
		return this.roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public void createRoom() throws IOException {
		String endpointUrl = apiUrl + "/Room";
		HttpURLConnection connection = createConnection(endpointUrl, "POST");
		int responseCode = connection.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				StringBuilder response = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}

				this.roomId = Integer.parseInt(JsonHelper.extractFromResponse(response.toString(), "roomId"));
				player = "X";
			}
		} else {
			System.out.println("Failed to create room. Response code: " + responseCode);
		}

		connection.disconnect();
	}

	public boolean playMove(int col, int row) throws IOException {
		String endpointUrl = apiUrl + "/Room/" + roomId + "/move?player=" + player + "&row=" + row + "&col=" + col;
		HttpURLConnection connection = createConnection(endpointUrl, "POST");

		int responseCode = connection.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) {
			System.out.println("Move played successfully");
			connection.disconnect();
			return true;

		} else {
			System.out.println("Failed to play move. Response code: " + responseCode);
			connection.disconnect();
		}

		return true;
	}

	public String playRandomMove(String board) throws IOException {

		while (true) {
			int[] randomNumbers = RandomNumberGenerator.getRandomRowCol();
			int row = randomNumbers[0];
			int col = randomNumbers[1];
			boolean isValidMove = false;

			do {
				if (board.charAt(row * 3 + col) == '-') {
					isValidMove = true;
				}

				randomNumbers = RandomNumberGenerator.getRandomRowCol();
				row = randomNumbers[0];
				col = randomNumbers[1];
			} while (!isValidMove);

			String endpointUrl = apiUrl + "/Room/" + roomId + "/move?player=" + player + "&row=" + row + "&col=" + col;
			HttpURLConnection connection = createConnection(endpointUrl, "POST");

			int responseCode = connection.getResponseCode();
			String responseBoard = "";

			if (responseCode == HttpURLConnection.HTTP_OK) {
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}

					if (response.toString().contains("isEnd")) {
						return "end";
					}

					responseBoard = JsonHelper.extractFromResponse(response.toString(), "board");
					return responseBoard;
				}
			} else {
				return board;

			}

		}
	}

	public void joinRoom() throws IOException {
		String endpointUrl = apiUrl + "/Room?id=" + roomId;
		HttpURLConnection connection = createConnection(endpointUrl, "GET");

		int responseCode = connection.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) {
			player = "O";
		} else {
			System.out.println("Failed to join room. Response code: " + responseCode + " roomid: " + roomId);
		}

		connection.disconnect();
	}

	private HttpURLConnection createConnection(String endpointUrl, String requestMethod) throws IOException {
		URL url = new URL(endpointUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(requestMethod);
		connection.setDoOutput(true);
		return connection;
	}
}
