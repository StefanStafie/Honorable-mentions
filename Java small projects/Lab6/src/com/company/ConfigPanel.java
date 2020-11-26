package com.company;
import javax.swing.*;

public class ConfigPanel extends JPanel {
    final MainFrame frame;
    JSpinner sidesField; // number of sides
    JComboBox colorCombo; // the color of the shape
    JSpinner sizeField;
    JComboBox shapesCombo;
    JLabel sizeLabel;
    JLabel sidesLabel;

    public ConfigPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }
    private void init() {
        //create the label and the spinner
        String colors[] = {"Blue", "Red", "Green"};
        colorCombo = new JComboBox(colors);
        colorCombo.setBounds(50, 50,90,20);

        String shapes[] = {"Circle", "RegularPolygon", "Rectangle"};
        shapesCombo = new JComboBox(shapes);
        shapesCombo.setBounds(50, 50,130,20);

        sidesLabel = new JLabel("");
        sidesField = new JSpinner(new SpinnerNumberModel(6, -100, 100, 1));

        sizeLabel = new JLabel("Radius");
        sizeField = new JSpinner(new SpinnerNumberModel(20, -100, 100, 1));

        this.add(colorCombo);
        this.add(shapesCombo);
        this.add(sizeField);
        this.add(sizeLabel);
        //this.add(sidesField);
        this.add(sidesLabel); //JPanel uses FlowLayout by default


    }
    public void polygonConfig(){
        this.sizeLabel.setText("radius");
        this.remove(sidesField);
        this.add(sidesField);
        this.sidesLabel.setText("sides");

    }
    public void circleConfig (){
        this.sizeLabel.setText("Radius");
        this.sidesLabel.setText("                                                "); //to draw over stuff
        this.remove(sidesField);
    }
    public void rectangleConfig (){
        sizeLabel.setText("Width");
        this.remove(sidesField);
        this.add(sidesField);
        sidesLabel.setText("Height");
    }


}
