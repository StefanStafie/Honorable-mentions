package ro.uaic.info.myapp.dao;

import ro.uaic.info.myapp.app.Artist;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArtistController {
    public ArtistController() {
    }

    /**create entry with executeUpdate
     *
     * @param name the name of the artist to be inserted
     * @param country the country of the artist to be inserted
     *
     */
    public void create(int id, String name, String country) {
        int affectedRows = -1; // might be useful later
        try {
            java.sql.Statement stmt = Database.getInstance().getStmt();
            affectedRows = stmt.executeUpdate("INSERT INTO artists (id, name, country) values (" + id + ", '" + name + "', '" + country + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //System.out.println(affectedRows);
    }







    /** finds entry with given name
     *
     * @param name the name of the entry to be found
     * @return the first id it finds corresponding to the name
     */
    public List findByName(String name){
        ResultSet rset = null;
        try {
            rset = Database.getInstance().getStmt().executeQuery("Select * from artists where name = '" + name + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List artistIds = new ArrayList<>();
        try{
            while(rset.next())
                artistIds.add(rset.getInt("id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artistIds;
    }







    public Artist find(int id){
        ResultSet rset = null;
        Artist artist = new Artist();
        try {
            rset = Database.getInstance().getStmt().executeQuery("Select * from artists WHERE id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            rset.next();
            artist.setName(rset.getString("name"));
            artist.setCountry(rset.getString("country"));
            artist.setId(rset.getInt("id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return artist;
    }












    public List findAll(){
        ResultSet rset = null;
        try {
            rset = Database.getInstance().getStmt().executeQuery("Select id from artists");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List artistIds = new ArrayList<>();
        try{
            while(rset.next())
                artistIds.add(rset.getInt("id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artistIds;
    }
}
