package de.hsh.dbs2.imdb.persistenz.Factory;

import de.hsh.dbs2.imdb.persistenz.activeRecord.HasGenre;
import de.hsh.dbs2.imdb.util.MovieDB_Connection;

import java.util.ArrayList;
import java.sql.*;


public class HasGenreFactory {
    public ArrayList<HasGenre> findByGenre(String genre) throws SQLException {
        ArrayList<HasGenre> list = new ArrayList<>();
        HasGenre hasGenre;
        String SQL = "SELECT * FROM HasGenre hg Join Genre g ON hg.genreID = g.id WHERE g.genre LIKE ? ;";

        try (PreparedStatement stmt = MovieDB_Connection.getConnection().prepareStatement(SQL)) {
            stmt.setString(1, genre);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                hasGenre = new HasGenre();
                hasGenre.setMovieID(rs.getLong("hg.movieid"));
                hasGenre.setGenreID(rs.getLong("hg.genreid"));
                list.add(hasGenre);
            }
        }
        return list;
    }

    public ArrayList<HasGenre> findByMovieID(long id) throws SQLException {
        ArrayList<HasGenre> list = new ArrayList<>();
        HasGenre hasGenre;
        String SQL = "SELECT * FROM HasGenre WHERE movieId = ? ;";

        try (PreparedStatement stmt = MovieDB_Connection.getConnection().prepareStatement(SQL)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                hasGenre = new HasGenre();
                hasGenre.setMovieID(rs.getLong("movieid"));
                hasGenre.setGenreID(rs.getLong("genreid"));
                list.add(hasGenre);
            }
        }
        return list;
    }

    public static ArrayList<HasGenre> findByGenreID(long id) throws SQLException {
        ArrayList<HasGenre> list = new ArrayList<>();
        String SQL = "SELECT * FROM HasGenre WHERE genreID = ? ;";

        try (PreparedStatement stmt = MovieDB_Connection.getConnection().prepareStatement(SQL)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                HasGenre tmp = new HasGenre();
                tmp.setGenreID(id);
                tmp.setMovieID(rs.getLong("movieID"));
                list.add(tmp);
            }
        }
        return list;
    }
}
