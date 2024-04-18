package de.hsh.dbs2.imdb.persistenz.Factory;


import de.hsh.dbs2.imdb.persistenz.activeRecord.*;
import de.hsh.dbs2.imdb.util.MovieDB_Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MovieFactory {
    List<Movie> movieList;

    /*
    Liest die Daten zu einer ID aus der DB ein
    und erzeugt ein entsprechendes Objekt
    Rückgabe ist ein ActiveRecord-Objekt mit dem Film
     */
    public static Movie findById(Long id) throws SQLException {
        Movie movie;
        String sql = "select * FROM movie WHERE id = ?;";
        try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            movie = new Movie(id);
            while (rs.next()) {
                movie.setTitle(rs.getString("title"));
                movie.setYear(rs.getInt("year"));
                movie.setType(rs.getString("type").charAt(0));
            }
        }
        return movie;
    }



    /*
            Liest alle Filme aus der DB ein, deren Titel den übergebenen String enthält
            Groß/Klein soll ignoriert werden
            Bsp. wenn der String „potter“ übergeben wird, wird „Harry Potter 3“ gefunden

            Rückgabe ist eine Liste von ActiveRecord-Objekten mit ggf. mehreren Filmen
             */
    public static List<Movie> findByTitle(String title) throws SQLException {
        List <Movie> movieList = new ArrayList<Movie>();
        String sql;
        if (title.isEmpty()) {
            sql = "SELECT * FROM movie;";
        } else {
            sql = "SELECT * FROM movie WHERE title LIKE ?;"; //COLLATE NOCASE
        }

        try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql)) {
            if (!title.isEmpty()) {
                pstmt.setString(1,"%"+title+"%" );
            }
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Movie movie = new Movie(rs.getLong("id"));
                movie.setTitle(rs.getString("title"));
                movie.setYear(rs.getInt("year"));
                movie.setType(rs.getString("type").charAt(0));
                movieList.add(movie);

                System.out.println(movie.getMovieID()+" "+movie.getTitle() +" "+ movie.getType()+" "+movie.getYear());
            }
        }
        return movieList;
    }

}
