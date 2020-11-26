package com.company;

public class ListCommand implements Command{
    String id;
    Catalog catalog ;

    public ListCommand(Catalog catalog, String id) {
        this.catalog = catalog;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public String getId() {
        return id;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    @Override
    public void run() {
        Document doc = catalog.findById(id);
        if(doc== null)
            System.out.println("\"" + id + "\" nu exista");
        else
            System.out.println(doc.toString());
    }
}
