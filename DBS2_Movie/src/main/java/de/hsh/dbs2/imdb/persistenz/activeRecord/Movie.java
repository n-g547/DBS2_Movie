package de.hsh.dbs2.imdb.persistenz.activeRecord;

import de.hsh.dbs2.imdb.util.MovieDB_Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Movie {
    private Long movieID;
    private String title;
    private int year;
    private char type;

    public Movie(String title, int year, char type) {
        setTitle(title);
        setType(type);
        setYear(year);
    }

    public Movie(Long movieID) {
        this.movieID=movieID;
    }

    public Movie(){}

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public Long getMovieID() { return movieID; }


    public int getYear() { return year; }

    public void setYear(int year) { this.year = year; }

    public char getType() { return type; }

    public void setType(char type) { this.type = type; }


    /*
    Fügt einen neuen Datensatz in die Tabelle Movie mit den in dem Objekt gesetzten Werten
    für „title“, „year“ und „type“ ein.
    Die ID wird neu vergeben und in der Klasse das Attribut „id“ auf den neuen Wert gesetzt
     */
    //Insert sollte nur arbeiten, wenn die ID nicht gesetzt ist im Objekt
    public void insert() throws SQLException{
        if (movieID == null) {
            String sql = "INSERT INTO movie VALUES(NULL, ?, ?, ?);";
            String sqlId = "SELECT last_insert_rowid();";
            ResultSet rs;
            try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql/*, Statement.RETURN_GENERATED_KEYS*/)) {
                pstmt.setString(1, getTitle());
                pstmt.setInt(2, getYear());
                pstmt.setString(3, String.valueOf(getType()));

                pstmt.executeUpdate();
            }
            try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sqlId)) {
                rs= pstmt.executeQuery();
                movieID= rs.getLong(1);
            }
        }
    }


    /*
    Aktualisiert die Werte von „title“, „year“ und „type“ in der Datenbank mit den
    Werten aus dem Objekt.
    Update kanen nur arbeiten, wenn eine ID vorhanden ist.
    */
    public void update() throws SQLException {
        if (this.getMovieID() != null) {
            String sql = "UPDATE movie  SET  title=?, year=?, type=?  WHERE id = ?;";
            try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql)) {
                pstmt.setString(1, this.getTitle());
                pstmt.setInt(2, this.getYear());
                pstmt.setString(3, String.valueOf(this.getType()));
                pstmt.setLong(4, this.getMovieID());

                pstmt.executeUpdate();
            }
        }
    }


    /*
    Löscht den Datensatz aus der Datenbank.
    Delete kannen nur arbeiten, wenn eine ID vorhanden ist
    */
    public void delete() throws SQLException{
        if (this.getMovieID() != null) {
            String sql = "DELETE FROM movie WHERE id = ?;";
            try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql)) {
                pstmt.setLong(1, this.getMovieID());
                pstmt.executeUpdate();
            }
        }
    }


}


