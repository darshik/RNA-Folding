package de.unibi.bibiserv.rnamovies.actions;

import java.awt.event.ActionEvent;

import de.unibi.bibiserv.rnamovies.RNAMovies;

public class Reset extends MovieAction {

  public Reset(RNAMovies movies) {
    super("Reset", "");
    this.movies = movies;
  }

  public void actionPerformed(ActionEvent e) {
    movies.getMovie().reset();
  }

}
