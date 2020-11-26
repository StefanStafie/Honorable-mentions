package com.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Catalog implements Serializable {
    private String name;
    private String path;
    private List<Document> documents = new ArrayList<>();

    public Catalog() {
    }

    public Catalog(String name, String path, List<Document> documents) {
        this.path = path;
        this.name = name;
        this.documents = documents;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public Catalog(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }


    public List<Document>  getDocuments() {
        return  documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;;
    }


    public Document findById(String id) {
        return documents.stream()
                .filter(d -> d.getId().equals(id)).findFirst().orElse(null);
    }
    public void add(Document doc) {
        if(this.findById(doc.getId()) == null) // VERIFICA DACA A MAI FOST ADAUGAT DOCUMENTUL
            documents.add(doc);
        else
            System.out.println("Documentul " + doc.getId() + "exista deja");
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "{" +
                "\n  name:'" + name + '\'' +
                ",\n  path:'" + path + '\'' +
                ",\n  documents:" + documents +
                "\n}";
    }
}
