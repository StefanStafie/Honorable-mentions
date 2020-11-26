#### Advanced-Programming-Java

# 1. The (Elder) Knapsack Problem
Write an application that can model and solve the knapsack problem.
Consider a knapsack of a given capacity and a set of items that could be added in the knapsack, for example books, food, weapons, etc. Each item has at least the following properties: a name, a weight and a value.
The name of a weapon is actually its type, described by an enum. The weight of a book is computed as the number of pages divided by 100. The value of a food is computed as its weight multiplied by 2.
The profit factor of an item is defined as value / weight.
The problem is to determine what items to include in the knapsack such that:

- the total weight of the selected items is less than or equal to the capacity and
- the total value is as large as possible.

Example:
  
    capacity of the knapsack = 10
    available items:
     book1: Dragon Rising, weight = 3, value = 5 (profit factor = 1.66)
     book2: A Blade in the Dark, weight = 3, value = 5
     food1: Cabbage, weight = 2, value = 4 (profit factor = 2)
     food2: Rabbit, weight = 2, value = 4
     weapon: Sword, weight = 5, value = 10 (profit factor = 2)
    selected items: weapon, book1, food1 (total weight=10, total value=19)
## The main specifications of the application are:

- Create an object-oriented model of the problem. You should have at least the following: the interface Item, and the classes Book, Food, Weapon, Knapsack.
- The classes Book, Food and Weapon should implement the interface Item. The interface should have at least one default method, for example profitFactor.
- Create and print on the screen the instance of the problem described in the previous example. When printing the content of the knapsack, the items must be ordered according to their name (the natural order).
- Create the interface Algorithm and two classes that implement algorithms for solving the knapsack problem: a greedy algorithm and one using dynamic programming.
- Each algorithm will receive an empty knapsack and a list of available items.
- Create a random knapsack problem generator and analyse the differences between running times and solution qualities for both algorithms.
**10000 iterations on each test**

pairs: dynamic--greedy
capacity 500, itemCount 500 : 

	avg value:  	2501.794638376264  2470.371865336172
   	avg time:     	6.3644  0.2276

capacity 50, itemCount 50:

    avg value 	235.71401113725702  208.18711464508496
	avg time: 	0.0735  0.0192

Capacity 1000, itemCount 15: 	

    avg value  	787.7506059869218  787.7501364038869
	avg time  	0.5299  0.0053

Capacity 15, itemCount 1000:
        
 	avg value	488.1674715302053  465.9094789124492
    avg time	0.3442  0.3938

# 2. The Hospitals/Residents Problem (HR)
An instance of HR involves a set of residents and a set f hospitals, each resident seeking a post at one hospital, and each hospital having a number of available posts (its capacity). Each resident ranks some (acceptable) hospitals in strict order, and each hospital ranks its applicants in strict order. A matching is a set of pairs (resident, hospital) such that each resident is assigned to at most one hospital and the capacities of the hospitals are not exceeded. A matching is stable if there is no pair (r, h) such that r is assigned to h' but r prefers h better than h' and h prefers r better than some of its assigned residents. We consider the problem of creating a stable matching between residents and hospitals.

Example:
        
    4 residents R0,R1,R2, 3 hospitals H0,H1,H2, capacity(H0)=1, capacity(H1)=2, capacity(H2)=2.
    Residents preferences
    R0: (H0, H1, H2)
    R1: (H0, H1, H2)
    R2: (H0, H1)
    R3: (H0, H2)
    Hospitals preferences
    H0: (R3, R0, R1, R2)
    H1: (R0, R2, R1)
    H2: (R0, R1, R3)
A solution for this example might be: [(R0:H1),(R1:H2),(R2:H1),(R3:H0)]


## The main specifications of the application are:
- Create an object-oriented model of the problem. You should have at least the following classes: Resident, Hospital, Main.
- Create all the objects in the example (one by one or using streams).
- Create a list of residents, using an ArrayList implementation. Sort the residents, using a comparator.
- Create a set of hospitals, using a TreeSet implementation. Make sure that Hospital objects are comparable.
- Create two maps (having different implementations) describing the residents and the hospital preferences and print them on the screen.
Using Java Stream API, write queries that display the residents who find acceptable H0 and H2, and the hospitals that have R0 as their top preference.
- Create a class that describes the HR Problem (or any stable matching problem!). Consider creating the classes Problem, Partition, Element, Matching.
- Implement an algorithm for creating a matching.
- Use a third-party library in order to generate random fake names for residents and hospitals. Create random instances and test your algorithm.
Verify if the matching produced by your algorithm is stable.
-The first part can be viewed by uncommenting "//Compulsory compulsory = new Compulsory();" in Main
- Problems are generated using ProblemGenerator. The number of hospitals and residents to be generated are given as parameters. The names are generated using Faker class (https://github.com/faker-ruby/faker)
- A possible Matching is created using Algorithm on a Problem

- For checking the corectness of Matching, use CheckStable;


# 3. Document Management System
Write an application that can manage a catalog of documents. An entry in this catalog might be an article, a book, etc.
A document may be located using a path in the local file system or a link to an external URL. Each document has a unique ID, a name and may have additional tags, which are pairs name-value. Example of tags may be title, author, year, publishingDate, etc.
## The main specifications of the application are:
- Create an object-oriented model of the problem. You should have at least the following classes: Catalog, Document. Consider creating a class responsible with external operations regarding a catalog.
- Implement the following methods:

        save: saves the catalog to an external file, using object serialization;
        load: loads the catalog from an external file.
        view: opens a document using the native operating system application (see the Desktop class);
- Implement the save and load methods using a plain text representation of the catalog (instead of binary serialization).
- Create a shell that allows reading commands from the keyboard, together with their arguments and implement the commands load, list, view.
- Represent the commands using classes instead of methods (create the classes LoadCommand, ListCommand, etc.). Use an interface or an abstract class in order to desribe a generic command.
- Implement the command report html: create an HTML report representing the content of the catalog.
- The application will signal invalid data (duplicate names, invalid paths or URLs, etc.) or invalid commands using custom exceptions.
The final form of the application will be an executable JAR archive.   

- Identify the generated archive and launch the application from the console, using the JAR.

# 4. Drawing
Create an application with graphical user interface for creating images. You may use either Swing or JavaFX.

## The main specifications of the application are:
Create the following components:

- The main frame of the application.
- A configuration panel for introducing parameters regarding the shapes that will be drawn: the size, the number of sides, the stroke, etc.
- A canvas (drawing panel) for drawing various types of shapes: circles, squares, regular polygons, snow flakes, etc. You must implement at least one shape type. This panel must be placed in the center part of the frame.
- When the users executes mouse pressed operation, a shape must be drawn at the mouse location. You must use the properties defined in the configuration panel (at least one) and generate random values for others (color, etc.).
- A control panel for managing the image being created. This panel will contains the buttons: Load, Save, Reset, Exit and it will be placed at the bottom part of the frame.
- Add support for drawing multiple types of components. Consider creating a new panel, containing a list of available shapes. The configuration panel must adapt according to the type of the selected shape. Implement at least two types of shapes.
- Use a file chooser in order to specify the file where the image will be saved (or load).
- Implement the retained mode drawing and add support for deleting shapes.
- Support for 3 Shapes (Rectangle, Circle, RegularPolygon).
- The configuration panel adapts to the current Shape.
- Support for 3 colors (Red, Green, Blue).
- Support for saving and loading the image to disk using a file chooser.
- Support for reset and exit.
- To refresh canvas and configPanel, click on canvas after every change.

# 5. Concurrency
Write a program that simulates a positional game, such as the arithmetic progression game, between a given number of players.At the beginning of the game the board contains n tokens, each token having a distinct value from 1 to m. Each player extracts tokens successively from the board and must create with them a a complete arithmetic progression of a given sizek.
The game ends when either a player makes a complete arithmetic progression (in this case the winner receives n points and the others 0) or when all tokens have been removed from the board (in this case each player receives a number of points equal to the their largest arithmetic progression).
The players might take turns (or not...) and a time limit might be imposed (or not...).
## The main specifications of the application are:
- Create the class Token. An instance of this class will hold a number from 1 to m. Consider the case when a token may be blank, meaning that it can take the place of any number.
- Create the class Board. An instance of this class will contain n tokens (you may consider the numbers from 1 to n).
- Create the class Player. Each player will have a name. This class will implement the Runnable interface. In the run method the player will repeatedly extract one token from the board.
- Create the Game class. Simulate the game using a thread for each player.
Pay attention to the synchronization of the threads when extracting tokens from the board.
- Make sure that players wait their turns, using a wait-notify approach.
Implement a timekeeper thread that runs concurrently with the player threads, as a daemon. This thread will display the running time of the game and it will stop the game if it exceeds a certain time limit. Try it using larger values for n.
- Consider the situation when each player might have a different strategy for extracting a number: automated (random or smart) or manual.
Player should be an abstract class having as subclasses: RandomPlayer, SmartPlayer, ManualPlayer.
- A "smart" player should try to extend its largest arithmetic progression, while not allowing others to extend theirs. A manual player will use the keyboard. Implement all three strategies.

- The game plays itself in initialisation ( new Game(playerCount, tokenCount, progressionSize); )
- A thread is created for each Player.
- Each player takes progressionSize tokens from the Board (using getToken()). Then, he exchanges tokens (1 player token with 1 board token, using exchangeToken() ) until the player has a progression.
- Both getToken() and exchangeToken() are synchronised methods so that the players don't clash when they take tokens. After a token is taken from the board, it is removed from the list of tokens available in the board.
- A player may only keep progressionSize Tokens in hand.
- When a player obtains a progression, the main thread stops all other player threads (killing it softly) and prints the winner.

# 6. JDBC
Write an application that allows to connect to a relational database by using JDBC, submit SQL statements and display the results.
## The main specifications of the application are:
- Create a database having the name MusicAlbums using any RDBMS (Oracle, Postgres, MySql, Java DB, etc.)
- Create a user with the name dba and the password sql
- Create the following tables (this example uses Java DB SQL Dialect):

        create table artists(
            id integer not null generated always as identity (start with 1, increment by 1),
            name varchar(100) not null,
            country varchar(100),
            primary key (id)
        );
        create table albums(
            id integer not null generated always as identity (start with 1, increment by 1),
            name varchar(100) not null,
            artist_id integer not null references artists on delete restrict,
            release_year integer,
            primary key (id)
        );

- Add the database driver to the project libraries.
- Create the singleton class Database that manages a connection to the database.
- Create the DAO class ArtistController, having the methods create(String name, String country) and findByName(String name).
- Create the DAO class AlbumController, having the methods create(String name, int artistId, int releaseYear) and findByArtist(int artistId).
Implement a simple test using your classes.
- Create the necessary table(s) in order to store charts in the database (a chart contains some albums in a specific order).
- Create an object-oriented model of the data managed by the Java application. You should have the classes Artist, Album, Chart.
- Generate random data and insert it into the database. You may consider using a fake data generator. (Or you may import real data ...)
- Display the ranking of the artists, considering their positions in the charts.
- The database is created using MySql and is hosted by localhost phpmyadmin. To use:
    - choice 1: copy the musicalalbums folder to your C:\xampp\mysql\data
    - choice 2: use musicalalbums.sql to create the database on your system.
 
- The Test class inserts 3 rows in artist table and 3 rows in album table using create(). Then uses findByName() and findByArtist() to return a result set and and print on screen.
- ArtistController, having the methods create(String name, String country) and findByName(String name):return ResultSet
- AlbumController, having the methods create(String name, int artistId, int releaseYear) and findByArtist(int artistId):return ResultSet
- Database class is a Singleton. Has Statement and Connection variables.
- Created tables 'charts' (id, title)and 'chart_record' (id, chart_id, album_id) for storing charts.
- Created classes Artist, Album, Chart and ChartController.
- Used a fake data generator.
- Implemented a ranking based on number of points (might be interpreted as views). More points bring the artist higher in ranking.
- Created a method for finding the ranking of artists based on all their albums that appear in all charts. 
- Test class generates 20 artists, 100 albums and 10 charts with 10 albums each. Then displays the ranking of the artists based on their points.


# 7. REST Services
Continue the application created at the previous lab integrating the following functionalities:
Create a database in order to store the games. A game must contain information about the players, a text representing the content of the game, the date and the result. A player must have at least a name.
Implement REST services needed to comunicate with the database (CRUD).
## The main specifications of the application are:
- Create a Spring Boot project containing the REST services for comunicating with the database.
- Create the classes Game and Player.
- Create a REST controller containing methods for:
obtaining the list of the players, via a HTTP GET request.
adding a new player in the database, via a HTTP POST request.
modifying the name of a player, via a HTTP PUT request.
deleting a player, via a HTTP DELETE request.
- Test your services using the browser and/or Postman.
- Create REST services for inserting and reading games.
- Integrate the services into your previous project, invoking them using the support offered by Spring Boot.
- Handle the exceptions using a RestControllerAdvice.
- Secure the communication using the HTTPS protocol.
- Create REST services for inserting and reading games.
- Integrate the services into your previous project. (History of games from client. Inserting games into database from server)
- Handle the exceptions using a RestControllerAdvice.
- Secure services with JSON Web Tokens.

# 8. Dynamic Swing Designer
Create an application to assist the user in building a Swing graphical user interface.
The purpose is to visually design a JPanel container using absolute positioning of its components.
## The main specifications of the application are:
- Create the class MainFrame of type JFrame, that will also represent the main class of the application. The frame will contain a ControlPanel in the north and a DesignPanel in the center.
- The ControlPanel will allow the user to specify any class name of a Swing component (such as javax.swing.JButton, javax.swing.JLabel, etc.), a default text for that component (if applicable) and a button for creating and adding an instance of the specified component to the DesignPanel.
- The DesignPanel represents a simple JPanel, using absolute positioning of its components and containing Swing components added by our application.


