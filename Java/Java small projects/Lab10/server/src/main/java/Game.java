import com.jcraft.jsch.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Game extends Thread {


    private String gameId;
    private Board board;
    private int size;
    private boolean turn;
    private String SGF;
    private Player p1, p2;
    private static HttpURLConnection con;



    public Game(int size) {
        this.board = new Board(size);
        this.size = size;
        this.SGF = "";
    }


    public void run() {

        System.out.println("starting game " + this.gameId);
        p1.setColor(false);//black
        p2.setColor(true);//white

        p1.setTurn(true);//it's the black player's turn to move
        p2.setTurn(false);

        p1.setGameStarted(true);
        p2.setGameStarted(true);

        p1.start();
        p2.start();

        this.turn = true;

        while (this.board.gameEnded() == 0) {
            try {//thread issues solved
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            if (this.turn) {
                while (!this.p1.isMadeMove()) {
                    try {//thread issues solved
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                String move = p1.getMove();//after all that wait, get the move, split it and update board
                String[] log = move.split(" "); //splits the command to more usable syntax
                this.SGF += "AB:" + log[1] + "," + log[2] + "\n";
                int x = Integer.parseInt(log[1]);
                int y = Integer.parseInt(log[2]);

                this.board.putPiece(x, y, false);//update server board

                this.p2.setLastMove("opponent " + x + " " + y);
                this.p1.setMadeMove(false);
                this.p2.setTurn(true);
                this.turn = false;
            } else {
                while (!this.p2.isMadeMove()) {
                    try {//thread issues solved
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                String move = p2.getMove(); //after all that wait, get the move, split it and update board
                String[] log = move.split(" "); //splits the command to more usable syntax
                this.SGF += "AW:" + log[1] + "," + log[2] + "\n";
                int x = Integer.parseInt(log[1]);
                int y = Integer.parseInt(log[2]);
                this.board.putPiece(x, y, true);//update server board

                this.p1.setLastMove("opponent " + x + " " + y);
                this.p2.setMadeMove(false);
                this.p1.setTurn(true);
                this.turn = true;
            }
        }
        this.p1.setTurn(false);
        this.p2.setTurn(false);


        if (this.board.gameEnded() == 1) {
            //white won
            p1.setWinnerName("You lose");
            p2.setWinnerName("You win");
        } else {
            //black won
            p2.setWinnerName("You lose");
            p1.setWinnerName("You win");
        }

        p1.setGameEnded(true);
        p2.setGameEnded(true);

        try {
            Thread.sleep(2000);
            p1.getSocket().close();
            p2.getSocket().close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(gameId + " a fost terminat. \n" + SGF);
        upload(this.SGF, this.gameId);
        uploadRest();
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public String getSGF() {
        return SGF;
    }

    public void setSGF(String SGF) {
        this.SGF = SGF;
    }

    public Player getP1() {
        return p1;
    }

    public void setP1(Player p1) {
        this.p1 = p1;
    }

    public Player getP2() {
        return p2;
    }

    public void setP2(Player p2) {
        this.p2 = p2;
    }

    public void upload(String SGF, String name) {
        try {
            File myObj = new File(name + ".txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                //System.out.println("File already exists.");
            }
            FileWriter myWriter = new FileWriter(name +".txt");
            myWriter.write(SGF);
            myWriter.close();


            JSch jsch = new JSch();
            Session session = jsch.getSession("stefan.stafie", "fenrir.info.uaic.ro");
            session.setPassword("Password9");

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");

            sftpChannel.connect();
            sftpChannel.put(name + ".txt", "/fenrir/studs/stefan.stafie/html");
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void uploadRest(){
        var url = "http://localhost:8080/rest/game";
        var urlParameters = "jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwicGxheWVyMSI6MTEsInBsYXllcjIiOjEwLCJjb250ZW50IjoiQUI6MTEsNlxuQVc6MTIsM1xuQUI6MTEsNVxuQVc6MTIsNFxuQUI6MTEsNFxuQVc6MTIsNVxuQUI6MTEsN1xuQVc6MTIsMlxuQUI6MTEsOCIsInJlc3VsdCI6IndoaXRlIHdvbiIsImlhdCI6MTUxNjIzOTAyMn0.h2sRzFg7z67SkZOFX1tneVB1S4w_zMQUFYEBaSsfo2Q";
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Java client");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (var wr = new DataOutputStream(con.getOutputStream())) {

                wr.write(postData);
            }

            StringBuilder content;

            try (var br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            System.out.println(content.toString());

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            con.disconnect();
        }
    }
}

