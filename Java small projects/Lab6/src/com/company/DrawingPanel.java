package com.company;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.Color;


public class DrawingPanel extends JPanel {
    final MainFrame frame;
    final static int W = 800, H = 600;
    BufferedImage image; //the offscreen image
    Graphics2D graphics; //the "tools" needed to draw in the image
    //Create a file chooser
    final JFileChooser fc = new JFileChooser();

    public DrawingPanel(MainFrame frame) {
        this.frame = frame; createOffscreenImage(); init();
    }
    private void createOffscreenImage() {
        image = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
        graphics.setColor(Color.WHITE); //fill the image with white
        graphics.fillRect(0, 0, W, H);
    }
    private void init() {
        setPreferredSize(new Dimension(W, H)); //don’t use setSize. Why?
        setBorder(BorderFactory.createEtchedBorder()); //for fun
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                drawShape(e.getX(), e.getY());
                repaint();

            }
        });
    }
    private void drawShape(int x, int y) {
        int val1 = (Integer)frame.configPanel.sizeField.getValue();
        int val2 = (Integer)frame.configPanel.sidesField.getValue();
        String culoare = frame.configPanel.colorCombo.getSelectedItem().toString();
        String shape = frame.configPanel.shapesCombo.getSelectedItem().toString();
        Color color;
        if(culoare.equals("Red")) {
             color = new Color(255, 0, 0);
        } else {
            if(culoare.equals("Blue"))
                color = new Color(0,0,255);
            else
                color = new Color(0,255,0);
        }
        graphics.setColor(color);
        if(shape.equals("Circle")) {
            graphics.fill(new RegularPolygon(x-10, y-66,val1,100));
            frame.configPanel.circleConfig();
        } else{
            if(shape.equals("RegularPolygon")) {
                graphics.fill(new RegularPolygon(x-10, y-66, val1, val2));
                frame.configPanel.polygonConfig();
            } else {
                graphics.fill(new Rectangle(x-10-val1/2, y-66-val2/2, val1, val2));
                frame.configPanel.rectangleConfig();
            }
        }
    }
    @Override
    public void update(Graphics g) { }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }
}
