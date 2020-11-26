package com.example.demo;

import oracle.ucp.proxy.annotation.Pre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseController {

    public boolean addPlayer(String email, String hash) {
        int affectedRows = -1;
        try {
            affectedRows = Database.getInstance().getStmt().executeUpdate("INSERT INTO email_hash values ('" + email + "', '" + hash + "') ");
        } catch (SQLException e) {
            return false;
        }
        if (affectedRows > 0)
            return true;
        return false;
    }

    public boolean playerExists(String email, String hash) {
        ResultSet rset = null;
        int items = 0;
        try {
            String strSelect = "SELECT * FROM email_hash WHERE email = ? AND hash = ?";
            PreparedStatement preparedStatement = Database.getConn().prepareStatement(strSelect);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, hash);
            rset = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            while (rset.next())
                items++;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (items > 0)
            return true;
        return false;
    }

    public String raspunsBazaDate(String email, String csv) {
        String returnValue = "";
        try {
            CallableStatement cstmt = Database.getConn().prepareCall("begin ? := urmatoarea_intrebare(?, ?);  end;");
            cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
            cstmt.setString(2,email);
            cstmt.setString(3,csv);
            cstmt.execute();
            returnValue = cstmt.getString(1);
            return returnValue;
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Raspunsuri> raspunsFinal(String email) {
        String returnValue = "";
        ResultSet rset = null;
        ResultSet rset2 = null;
        int counter = 0;
        List<Raspunsuri> raspunsuris = new ArrayList<>();
        try {
            String strSelect = "SELECT * FROM teste WHERE email = ? ORDER BY nr_ordine";
            PreparedStatement preparedStatement = Database.getConn().prepareStatement(strSelect);
            preparedStatement.setString(1, email);
            rset = preparedStatement.executeQuery();
            while (rset.next()){
                counter++;
                String raspunsuri_corecte = "";
                String raspunsuri_utilizator = "";
                String strSelect2 = "SELECT * FROM raspunsuri WHERE id = ?";
                String[] log =  rset.getString("raspunsuri_corecte").split(",");
                //iau text raspunsuri corecte
                for(int i=0;i<log.length;i++){
                    PreparedStatement preparedStatement2 = Database.getConn().prepareStatement(strSelect2);
                    preparedStatement2.setString(1, log[i]);
                    rset2 = preparedStatement2.executeQuery();
                    rset2.next();
                    raspunsuri_corecte+= rset2.getString("text_raspuns") + ", ";
                }

                //iau text raspunsuri utilizator
                strSelect2 = "SELECT * FROM raspunsuri WHERE id = ?";
                log =  rset.getString("raspunsuri_utilizator").split(",");
                for(int i=0;i<log.length;i++){
                    PreparedStatement preparedStatement2 = Database.getConn().prepareStatement(strSelect2);
                    preparedStatement2.setString(1, log[i]);
                    rset2 = preparedStatement2.executeQuery();
                    rset2.next();
                    raspunsuri_utilizator+=rset2.getString("text_raspuns") + ", ";
                }

                //iau text intrebare
                strSelect2 = "SELECT * FROM intrebari WHERE id = ?";
                PreparedStatement preparedStatement2 = Database.getConn().prepareStatement(strSelect2);
                preparedStatement2.setString(1, rset.getString("intrebare_curenta"));
                rset2 = preparedStatement2.executeQuery();
                rset2.next();

                //iau scorul de la intrebarea asta
                CallableStatement cstmt = Database.getConn().prepareCall("begin ? := get_scor_intrebare(?, ?);  end;");
                cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
                cstmt.setString(2,email);
                cstmt.setInt(3,counter);
                cstmt.execute();
                String scor = cstmt.getString(1) ;


                raspunsuri_corecte = raspunsuri_corecte.substring(0,raspunsuri_corecte.length()-2);
                raspunsuri_utilizator = raspunsuri_utilizator.substring(0,raspunsuri_utilizator.length()-2);
                raspunsuris.add(new Raspunsuri(raspunsuri_corecte, raspunsuri_utilizator, rset2.getString("text_intrebare"), scor));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return raspunsuris;
    }
}
