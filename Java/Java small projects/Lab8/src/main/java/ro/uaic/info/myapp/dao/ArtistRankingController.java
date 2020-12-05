package ro.uaic.info.myapp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.util.stream.Collectors.toMap;

public class ArtistRankingController {
    Map<String, Integer> artistPoints = new HashMap<>();

    public ArtistRankingController() {

    }

    /**
     * for each artist in the database
     * checks all charts al all albums of the artist and adds the points to the artist point pool
     *
     * @return ordered Hashmap of artist name and number of points
     */
    public Map<String, Integer> findRanking() {

        ResultSet rset, rset2;
        try {
            rset = Database.getInstance().getStmt().executeQuery("Select name from artists");
            List<String> artistIds = new ArrayList<>();
            while (rset.next()) {
                artistIds.add(rset.getString("name"));
            }
            for (int i = 0; i < artistIds.size(); i++) {
                rset2 = Database.getInstance().getStmt().executeQuery("Select rank from artists ar\n" +
                        "    JOIN albums al ON ar.id = al.artist_id\n" +
                        "    JOIN chart_record cr ON cr.album_id = al.id\n" +
                        "    JOIN charts c ON c.id = cr.chart_id\n" +
                        "    WHERE ar.name = '" + artistIds.get(i) + "'");
                int points = 0;
                while (rset2.next()) {
                    points += rset2.getInt("rank");
                }
                artistPoints.put(artistIds.get(i), points);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artistPoints.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
