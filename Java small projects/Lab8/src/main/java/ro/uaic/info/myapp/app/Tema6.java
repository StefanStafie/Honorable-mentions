package ro.uaic.info.myapp.app;

public class Tema6 {
    public Tema6() {
        Artist artist = new Artist(23,"nume","Tara");

        artist.insertIntoDatabase();


        Artist artist1 = new Artist();
        artist1.getFromDatabase(23);

        System.out.println(artist1);
    }
}
