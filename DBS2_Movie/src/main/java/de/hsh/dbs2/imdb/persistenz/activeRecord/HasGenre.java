package de.hsh.dbs2.imdb.persistenz.activeRecord;

import de.hsh.dbs2.imdb.util.MovieDB_Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HasGenre {
    private Long movieID;
    private Long genreID;

    public Long getMovieID() { return movieID; }

    public void setMovieID(long movieID) { this.movieID = movieID; }

    public Long getGenreID() { return genreID; }

    public void setGenreID(long genreID) { this.genreID = genreID; }


    public void insert() throws SQLException {
        String sql = "INSERT INTO hasGenre VALUES(?, ?);";
        ResultSet rs;
        try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, getGenreID());
            pstmt.setLong(2, getMovieID());

            pstmt.executeUpdate();
        }

    }

    /*
    public void updateGenreID() throws SQLException {
        String sql = "UPDATE hasGenre set genreID=? where movieID = ?;";
        ResultSet rs;
        try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, getGenreID());
            pstmt.setLong(2, getMovieID());

            pstmt.executeUpdate();
        }
    }

    public void upateMovieID() throws SQLException {
        String sql = "UPDATE hasGenre set movieID=? where genreID = ?;";
        ResultSet rs;
        try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, getMovieD());
            pstmt.setLong(2, getGenreID());

            pstmt.executeUpdate();
        }
    }
    */

    public void delete() throws SQLException {
        if (getGenreID() != null) {
            String sql = "DELETE FROM hasGenre WHERE genreid = ? AND movieid=?;";
            try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql)) {
                pstmt.setLong(1, this.getGenreID());
                pstmt.setLong(2, this.getMovieID());
                pstmt.executeUpdate();
            }
        }
    }
}
