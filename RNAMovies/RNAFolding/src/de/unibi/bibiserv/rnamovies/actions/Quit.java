package de.unibi.bibiserv.rnamovies.actions;

import java.awt.event.ActionEvent;

import de.unibi.bibiserv.rnamovies.Movie;
import de.unibi.bibiserv.rnamovies.RNAMovies;

public class Quit extends MovieAction {

  public Quit(RNAMovies movies) {
    super("Quit", "");
    this.movies = movies;
  }

  public void actionPerformed(ActionEvent e) {
    System.exit(0);
  }

}
