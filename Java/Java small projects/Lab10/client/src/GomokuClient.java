import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GomokuClient {

    public static void main(String[] args) throws IOException {
        String serverAddress = "127.0.0.1"; // The server's IP address
        int PORT = 8100; // The server's port

        try {
            ServerCommunicator serverCommunicator = new ServerCommunicator(serverAddress, PORT);
            GameVisuals gameVisuals = new GameVisuals(Color.BLACK, 15, serverCommunicator);
            gameVisuals.startGUI();

            while(gameVisuals.serverCommunicator.isConnected == false){
                Thread.sleep(5);
            }

            String response = serverCommunicator.receive();
            while (response != null) {
                if (response.startsWith("opponent")) {
                    String[] log = response.split(" "); //splits the command to more usable syntax
                    System.out.println(response);
                    int ox = Integer.parseInt(log[1]);
                    int oy = Integer.parseInt(log[2]);
                    gameVisuals.board.notEmpty[ox][oy] = true;
                    gameVisuals.board.blackOrWhite[ox][oy] = true;
                    gameVisuals.redraw();
                }
                if (response.startsWith("You")) {
                    System.out.println(response);
                    gameVisuals.end(response);
                    Thread.sleep(4000);
                }
                if (response.startsWith("Hello")) {
                    System.out.println(response);
                    serverCommunicator.sendName("Ready");
                }
                if (response.equals("Bad")) {
                    JFrame bad = new JFrame();
                    bad.add(new Label("Bad room id"));
                    bad.setVisible(true);
                    gameVisuals.serverCommunicator.isConnected = false;
                    while(gameVisuals.serverCommunicator.isConnected == false){
                        Thread.sleep(5);
                    }
                }

                response = serverCommunicator.receive();
            }

            if (response == null) {
                gameVisuals.stopGUI();
                System.out.println("Bye");
                return;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}