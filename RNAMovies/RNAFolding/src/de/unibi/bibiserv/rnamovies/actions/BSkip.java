package de.unibi.bibiserv.rnamovies.actions;

import java.awt.event.ActionEvent;

import de.unibi.bibiserv.rnamovies.RNAMovies;

public class BSkip extends MovieAction {

  public BSkip(RNAMovies movies) {
    super("Previous", "rev");
    this.movies = movies;
  }

  public void actionPerformed(ActionEvent e) {
    movies.getMovie().bskip();
  }

}
