import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class GomokuServer {
    private Map<String, Game> games = new HashMap<>();
    private int SIZE = 15;  // all games have this size
    // Define the port on which the server is listening

    public static final int PORT = 8100;

    public GomokuServer() throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                System.out.println("Waiting for a client ...");
                Socket socket = serverSocket.accept();

                // Execute the client's request in a new thread
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                String request = in.readLine();

                //if gets "create <id>" from client
                if (request.startsWith("create")) {
                    String id = request.split(" ")[1];
                    Game game = new Game(this.SIZE);
                    game.setGameId(id);
                    Player player = new Player(socket, false); //create black player
                    game.setP1(player);
                    if (games.get(id) == null) {
                        games.put(id, game);
                        System.out.println("Joc cu id = " + id + " a fost creat.");
                    } else {
                        PrintWriter outt = new PrintWriter(socket.getOutputStream(), true);
                        outt.println("Bad"); outt.flush();
                        System.out.println("Jocul nu a putut fi creat. id = " + id);
                    }
                }

                //if gets "join <id>" from client
                if (request.startsWith("join")) {
                    String id = request.split(" ")[1];
                    Game game = games.get(id);
                    Player player = new Player(socket, true); //create white player
                    game.setP2(player);
                    game.start();
                    games.remove(id);
                }

                //if admin connects
                if (request.startsWith("admin")) {
                    String[] log = request.split(" "); //splits the command to more usable syntax
                    if(log[1].equals("root") && log[2].equals("myPass")){
                        if(log[3].equals("STOP")) {
                            PrintWriter outt = new PrintWriter(socket.getOutputStream(), true);
                            outt.println("Server stopped"); outt.flush();
                            break;
                        }
                        if(log[3].equals("RESTART")){
                            PrintWriter outt = new PrintWriter(socket.getOutputStream(), true);
                            outt.println("Server restarted"); outt.flush();
                            Thread.sleep(1000);
                            serverSocket.close();
                            serverSocket = new ServerSocket(PORT);
                        }
                    } else {
                        PrintWriter outt = new PrintWriter(socket.getOutputStream(), true);
                        outt.println("incorrect login or password"); outt.flush();
                    }
                }

            }
        } catch (IOException e) {
            System.err.println("Ooops... " + e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            serverSocket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        GomokuServer server = new GomokuServer();
    }
}