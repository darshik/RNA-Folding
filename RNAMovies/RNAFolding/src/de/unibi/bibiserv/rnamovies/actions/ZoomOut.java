package de.unibi.bibiserv.rnamovies.actions;

import java.awt.event.ActionEvent;

import de.unibi.bibiserv.rnamovies.Movie;
import de.unibi.bibiserv.rnamovies.RNAMovies;

public class ZoomOut extends MovieAction {

  public ZoomOut(RNAMovies movies) {
    super("Zoom Out", "");
    this.movies = movies;
  }

  public void actionPerformed(ActionEvent e) {
    int f, g;

    f = movies.getMovie().getZoom();
    if(f > Movie.MIN_ZOOM) {
      g = f%25;
      movies.getMovie().setZoom(g == 0 ? f - 25 : f - g);
    }
  }
}
