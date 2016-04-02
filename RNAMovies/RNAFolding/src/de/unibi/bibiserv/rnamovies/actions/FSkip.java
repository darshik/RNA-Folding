package de.unibi.bibiserv.rnamovies.actions;

import java.awt.event.ActionEvent;

import de.unibi.bibiserv.rnamovies.RNAMovies;

public class FSkip extends MovieAction {

  public FSkip(RNAMovies movies) {
    super("Next", "fwd");
    this.movies = movies;
  }

  public void actionPerformed(ActionEvent e) {
    movies.getMovie().fskip();
  }

}
