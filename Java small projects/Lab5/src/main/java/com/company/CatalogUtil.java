package com.company;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.awt.*;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;


public class CatalogUtil {
    public static void save(Catalog catalog)
            throws IOException {
        PrintStream file = new PrintStream( catalog.getPath( ));
        System.setOut( file);

        JsonObject jsonCatalog = new JsonObject();
        jsonCatalog.addProperty("name", catalog.getName ());
        jsonCatalog.addProperty( "path", catalog.getPath( ));

        JsonArray jsonDocs = new JsonArray();
        List<Document> documents = catalog.getDocuments();
        for(Document doc:documents) {
            JsonObject jsonDoc = new JsonObject();
            jsonDoc.addProperty("id", doc.getId());
            jsonDoc.addProperty("location", doc.getLocation());
            jsonDoc.addProperty("name", doc.getName());
            Map<String, Object> tags = doc.getTags();
            tags.forEach((key, value) -> jsonDoc.addProperty(key.toString(), value.toString()));
            jsonDocs.add(jsonDoc);
        }
        jsonCatalog.add( "Documents", jsonDocs);
        System.out.println(jsonCatalog);
        System.setOut(new PrintStream (
                        new FileOutputStream(
                            FileDescriptor.out)));
    }



    public static void view(Document doc) {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(new URI(doc.getLocation()));
        } catch (IOException e) {
            System.out.println(e);;
        } catch (URISyntaxException e) {
            System.out.println(e);;
        }
    }
}