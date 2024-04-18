package de.hsh.dbs2.imdb.persistenz.Factory;
import de.hsh.dbs2.imdb.persistenz.activeRecord.*;
import de.hsh.dbs2.imdb.util.MovieDB_Connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonFactory {
    public static List<Person> findPerByMovId(long id) throws SQLException {
        List<Person> personMovList = new ArrayList<>();
        String sql = "select * FROM person p join MovieCharacter mc ON p.id=mc.perID WHERE mc.movId = ? ORDER BY mc.position;";
        try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Person person = new Person(rs.getLong(1));
                person.setName(rs.getString(2));
                person.setSex(rs.getString(3).charAt(0));

                personMovList.add(person);
            }
        }
        return personMovList;
    }

    public static List<String> getPerson(String text) throws SQLException{
        List<String> result = new ArrayList<String>();
        String person = "SELECT Name FROM Person Where name LIKE ?;";
        try (PreparedStatement stmt =  MovieDB_Connection.getConnection().prepareStatement(person)) {
            stmt.setString(1, "%"+text+"%" );
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                result.add(rs.getString("Name"));
            }
        }

        return result;
    }

    public static Person findPersonByName(String text) throws SQLException{
        Person person = null ;
        String sql = "SELECT * FROM person Where name LIKE ?;";
        try (PreparedStatement stmt =  MovieDB_Connection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, text );
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                person = new Person(rs.getLong("id"));
                person.setName(rs.getString("Name"));
                person.setSex(rs.getString("sex").charAt(0));
            }
        }

        return person;
    }
}
