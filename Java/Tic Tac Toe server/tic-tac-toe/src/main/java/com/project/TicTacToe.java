package com.project;

public class TicTacToe {
	private char[][] board;
	private char currentPlayer;
	private boolean gameEnded;
	private boolean gameStarted;
	private String gameLog;
	private String endMessage;

	/**
	 * Instantiate a new game class with its board
	 */
	public TicTacToe(int roomId) {
		board = new char[3][3];
		currentPlayer = 'X';
		gameEnded = false;
		gameStarted = false;
		gameLog = "GameStarted in room " + roomId + "\n";
		initializeBoard();
	}

	/**
	 * Will check if a game has already started and try to start it. Used as a check to not join a game twice.
	 * @return
	 */
	public boolean startGame() {
		if (gameStarted) {
			return false;
		} else {
			gameStarted = true;
			return true;
		}
	}

	/**
	 * fills the board with '-' chars in order to empty it. Must be done at the
	 * beginning of the run
	 */
	public void initializeBoard() {
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				board[row][col] = '-';
			}
		}
	}

	/**
	 * Checks if the has reached an end
	 * 
	 * @return: boolean
	 */
	public boolean isGameEnded() {
		return gameEnded;
	}

	public String getEndMessage() {
		return this.endMessage;
	}

	/**
	 * Returns the current player
	 * 
	 * @return: char
	 */
	public char getCurrentPlayer() {
		return currentPlayer;
	}

	public char[][] getBoard() {
		return board;
	}

	public String getBoardString() {
		String result;
		result = String.format("%c%c%c%c%c%c%c%c%c", board[0][0], board[0][1], board[0][2], board[1][0], board[1][1],
				board[1][2], board[2][0], board[2][1], board[2][2]);
		return result;
	}

	/**
	 * Makes the move defined by the row and col parameters. After the move is made,
	 * checks if the game has ended
	 * 
	 * @param row
	 * @param col
	 */
	public String makeMove(int row, int col) {
		if (board[row][col] == '-' && !gameEnded) {
			board[row][col] = currentPlayer;
			gameLog += currentPlayer + ":" + row + " " + col + "\n";
			if (checkWin(row, col)) {
				gameEnded = true;
				gameLog += "Player" + currentPlayer + " wins!\n\n";
				endMessage = "Player" + currentPlayer + " wins!";
				return "Player" + currentPlayer + " wins!\n\n";
			} else if (checkDraw()) {
				gameEnded = true;
				gameLog += "Draw!\n\n";
				endMessage = "Draw!";
				return "Draw!";
			} else {
				currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
				return currentPlayer + " is next.";
			}
		}
		return "Error";
	}

	/**
	 * Checks if a move has won the game
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean checkWin(int row, int col) {
		// Check row
		if (board[row][0] == board[row][1] && board[row][1] == board[row][2] && board[row][0] != '-') {
			return true;
		}
		// Check column
		if (board[0][col] == board[1][col] && board[1][col] == board[2][col] && board[0][col] != '-') {
			return true;
		}
		// Check diagonals
		if (row == col || row + col == 2) {
			if ((board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != '-')
					|| (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != '-')) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the game has ended in a draw (all the board is filled with x and
	 * 0). Does not check for a win. That is done in CheckWin method
	 * 
	 * @return
	 */
	public boolean checkDraw() {
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				if (board[row][col] == '-') {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Saves Game moves to disk in txt file.
	 * @return
	 */
	public boolean SaveData() {
		return ThreadSafeFileWriter.saveStringToFile(gameLog);
	}
}
