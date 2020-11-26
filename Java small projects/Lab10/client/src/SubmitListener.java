import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SubmitListener implements ActionListener {
    GameVisuals gameVisuals;

    public SubmitListener(GameVisuals gameVisuals) {
        this.gameVisuals = gameVisuals;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        for (int row = 0; row < gameVisuals.size; row++) {
            for (int col = 0; col < gameVisuals.size; col++) {
                if (this.gameVisuals.grid[row][col].getBackground().toString().equals("java.awt.Color[r=128,g=128,b=128]")) {
                    gameVisuals.board.notEmpty[row][col] = true;
                    gameVisuals.board.blackOrWhite[row][col] = false;
                    System.out.println("you " + row + " " + col);
                    gameVisuals.serverCommunicator.sendSubmit(row, col);
                    return;
                }
            }
        }
        System.out.println("Please select a valid button");
    }

}
