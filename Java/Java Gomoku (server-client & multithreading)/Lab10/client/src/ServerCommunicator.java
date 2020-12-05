import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerCommunicator {
    private String serverAddress;
    private int PORT;
    private String string;
    Socket socket;
    PrintWriter out;
    BufferedReader in;
    boolean isConnected = false;

    public ServerCommunicator(String serverAddress, int PORT) {
        this.serverAddress = serverAddress;
        this.PORT = PORT;
    }

    public void sendName(String name) {
        out.println(name);
        out.flush();
    }

    public void sendSubmit(int x, int y) {
        out.println("submit " + x + " " + y);
        out.flush();
    }

    public void sendCreate(int id) {
        if(isConnected){
            JFrame connected = new JFrame("Already connected");
            connected.add(new JLabel("\n\n\nYou are already in a room\n\n\n"));
            connected.setVisible(true);
            return;
        }
        createSocket();
        out.println("create " + id);
        out.flush();
    }

    private void createSocket() {
        try {
            this.socket = new Socket(serverAddress, PORT);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.isConnected = true;
    }

    public void sendJoin(int id) {
        if(isConnected){
            JFrame connected = new JFrame("Already connected");
            connected.add(new JLabel("\n\n\nYou are already in a room\n\n\n"));
            connected.setVisible(true);
            return;
        }
        createSocket();
        out.println("join " + id);
        out.flush();
    }

    public String receive() {
        try {
            return in.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
