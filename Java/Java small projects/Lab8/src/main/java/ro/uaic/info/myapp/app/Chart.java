package ro.uaic.info.myapp.app;

import com.github.javafaker.Faker;
import ro.uaic.info.myapp.dao.ChartController;

import java.util.ArrayList;
import java.util.List;

public class Chart {
    //private int id; //might be useful later
    private String title;
    private List<ChartRecord> records = new ArrayList<>();

    public Chart(String title) {
        this.title = title;
    }

    public Chart() {
    }
    public void generate(int length) {
        Faker faker = new Faker();
        this.title = "top " + faker.pokemon().name();
        for (int i = 0; i < length; i++) {
            ChartRecord chartRecord = new ChartRecord();
            this.addRecord(chartRecord);
        }
    }
    public void insertIntoDatabase() {
        ChartController chartController = new ChartController();
        chartController.create(this.title, this.records);
    }
    public void addRecord(ChartRecord chartRecord) {
        this.records.add(chartRecord);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Chart{" +
                "title='" + title + '\'' +
                ", records=" + records +
                '}';
    }
}
