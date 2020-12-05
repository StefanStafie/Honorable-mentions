package com.company;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
        static Catalog  catalog = null;
        public static void main(String[] args){
            String linie;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.print("enter command>");
                try {
                    linie = reader.readLine();
                } catch (IOException e) {
                    System.out.println("exceptie IO");
                    return;
                }
                //pentru enter
                if (linie.equals("")){
                    continue;
                }
                //pentru clear screen
                if (linie.equals("clear")) {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
                    continue;
                }
                //pentru exit
                if (linie.equals("exit")) {
                    System.out.println("Now exiting");
                    System.exit(0);

                }

                //pentru help
                if (linie.equals("help")) {
                    System.out.println();
                    System.out.println();
                    System.out.println("Written by: Stafie Stefan");;
                    System.out.println("Lab5 Java");
                    System.out.println("Commands to use:");
                    System.out.println("--------------------");
                    System.out.println(" create catalog <name> <path>");
                    System.out.println("add doc {id} <name> <path>");
                    System.out.println("add tag <name> <value> to <id>");
                    System.out.println("load <path>");
                    System.out.println("list <id>");;
                    System.out.println("report html");
                    System.out.println("view");
                    System.out.println("clear");
                    System.out.println("save");
                    System.out.println("help");
                    System.out.println("---------------------");;
                    continue;
                }

                //pentru adaugare a unui document intr-un catalog
                if(linie.startsWith("add doc ")) {
                    if(catalog==null){
                        System.out.println("You have to load the catalog first");
                    } else {
                        String[] parametri = linie.split(" ");
                        if (parametri.length != 5) {
                            System.out.println("Write \"help\" for help");
                        } else {
                            Document document = new Document(parametri[2], parametri[3], parametri[4]);
                            catalog.add(document);
                        }

                    }
                    continue;
                }

                //pentru salvarea catalogului pe disc
                if(linie.equals("save")) {
                    if(catalog==null){
                        System.out.println("You have to load the catalog first");
                    } else {
                        CatalogUtil use = new CatalogUtil();
                        try{use.save(catalog);}
                        catch(Exception e){
                            System.out.println("Cannot find file path or file already exists");
                        }
                    }
                    continue;
                }

                //pentru crearea unui catalog gol
                if(linie.startsWith("create catalog ")) {
                    String[] parametri = linie.split(" ");
                    if (parametri.length != 4) {
                        System.out.println("Write \"help\" for help");
                    } else {
                       catalog = new Catalog(parametri[2], parametri[3]);
                    }
                    continue;
                }

                //pentru adaugarea unui tag la un document
                if(linie.startsWith("add tag ")) {
                    if(catalog==null){
                        System.out.println("You have to load the catalog first");
                    } else {
                        String[] parametri = linie.split(" ");
                        if (parametri.length != 6) {
                            System.out.println("Write \"help\" for help");
                        } else {
                            Document document = catalog.findById(parametri[5]);
                            if(document == null) {
                                System.out.println("Document not found");
                            } else {
                                document.addTag(parametri[2],parametri[3]);
                            }

                        }
                    }
                    continue;
                }

                //pentru incarcarea unui catalog de pe disc in memorie
                if(linie.startsWith("load ")) {
                    String[] parametri = linie.split(" ");
                    if (parametri.length != 2){
                        System.out.println("\"help\" for help");
                        continue;
                    }
                    LoadCommand x = new LoadCommand(catalog, parametri[1]);
                    x.run();
                    catalog = x.getCatalog();
                    continue;
                }

                //pentru deschis catalog in notepad
                if (linie.equals("view")) {
                    if(catalog != null && catalog.getPath() !=null){
                        new ViewCommand(catalog.getPath()).run();
                    } else {
                        System.out.println("load catalog pls");
                    }
                    continue;
                }

                //pentru salvare in format html (salveaza ca index.html)
                if (linie.equals("report html")) {
                    if(catalog != null){
                        new ReportHtmlCommand(catalog,"index.html").run();
                    } else {
                        System.out.println("load catalog pls");
                    }
                    continue;
                }

                //pentru afisarea unui document in cosola
                if (linie.startsWith("list")) {
                    if (catalog == null) {
                        System.out.println("load catalog pls");
                    } else {
                        String[] argumente = linie.split(" ");
                        if (argumente.length != 2) {
                            System.out.println("\"help\" for help");
                        } else {
                            new ListCommand(catalog, argumente[1]).run();
                        }
                    }
                    continue;
                }
                //daca nu e recunoscuta nicio comanda, propune help
                System.out.println("\"help\" for help");
            }
        }
    }
