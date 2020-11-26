package ro.uaic.info.myapp.app;

import com.github.javafaker.Faker;
import ro.uaic.info.myapp.dao.AlbumController;
import ro.uaic.info.myapp.dao.ArtistController;

public class Album {
    //private int id; //might be useful later
    private String name;
    private int artist_id;
    private int release_year;

    public Album(String name, int artist_id, int release_year) {
        this.name = name;
        this.artist_id = artist_id;
        this.release_year = release_year;
    }

    public Album() {
    }

    public void generate(int artist_id) {
        Faker faker = new Faker();
        do {
            this.name = faker.music().genre() + " " + faker.animal().name();
        } while (this.name.contains("'"));
        this.artist_id = artist_id;
        this.release_year = faker.random().nextInt(100) + 1920;
    }

    public void insertIntoDatabase() {
        new AlbumController().create(this.name, this.artist_id, this.release_year);
    }

    public int getRelease_year() {
        return release_year;
    }

    public void setRelease_year(int release_year) {
        this.release_year = release_year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }

    @Override
    public String toString() {
        return "Album{" +
                "name='" + name + '\'' +
                ", artist_id=" + artist_id +
                ", release_year=" + release_year +
                '}';
    }
}
