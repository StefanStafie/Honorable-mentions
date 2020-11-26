package com.company;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LoadCommand implements Command {
    Catalog catalog ;
    String id;

    public Catalog getCatalog() {
        return catalog;
    }

    public LoadCommand(Catalog catalog, String id) {
        this.catalog = catalog;
        this.id = id;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void run() {
        this.catalog = new Catalog();
        String text = "";
        try (var reader = new BufferedReader(new FileReader(this.id))) {
            String line ;
            while ((line = reader.readLine()) != null) {
                text += line;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return;
        } catch (IOException e) {
            System.out.println("exceptie IO");
            return;
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject objectFromString = jsonParser.parse(text).getAsJsonObject();

        //informatii despre catalog
        this.catalog.setName(objectFromString.get("name").getAsString());
        this.catalog.setPath(objectFromString.get("path").getAsString());

        //adaugare de documente
        JsonArray jsonArray = objectFromString.get("Documents").getAsJsonArray();
        for(var x : jsonArray) {
            Document doc = new Document();
            JsonObject jsonDoc = x.getAsJsonObject();
            doc.setId(jsonDoc.get("id").getAsString());
            doc.setName(jsonDoc.get("name").getAsString());
            doc.setLocation(jsonDoc.get("location").getAsString());
            //taguri
            var tags = doc.getTags().entrySet();
            tags.forEach(tag -> doc.addTag(tag.getKey(), tag.getValue()));
            catalog.add(doc);
        }
    }
}
