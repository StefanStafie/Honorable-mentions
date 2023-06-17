package com.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class GameController {
	public static void main(String[] args) {
		SpringApplication.run(GameController.class, args);
	}
}

@RestController
@RequestMapping("/api")
class ApiController {
	private static final Object lock = new Object();
	Map<Integer, TicTacToe> gameDictionary = new HashMap<>();
	int roomCounter = 0;

	/**
	 * Creates a room with the unique id of the user. If the user already exists,
	 * returns error
	 * 
	 * @return
	 */
	@PostMapping("/Room")
	public ResponseEntity<Map<String, String>> CreateRoom() {
		synchronized (lock) {
			roomCounter++;

			TicTacToe game1 = new TicTacToe(roomCounter);
			gameDictionary.put(roomCounter, game1);

			Map<String, String> response = new HashMap<>();
			response.put("message", "Room created with ID: " + (roomCounter - 1));
			response.put("roomId", "" + (roomCounter - 1));

			return ResponseEntity.ok().body(response);
		}
	}

	@GetMapping("/Room")
	public ResponseEntity<Map<String, String>> JoinRoom(@RequestParam("id") int roomId) {
		Map<String, String> response = new HashMap<>();

		if (gameDictionary.containsKey(roomId)) {
			if (gameDictionary.get(roomId).startGame()) {
				response.put("message", "Room joined with ID: " + roomId);
				response.put("roomId", "" + roomId);
			} else {
				response.put("message", "Room already full " + (-1));
				response.put("roomId", "" + (-1));
				return ResponseEntity.status(HttpStatus.LOCKED).body(response);
			}
		} else {
			response.put("message", "Room was not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/Room/{id}/move")
	public ResponseEntity<Map<String, String>> PlayMove(@PathVariable("id") int roomId,
			@RequestParam("player") char player, @RequestParam("row") int row, @RequestParam("col") int col) {
		Map<String, String> response = new HashMap<>();

		if (!gameDictionary.containsKey(roomId)) {
			response.put("message", "Room was not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

		TicTacToe game = gameDictionary.get(roomId);
		if (game.getCurrentPlayer() != player) {
			response.put("message", "It's not your turn");
			return ResponseEntity.badRequest().body(response);
		}

		String result = game.makeMove(row, col);
		if (result.startsWith("Draw") || result.startsWith("Player")) {
			game.SaveData();
			response.put("isEnd", "true");
		}

		response.put("message", result);
		response.put("board", game.getBoardString());
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/Room/{id}/move")
	public ResponseEntity<Map<String, String>> WaitOpponentMove(@PathVariable("id") int roomId,
			@RequestParam("player") char player) {
		Map<String, String> response = new HashMap<>();

		if (!gameDictionary.containsKey(roomId)) {
			response.put("message", "Room was not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

		TicTacToe game = gameDictionary.get(roomId);
		int retryCount = 100;
		while (game.getCurrentPlayer() != player && retryCount > 0 && !game.isGameEnded()) {
			retryCount--;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		response.put("message", "waitSuccess");
		response.put("board", game.getBoardString());
		if (game.isGameEnded()) {
			response.put("isEnd", "true");
			response.put("message", game.getEndMessage());
		}
		return ResponseEntity.ok().body(response);
	}
}