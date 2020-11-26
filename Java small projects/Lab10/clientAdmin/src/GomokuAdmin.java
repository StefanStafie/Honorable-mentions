import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GomokuAdmin {

    public static void main(String[] args) throws IOException {
        String serverAddress = "127.0.0.1"; // The server's IP address
        int PORT = 8100; // The server's port
        try {
            Socket socket = new Socket(serverAddress, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            Scanner keyboard = new Scanner(System.in);
            System.out.println("enter a command");
            String command = keyboard.nextLine();

            out.println(command);
            System.out.println(in.readLine());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}