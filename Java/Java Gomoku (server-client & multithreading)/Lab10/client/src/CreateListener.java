import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateListener implements ActionListener {
    GameVisuals gameVisuals;
    JSpinner spinner;

    public CreateListener(GameVisuals gameVisuals, JSpinner spinner) {
        this.gameVisuals = gameVisuals;
        this.spinner = spinner;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        gameVisuals.serverCommunicator.sendCreate((int) spinner.getValue());
    }
}
