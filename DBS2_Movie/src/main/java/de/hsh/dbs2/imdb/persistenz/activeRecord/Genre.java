package de.hsh.dbs2.imdb.persistenz.activeRecord;

import de.hsh.dbs2.imdb.util.MovieDB_Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Genre {
    private String genre;
    private Long genreID;

    public void setGenre(String genre) { this.genre = genre; }
    public Long getGenreID() { return genreID; }
    public String getGenre() { return genre; }

    public Genre(){}
    public Genre(long genreID) {
        this.genreID= genreID;
    }


    public void insert() throws SQLException {
        if (genreID == null) {
            String sql = "INSERT INTO genre VALUES(NULL, ?);";
            String sqlId = "SELECT last_insert_rowid();";
            ResultSet rs;
            try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql/*, Statement.RETURN_GENERATED_KEYS*/)) {
                pstmt.setString(1, getGenre());

                pstmt.executeUpdate();
            }
            try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sqlId)) {
                rs= pstmt.executeQuery();
                genreID= rs.getLong(1);
            }
        }

    }


    /*
    Aktualisiert die Werte von „title“, „year“ und „type“ in der Datenbank mit den
    Werten aus dem Objekt.
    Update kanen nur arbeiten, wenn eine ID vorhanden ist.
    */
    public void update() throws SQLException {
        if (this.getGenreID() != null) {
            String sql = "UPDATE genre  SET  genre=?  WHERE id = ?;";
            try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql)) {
                pstmt.setString(1, this.getGenre());
                pstmt.setLong(2, this.getGenreID());

                pstmt.executeUpdate();
            }
        }
    }


    /*
    Löscht den Datensatz aus der Datenbank.
    Delete kannen nur arbeiten, wenn eine ID vorhanden ist
    */
    public void delete() throws SQLException{
        if (this.getGenreID() != null) {
            String sql = "DELETE FROM genre WHERE id = ?;";
            try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql)) {
                pstmt.setLong(1, this.getGenreID());
                pstmt.executeUpdate();
            }
        }
    }

}
