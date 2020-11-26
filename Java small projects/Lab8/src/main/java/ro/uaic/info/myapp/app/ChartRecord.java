package ro.uaic.info.myapp.app;

import com.github.javafaker.Faker;
import ro.uaic.info.myapp.dao.AlbumController;

import java.util.List;

public class ChartRecord {
    private int rank;
    private int album_id;

    public ChartRecord(int rank, int album_id) {
        this.rank = rank;
        this.album_id = album_id;
    }

    public ChartRecord() {
        Faker faker = new Faker();
        this.rank = faker.number().numberBetween(1, 9999);
        List ids = new AlbumController().findAllId();
        if (ids.size() > 0)
            this.album_id = (int) ids.get(faker.random().nextInt(ids.size() - 1));
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    @Override
    public String toString() {
        return "ChartRecord{" +
                "rank=" + rank +
                ", album_id=" + album_id +
                '}';
    }
}
