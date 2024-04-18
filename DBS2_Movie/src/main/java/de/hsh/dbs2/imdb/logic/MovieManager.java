package de.hsh.dbs2.imdb.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hsh.dbs2.imdb.persistenz.Factory.*;
import de.hsh.dbs2.imdb.persistenz.activeRecord.*;
import de.hsh.dbs2.imdb.logic.dto.*;
import de.hsh.dbs2.imdb.util.MovieDB_Connection;

public class MovieManager {

	/**
	 * Ermittelt alle Filme, deren Filmtitel den Suchstring enthaelt.
	 * Wenn der String leer ist, sollen alle Filme zurueckgegeben werden.
	 * Der Suchstring soll ohne Ruecksicht auf Gross-/Kleinschreibung verarbeitet werden.
	 * @param search Suchstring. 
	 * @return Liste aller passenden Filme als MovieDTO
	 * @throws Exception
	 */
	public List<MovieDTO> getMovieList(String search) throws Exception {
		List<Movie> movieList;
		List<MovieDTO> movieDTOList;
		boolean ok= false;
		try {
			movieList = MovieFactory.findByTitle(search);
			movieDTOList = new ArrayList<>();

			for (Movie movie : movieList) {
				movieDTOList.add(getMovie(movie.getMovieID()));
			}

			MovieDB_Connection.getConnection().commit();
			ok=true;
		} finally {
			if (!ok) {
				MovieDB_Connection.getConnection().rollback();
			}
		}
		return movieDTOList;
	}

	/**
	 * Speichert die uebergebene Version des Films neu in der Datenbank oder aktualisiert den
	 * existierenden Film.
	 * Dazu werden die Daten des Films selbst (Titel, Jahr, Typ) beruecksichtigt,
	 * aber auch alle Genres, die dem Film zugeordnet sind und die Liste der Charaktere
	 * auf den neuen Stand gebracht.
	 * @param movieDTO Film-Objekt mit Genres und Charakteren.
	 * @throws Exception
	 */
	public void insertUpdateMovie(MovieDTO movieDTO) throws Exception {
		// TODO
		boolean ok= false;
		try {
			// wenn movie nicht vorhanden insert() und in den movieDTOParameter setten
			// wenn movie vorhanden movie,character,genre update = movieDTOParameter.get...
			Movie movie;
			if (movieDTO.getId() == null) {
				movie = new Movie();
				movie.setType(movieDTO.getType().charAt(0));
				movie.setTitle(movieDTO.getTitle());
				movie.setYear(movieDTO.getYear());
				movie.insert();
				movieDTO.setId(movie.getMovieID());
			} else {
				movie = new Movie(movieDTO.getId());
				movie.setType(movieDTO.getType().charAt(0));
				movie.setTitle(movieDTO.getTitle());
				movie.setYear(movieDTO.getYear());
				movie.update();
			}


			//DELETE ALL HASGENRE FROM THE DB
			for (HasGenre hg : HasGenreFactory.findByGenreID(movieDTO.getId())) {
				hg.delete();
			}

			//INSERT THE UPDATES HASGENRE'S
			ArrayList<HasGenre> hasGenresList = new ArrayList<>();
			for(String genre : movieDTO.getGenres() ) {
				ArrayList<Genre> genreList = GenreFactory.findByTitle(genre);

				if(genreList.isEmpty()) {
					Genre g = new Genre();
					g.setGenre(genre);

					g.insert();
					genreList.add(g);
				}

				for (Genre genre1 : genreList) {
					HasGenre hasGenre = new HasGenre();
					hasGenre.setGenreID(genre1.getGenreID());
					hasGenre.setMovieID(movie.getMovieID());

					hasGenre.insert();
				}
			}
			//DELETE ALL THE ORIGINAL CHARACTERS
			for (MovieCharacter mC : MovCharacterFactory.findCharacterByMovieId(movie.getMovieID())) {
				mC.delete();
			}

			List<CharacterDTO> characterDTOList = movieDTO.getCharacters();

			int pos = 0;
			//INSERT/UPDATE CHARACTERS
			for (CharacterDTO characterDTO: characterDTOList) {
				Person person;
				//überprüfen ob Person bereits existiert (vorher keine Person gelöscht weil unabhängig)
				Person p2 = PersonFactory.findPersonByName(characterDTO.getPlayer()) ;
				if(p2 == null) {
					/*funktioniert nicht: character kann in der gui nur aus der Liste existierender Namen einem Personnamen zugewiesen werden
					* es kann aber keine neue Person erstellt werden
					* characterDTO wird bei einer nicht existierenden Person null zugewiesen-> somit ist characterDTO.getPlayer()=null und der Name der person somit dann auch
					* Ver*/
					person = new Person();
					person.setName(characterDTO.getPlayer());
					person.setSex('m');
					person.insert();
				} else {
					person = new Person(p2.getPersonID());
					person.setName(characterDTO.getPlayer());
//					person.setSex('x');
					person.update();
				}

				MovieCharacter	movieCharacter = new MovieCharacter();
				movieCharacter.setAlias(characterDTO.getAlias());
				movieCharacter.setCharacter(characterDTO.getCharacter());
				movieCharacter.setPosition(pos);
				movieCharacter.setPerID(person.getPersonID());
				movieCharacter.setMovieID(movie.getMovieID());
				movieCharacter.insert(); //update nicht nötig, weil movieCharacter.delete()

				++pos;
			}

			MovieDB_Connection.getConnection().commit();
			ok=true;
		} finally {
			if (!ok) {
				MovieDB_Connection.getConnection().rollback();
			}
		}
	}

	/**
	 * Loescht einen Film aus der Datenbank. Es werden auch alle abhaengigen Objekte geloescht,
	 * d.h. alle Charaktere und alle Genre-Zuordnungen.
	 * @param movieId
	 * @throws Exception
	 */
	public void deleteMovie(Long movieId) throws Exception {
		// TODO
		// lösche zuerst MovieCharacter, dann hasGenre und dann Movie


		boolean ok= false;
		try {
			List<MovieCharacter> movieCharacterList = MovCharacterFactory.findCharacterByMovieId(movieId);
			for (MovieCharacter movieCharacter : movieCharacterList) {
				movieCharacter.delete();
			}

			HasGenreFactory hasGenreFactory = new HasGenreFactory();
			List<HasGenre> hasGenresList = hasGenreFactory.findByMovieID(movieId);
			for (HasGenre hasGenre : hasGenresList) {
				hasGenre.delete();
			}

			Movie movie = new Movie(movieId);
			movie.delete();



			MovieDB_Connection.getConnection().commit();
			ok=true;
		} finally {
			if (!ok) {
				MovieDB_Connection.getConnection().rollback();
			}
		}
	}

	/**
	 * Liefert die Daten eines einzelnen Movies zurück
	 * @param movieId
	 * @return movieDTO
	 * @throws Exception
	 */
	public MovieDTO getMovie(long movieId) throws Exception {

		boolean ok= false;
		MovieDTO movieDTO = new MovieDTO();
		Movie movie;

		try {

			movie = MovieFactory.findById(movieId);
			movieDTO.setType(String.valueOf(movie.getType()));
			movieDTO.setYear(movie.getYear());
			movieDTO.setTitle(movie.getTitle());
			movieDTO.setId(movieId);


//			Set<String> genreSet = new HashSet<>();
			List<Genre> genreList = GenreFactory.findGenreByMovieId(movieId);
			for (Genre genre : genreList) {
				movieDTO.addGenre(genre.getGenre());
//				genreSet.add(genre.getGenre());
			}
//			movieDTO.setGenres(genreSet);



			List<MovieCharacter> movieCharacterList = MovCharacterFactory.findCharacterByMovieId(movieId);

			List<Person> personList = PersonFactory.findPerByMovId(movieId);
//			List<CharacterDTO> characterDTOList = new ArrayList<>();

			for (MovieCharacter movieCharacter : movieCharacterList) {
				CharacterDTO characterDTO = new CharacterDTO();
				characterDTO.setCharacter(movieCharacter.getCharacter());
				characterDTO.setAlias(movieCharacter.getAlias());
//				characterDTO.setPlayer(personList.get(i).getName());

				for (Person p : personList) {
					if (p.getPersonID() == movieCharacter.getPerID()) {
						characterDTO.setPlayer(p.getName());
						break;
					}
				}
				movieDTO.addCharacter(characterDTO);
//				characterDTOList.add(characterDTO);

			}
//			movieDTO.setCharacters(characterDTOList);

			MovieDB_Connection.getConnection().commit();
			ok=true;
		} finally {
			if (!ok) {
				MovieDB_Connection.getConnection().rollback();
			}
		}

		return movieDTO;
	}

}
