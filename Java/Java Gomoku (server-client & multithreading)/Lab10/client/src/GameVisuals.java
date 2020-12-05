import java.awt.*;
import javax.swing.*;

public class GameVisuals extends JPanel {
    Board board;
    Color color;
    private static final int GAP = 3;
    JButton[][] grid;
    int size;
    ServerCommunicator serverCommunicator;
    JFrame frame;

    public GameVisuals(Color color, int size, ServerCommunicator serverCommunicator) {
        this.serverCommunicator = serverCommunicator;
        this.size = size;
        this.color = color;
        this.board = new Board(size);
        grid = new JButton[size][size];
        JPanel gamePanel = new JPanel(new GridLayout(size, size, GAP, GAP));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
        gamePanel.setBackground(Color.BLACK);
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                grid[row][col] = new JButton();
                grid[row][col].setBackground(Color.RED);
                grid[row][col].setOpaque(true);
                grid[row][col].addActionListener(new ButtonListener(this));
                gamePanel.add(grid[row][col]);
            }
        }

        JPanel bottomPanel = new JPanel();
        JButton history = new JButton("History");
        history.addActionListener(new HistoryListener(this));


        JButton submit = new JButton("Submit move");
        submit.addActionListener(new SubmitListener(this));

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));

        JButton create = new JButton("Create Game");
        create.addActionListener(new CreateListener(this, spinner));

        JButton join = new JButton("Join Game");
        join.addActionListener(new JoinListener(this, spinner));


        bottomPanel.add(history);
        bottomPanel.add(submit);
        bottomPanel.add(create);
        bottomPanel.add(join);
        bottomPanel.add(spinner);



        setLayout(new BorderLayout());
        add(gamePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.PAGE_END);
    }


    public void startGUI() {
        GameVisuals mainPanel = this;
        frame = new JFrame("Play the Game");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(mainPanel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public void stopGUI() {
        frame.dispose();
    }

    public void redraw() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (this.board.notEmpty[row][col]) {
                    if (this.board.blackOrWhite[row][col]) {
                        grid[row][col].setBackground(Color.WHITE);
                    } else {
                        grid[row][col].setBackground(Color.BLACK);
                    }
                } else {
                    grid[row][col].setBackground(Color.RED);
                }
            }
        }
    }

    public void end(String message) {
        JFrame frame2 = new JFrame("Game Ended");
        frame.setVisible(false);
        JLabel endingMsg = new JLabel(message);
        endingMsg.setFont(new Font("Verdana", Font.PLAIN, 100));
        frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame2.add(endingMsg);
        frame2.pack();
        frame2.setLocationByPlatform(true);
        frame2.setVisible(true);
    }


}