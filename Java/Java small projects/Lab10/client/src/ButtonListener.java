import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener {
    GameVisuals gameVisuals;

    public ButtonListener(GameVisuals gameVisuals) {
        this.gameVisuals = gameVisuals;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JButton o = (JButton) actionEvent.getSource();
        this.gameVisuals.redraw();
        if (o.getBackground() == Color.RED)
            o.setBackground(Color.GRAY);
    }
}
