package de.hsh.dbs2.imdb.logic;

import de.hsh.dbs2.imdb.persistenz.Factory.PersonFactory;
import de.hsh.dbs2.imdb.util.MovieDB_Connection;

import java.util.ArrayList;
import java.util.List;

public class PersonManager {

	/**
	 * Liefert eine Liste aller Personen, deren Name den Suchstring enthaelt.
	 * @param text Suchstring
	 * @return Liste mit passenden Personennamen, die in der Datenbank eingetragen sind.
	 * @throws Exception
	 */
	public List<String> getPersonList(String text) throws Exception {
		// TODO
		List<String> result;
		boolean ok = false;
		try {
			result = PersonFactory.getPerson(text);
			MovieDB_Connection.getConnection().commit();
			ok=true;
		}
		finally {
			if(!ok) {
				MovieDB_Connection.getConnection().rollback();
			}
		}


		return result;
	}

}
