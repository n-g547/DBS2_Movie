package de.hsh.dbs2.imdb.persistenz.activeRecord;

import de.hsh.dbs2.imdb.util.MovieDB_Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieCharacter {
    private Long movCharID;
    private String character;
    private String alias;
    private int position;
    private long movieID;

    private long perID;

    public MovieCharacter(){}
    public MovieCharacter(long movCharID) {
        this.movCharID= movCharID;
    }

    public Long getMovCharID() { return movCharID; }

    public String getCharacter() { return character; }
    public void setCharacter(String character) { this.character = character; }

    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

    public long getMovieID() { return movieID; }
    public void setMovieID(long movieID) { this.movieID = movieID; }

    public long getPerID() {
        return perID;
    }

    public void setPerID(long perID) {
        this.perID = perID;
    }




    public void insert() throws SQLException {
        if (movCharID == null) {
            String sql = "INSERT INTO movieCharacter VALUES(NULL, ?, ?, ?, ?, ?);";
            String sqlId = "SELECT last_insert_rowid();";
            ResultSet rs;
            try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql/*, Statement.RETURN_GENERATED_KEYS*/)) {
                pstmt.setString(1, getCharacter());
                pstmt.setString(2, getAlias());
                pstmt.setInt(3, getPosition());
                pstmt.setLong(4, getMovieID());
                pstmt.setLong(5, getPerID());


                pstmt.executeUpdate();
            }
            try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sqlId)) {
                rs= pstmt.executeQuery();
                movieID= rs.getLong(1);
            }
        }
    }

    public void update() throws SQLException {
        if (getMovCharID() != null) {
            String sql = "UPDATE movieCharacter  SET  character=?, alias=?, position=?, movID=?, perID=?  WHERE id = ?;";
            try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql)) {
                pstmt.setString(1, getCharacter());
                pstmt.setString(2, getAlias());
                pstmt.setInt(3, getPosition());
                pstmt.setLong(4, getMovieID());
                pstmt.setLong(5, getPerID());
                pstmt.setLong(6, getMovCharID());

                pstmt.executeUpdate();
            }
        }
    }

    public void delete() throws SQLException {
        if (getMovCharID() != null) {
            String sql = "DELETE FROM movieCharacter  WHERE id = ?;";
            try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql)) {
                pstmt.setLong(1, getMovCharID());

                pstmt.executeUpdate();
            }
        }
    }

}
