# Gomoku
Create an implementation of Gomoku (Five in a row) that allows remote players to start or join games. The application will contain two parts (create a project for each one):

    The server is responsible with the game management and mediating the players.

    The client will communicate with the server, sending it commands such as:
        create or join a game,
        submit a move, etc.
## The main specifications of the application are:

- Create the project ServerApplication. This will contain (at least) the classes: GameServer and ClientThread.
- Create the class GameServer. An instance of this class will create a - ServerSocket running at a specified port. The server will receive requests (commands) from clients and it will execute them.
- Create the class ClientThread. An instance of this class will be responsible with communicating with a client Socket. If the server receives the command stop it will stop and will return to the client the respons "Server stopped", otherwise it return: "Server received the request ... ".
- Create the project ClientApplication. This will contain (at least) the class: GameClient.
- Create the class GameClient. An instance of this class will read commands from the keyboard and it will send them to the server. The client stops when it reads from the keyboard the string "exit".
- Implement functionalities of the game, using the classes Game, Board, Player, etc.
- The clients will send to the server commands such as: create game, join game, submit move, etc.
- The server is responsible with the game management and mediating the players.
- Once a game is finished, an HTML or SGF representation of the game should be uploaded to a Web server.
You may use JCraft for connecting to a server using SFTP and transferring a file (or a similar solution).

- Implemented functionalities of the game
- The clients send to the server commands such as: create game, join game, submit move, etc.
- The server is responsible with the game management and mediating the players.
- Once a game is finished a SGF representation of the game is uploaded to a Web server ( "http://students.info.uaic.ro/~stefan.stafie/" ) using JCraft
- Created a GUI for the game
- In server, a Thread is created for each room (game) and for each player (client), allowing multiple games to take place at once
- Client built as executable JAR. 
