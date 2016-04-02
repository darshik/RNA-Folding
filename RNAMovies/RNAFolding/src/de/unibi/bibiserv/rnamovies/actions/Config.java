package de.unibi.bibiserv.rnamovies.actions;

import java.awt.event.ActionEvent;

import de.unibi.bibiserv.rnamovies.configuration.dialog.ConfigDialog;
import de.unibi.bibiserv.rnamovies.RNAMovies;

public class Config extends MovieAction {

  private ConfigDialog cd = null;

  public Config(RNAMovies movies) {
    super("Configure...", "");
    this.movies = movies;
  }

  public void actionPerformed(ActionEvent e) {
    if(cd == null)
      cd = new ConfigDialog(movies.getConfiguration());
    cd.showDialog(movies);
  }

}
