package com.company;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class ViewCommand implements Command{
    String id;

    public ViewCommand(String id) {
        this.id = id;
    }

    @Override
    public void run() {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(URI.create(id));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
