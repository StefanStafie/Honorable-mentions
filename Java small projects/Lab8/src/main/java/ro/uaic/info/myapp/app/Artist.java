package ro.uaic.info.myapp.app;

import com.github.javafaker.Faker;
import ro.uaic.info.myapp.dao.ArtistController;

import java.io.Serializable;

public class Artist implements Serializable {
    private int id;

    private String name;
    private String country;

    public Artist(int id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public Artist() {
    }

    public void generate() {
        Faker faker = new Faker();
        do {
            this.name = faker.name().fullName();
            this.country = faker.country().name();
        } while (this.name.contains("'") || this.country.contains("'"));
    }

    public void insertIntoDatabase() {
        new ArtistController().create(this.id, this.name, this.country);
    }
    public void getFromDatabase(int id){
        Artist aux = new ArtistController().find(id);
        this.id = aux.getId();
        this.name = aux.getName();
        this.country = aux.getCountry();
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
