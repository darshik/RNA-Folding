package de.unibi.bibiserv.rnamovies.actions;

import java.awt.event.ActionEvent;

import de.unibi.bibiserv.rnamovies.Movie;
import de.unibi.bibiserv.rnamovies.RNAMovies;

public class ZoomIn extends MovieAction {

  public ZoomIn(RNAMovies movies) {
    super("Zoom In", "");
    this.movies = movies;
  }

  public void actionPerformed(ActionEvent e) {
    int f, g;

    f = movies.getMovie().getZoom();
    if(f < Movie.MAX_ZOOM) {
      g = f%25;
      movies.getMovie().setZoom(g == 0 ? f + 25 : f + 25 - g);
    }
  }

}
