package de.hsh.dbs2.imdb.persistenz.activeRecord;

import de.hsh.dbs2.imdb.util.MovieDB_Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Person {
    private Long personID;
    private String name;
    private char sex;

    public Long getPersonID() { return personID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public char getSex() { return sex; }
    public void setSex(char sex) { this.sex = sex; }

    public Person() {

    }
    public Person(long personID) {
        this.personID=personID;
    }


    public void insert() throws SQLException {
        if (personID == null) {
            String sql = "INSERT INTO person VALUES(NULL, ?, ?);";
            String sqlId = "SELECT last_insert_rowid();";
            ResultSet rs;
            try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql/*, Statement.RETURN_GENERATED_KEYS*/)) {
                pstmt.setString(1, getName());
                pstmt.setString(2, String.valueOf(getSex()));


                pstmt.executeUpdate();
            }
            try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sqlId)) {
                rs= pstmt.executeQuery();
                personID= rs.getLong(1);
            }
        }
    }

    public void update() throws  SQLException{
        if (personID != null) {
            String sql = "UPDATE Person SET name=?, sex=? WHERE id=?;";
            ResultSet rs;
            try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql)) {
                pstmt.setString(1, getName());
                pstmt.setString(2, String.valueOf(getSex()));
                pstmt.setLong(3, getPersonID());

                pstmt.executeUpdate();
            }
        }
    }

    public void delete() throws SQLException {
        if (personID != null) {
            String sql = "DELETE FROM Person WHERE id=?;";
            try (PreparedStatement pstmt = MovieDB_Connection.getConnection().prepareStatement(sql/*, Statement.RETURN_GENERATED_KEYS*/)) {
                pstmt.setLong(1, getPersonID());

                pstmt.executeUpdate();
            }
        }
    }
}
