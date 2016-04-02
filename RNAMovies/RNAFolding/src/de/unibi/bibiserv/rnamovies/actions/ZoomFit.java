package de.unibi.bibiserv.rnamovies.actions;

import java.awt.event.ActionEvent;

import de.unibi.bibiserv.rnamovies.RNAMovies;

public class ZoomFit extends MovieAction {

  public ZoomFit(RNAMovies movies) {
    super("Fit Window", "");
    this.movies = movies;
  }

  public void actionPerformed(ActionEvent e) {
    movies.getMovie().zoomFit();
  }

}
