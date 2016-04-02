package de.unibi.bibiserv.rnamovies.actions;

import java.awt.event.ActionEvent;

import de.unibi.bibiserv.rnamovies.RNAMovies;

public class StartAction extends MovieAction {

  public StartAction(RNAMovies movies) {
    super("Play", "play");
    this.movies = movies;
  }

  public void actionPerformed(ActionEvent e) {
    movies.getMovie().start();
  }

}
