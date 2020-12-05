import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JoinListener implements ActionListener {
    GameVisuals gameVisuals;
    JSpinner spinner;

    public JoinListener(GameVisuals gameVisuals, JSpinner spinner) {
        this.gameVisuals = gameVisuals;
        this.spinner = spinner;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        gameVisuals.serverCommunicator.sendJoin((int) spinner.getValue());
    }
}
