package de.hsh.dbs2.imdb.persistenz.Factory;

import de.hsh.dbs2.imdb.persistenz.activeRecord.MovieCharacter;
import de.hsh.dbs2.imdb.util.MovieDB_Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovCharacterFactory {



    public static List<MovieCharacter> findCharacterByMovieId(long movieId) throws SQLException {
        List<MovieCharacter> movieCharacterMovieList = new ArrayList<>();
        String sql = "SELECT * FROM MovieCharacter WHERE movid = ? ORDER BY position;";
        try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, movieId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                MovieCharacter movieCharacter = new MovieCharacter(rs.getLong("id"));
                movieCharacter.setCharacter(rs.getString("Character"));
                movieCharacter.setAlias(rs.getString("alias"));
                movieCharacter.setPosition(rs.getInt("position"));
                movieCharacter.setMovieID(movieId);
                movieCharacter.setPerID(rs.getLong("perID"));

                movieCharacterMovieList.add(movieCharacter);
            }
        }
        return movieCharacterMovieList;
    }


    public static MovieCharacter findCharacterByMovieIdAndCharacter(long movieId, String character) throws SQLException {
        MovieCharacter movieCharacter = null;
        String sql = "SELECT * FROM MovieCharacter WHERE movid = ? AND character LIKE ? ORDER BY position;";
        try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, movieId);
            pstmt.setString(2, character);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                movieCharacter = new MovieCharacter(rs.getLong("id"));
                movieCharacter.setCharacter(rs.getString("Character"));
                movieCharacter.setAlias(rs.getString("alias"));
                movieCharacter.setPosition(rs.getInt("position"));
                movieCharacter.setMovieID(movieId);
                movieCharacter.setPerID(rs.getLong("perID"));


            }
        }
        return movieCharacter;
    }
}
