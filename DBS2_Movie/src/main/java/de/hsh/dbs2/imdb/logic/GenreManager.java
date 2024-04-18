package de.hsh.dbs2.imdb.logic;

import java.util.ArrayList;
import java.util.List;
import de.hsh.dbs2.imdb.persistenz.activeRecord.*;
import de.hsh.dbs2.imdb.persistenz.Factory.*;
import de.hsh.dbs2.imdb.util.MovieDB_Connection;
import java.sql.SQLException;


/*
nur commit und rollback in Manager
mit activrecord hier arbeien
 */
public class GenreManager {

	/**
	 * Ermittelt eine vollstaendige Liste aller in der Datenbank abgelegten Genres
	 * Die Genres werden alphabetisch sortiert zurueckgeliefert.
	 * @return Alle Genre-Namen als String-Liste
	 * @throws Exception
	 */
	public static List<String> getGenres() throws Exception {
		// TODO
		ArrayList<String> genres = new ArrayList<>();
		ArrayList<Genre> genresObjects;
		boolean ok = false;

		try {
			genresObjects = GenreFactory.findAll();
			for (Genre g : genresObjects) {
				genres.add(g.getGenre());
			}
			MovieDB_Connection.getConnection().commit();
			ok = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (!ok) {
				MovieDB_Connection.getConnection().rollback();
			}
		}
		return genres;
	}

}
