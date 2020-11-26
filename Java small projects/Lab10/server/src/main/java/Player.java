import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Player extends Thread {
    private Socket socket;
    private boolean color;
    private boolean gameStarted = false;
    private boolean gameEnded = false;
    private boolean turn = false;
    private boolean madeMove = false;
    private String move;
    private String playerName;
    private String winnerName;
    private String lastMove;

    public Player(Socket socket, boolean color) {
        this.socket = socket;
        this.color = color;
    }

    public void run() {
        try {
            PrintWriter outt = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            if (gameStarted) {
                outt.println("Hello! Get ready to play");
                outt.flush();

                String request = in.readLine();
                System.out.println(in + " is " + request);
                this.setPlayerName(request);


                while (!gameEnded) {
                    try {//thread issues solved
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (this.turn) {
                        outt.println(lastMove);
                        outt.flush();

                        this.move = in.readLine();
                        this.madeMove = true; // server knows the move has been made
                        this.turn = false; //it's not this player's turn anymore
                    }
                }

                outt.println(this.winnerName);
                outt.flush();
            }
        } catch (IOException e) {
            System.err.println("Communication error... " + e);
        } finally {
            try {
                socket.close(); // or use try-with-resources
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }


    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public boolean isColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public boolean isMadeMove() {
        return madeMove;
    }

    public void setMadeMove(boolean madeMove) {
        this.madeMove = madeMove;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getLastMove() {
        return lastMove;
    }

    public void setLastMove(String lastMove) {
        this.lastMove = lastMove;
    }
}