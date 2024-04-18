package de.hsh.dbs2.imdb.persistenz.Factory;

import de.hsh.dbs2.imdb.persistenz.activeRecord.Genre;
import de.hsh.dbs2.imdb.persistenz.activeRecord.Movie;
import de.hsh.dbs2.imdb.util.MovieDB_Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenreFactory {


    public static List<Genre> findGenreByMovieId(long movieId) throws SQLException {
        String sql = "SELECT * FROM (genre g JOIN hasgenre hg on g.id=hg.genreID) JOIN movie m ON hg.movieID=m.id  WHERE m.id = ?;";
        List<Genre> genreMovieList = new ArrayList<>();
        try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, movieId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Genre genre = new Genre(rs.getLong(1));
                genre.setGenre(rs.getString(2));
                genreMovieList.add(genre);

            }
        }
        return genreMovieList;
    }

    public static Genre findById(long id) throws SQLException {
        Genre tmp = new Genre(id);
        String sql = "SELECT * FROM Genre WHERE ID = ? ;";
        try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id );
            ResultSet rs = pstmt.executeQuery();
            tmp.setGenre(rs.getString("genre"));
        }
        /**
         if (tmp.getName() == null ) {
         throw new RuntimeException("NOT FOUND!!");
         }
         */
        return tmp;
    }

    public static ArrayList<Genre> findByTitle(String str) throws SQLException {
        ArrayList<Genre> list = new ArrayList<>();

        String sql = "SELECT * FROM Genre WHERE genre LIKE ?;";

        try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, "%" + str + "%");
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {

                Genre tmp = new Genre((long)rs.getInt(1));
                tmp.setGenre(rs.getString(2));
                list.add(tmp);
            }

        }
        return list;
    }

    public static ArrayList<Genre> findAll() throws SQLException {
        ArrayList<Genre> list = new ArrayList<>();

        String sql = "SELECT * FROM Genre ;";

        try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                Genre tmp = new Genre((long)rs.getLong(1));
                tmp.setGenre(rs.getString(2));
                list.add(tmp);
            }

        }
        return list;
    }
}
