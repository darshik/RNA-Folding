package de.unibi.bibiserv.rnamovies.actions;

import java.awt.event.ActionEvent;

import de.unibi.bibiserv.rnamovies.RNAMovies;

public class StopAction extends MovieAction {

  public StopAction(RNAMovies movies) {
    super("Stop", "stop");
    this.movies = movies;
  }

  public void actionPerformed(ActionEvent e) {
    movies.getMovie().stop();
  }

}
