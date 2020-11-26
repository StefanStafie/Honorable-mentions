package ro.uaic.info.myapp.app;

import com.github.javafaker.Faker;
import org.w3c.dom.ls.LSOutput;
import ro.uaic.info.myapp.dao.ArtistController;
import ro.uaic.info.myapp.dao.ArtistRankingController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Test {
    public Test() {
        /**generate 20 artists and insert into database*/
        ArtistController artistController = new ArtistController();
        Artist artist = new Artist();
        for (int i = 0; i < 20; i++) {
            artist.generate();
            artist.insertIntoDatabase();
        }
        List artistIds = artistController.findAll();
        /**generate 100 albums and insert into database*/
        Album album = new Album();
        Faker faker = new Faker();
        for (int i = 0; i < 100; i++) {
            album.generate((int) artistIds.get(faker.random().nextInt(artistIds.size()-1)));
            album.insertIntoDatabase();
        }
        /**generate 10 charts with 10 albums each and insert into database
         * ranking is created based on number of points they receive.
         * */
        for (int i = 0; i < 10; i++) {
            Chart chart = new Chart();
            chart.generate(10);
            chart.insertIntoDatabase();
        }
        System.out.println();
        /**finds a ranking based on the number of points they have
         * checks all charts al all albums of the artist and adds the points to the artist point pool
         * */
        Map<String, Integer> cool = new ArtistRankingController().findRanking();
        cool.entrySet().stream().forEach(System.out::println);

    }
}
