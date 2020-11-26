package com.company;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class ReportHtmlCommand implements Command{
    Catalog catalog;
    String location;
    public ReportHtmlCommand(Catalog catalog, String location) {
        this.catalog = catalog;
        this.location = location;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public void run() {
        PrintStream file = null;
        try {

            file = new PrintStream(location);
        } catch (FileNotFoundException e) {
            System.out.println("cannot access file location" + location);;
        }
        System.setOut(file);


        System.out.println("<html>\n<head></head>\n<body>\n");
        System.out.println("<h1>" + catalog.getName() + "</h1>");
        System.out.println("<h1>" + catalog.getPath() + "</h1>");
        for(var doc:catalog.getDocuments()){
            System.out.println("<ul><li> ID: " + doc.getId() + "</li>");
            System.out.println("<li> Location: " + doc.getLocation() + "</li>");
            System.out.println("<li> Name: " + doc.getName() + "</li>");
            for(var tag:doc.getTags().entrySet()){
                System.out.println("<li>" + tag.getKey() + ": " + tag.getValue() + "</li>");
            }
            System.out.println("</ul>");
        }

        System.out.println("</body>\n</html>");
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }
}
