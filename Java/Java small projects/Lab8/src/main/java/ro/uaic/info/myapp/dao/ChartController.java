package ro.uaic.info.myapp.dao;


import ro.uaic.info.myapp.app.Chart;
import ro.uaic.info.myapp.app.ChartRecord;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class ChartController {
    public ChartController() {
    }

    /**
     * @param title   the name of the chart
     * @param records list of albums in the chart
     */
    public void create(String title, List<ChartRecord> records) {
        int affectedRows; // might be useful later
        try {
            java.sql.Statement stmt = Database.getInstance().getStmt();
            affectedRows = stmt.executeUpdate("insert into charts (title) values ('" + title + "')");
            ResultSet rset = stmt.executeQuery("select * from charts where title = '" + title + "'");
            rset.next();
            int chart_id = rset.getInt("id");
            if (affectedRows != 0)
                for (var x : records)
                    affectedRows = stmt.executeUpdate("insert into chart_record (chart_id, rank, album_id) values (" + chart_id + ", " + x.getRank() + ", " + x.getAlbum_id() + ")");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Duplicate entry in charts: " + title + ". Did not insert.");
        } catch (SQLException e) {
            System.out.println(e);
        }
        //System.out.println(affectedRows);
    }

    /**
     * finds entry with given name
     *
     * @param title the name of the entry to be found
     * @return the first id it finds corresponding to the name
     */
    public Chart findByName(String title) {
        ResultSet rset;
        Chart chart = new Chart(title);
        try {
            rset = Database.getInstance().getStmt().executeQuery("select id from charts where title = '" + title + "\'");

            rset.next();
            int chartId = rset.getInt("id");

            rset = Database.getInstance().getStmt().executeQuery("select * from charts_records where chart_id = '" + chartId + "'");

            while (rset.next()) {
                int rank = rset.getInt("rank");
                int albumId = rset.getInt("album_id");
                chart.addRecord(new ChartRecord(rank, albumId));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chart;

    }
}
