package de.unibi.bibiserv.rnamovies.actions;

import java.awt.event.ActionEvent;

import javax.swing.JSpinner;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;

import de.unibi.bibiserv.rnamovies.RNAMovies;

public class Goto extends MovieAction {

  public Goto(RNAMovies movies) {
    super("Goto Structure...", "");
    this.movies = movies;
  }

  public void actionPerformed(ActionEvent e) {
    int size, retval;
    JSpinner spinner;

    size = movies.getMovie().numFrames();

    if(size < 1)
      return;

    spinner = new JSpinner(new SpinnerNumberModel(1, 1, size, 1));
    retval = JOptionPane.showConfirmDialog(movies, spinner, "Goto Structure...",
                                           JOptionPane.OK_CANCEL_OPTION,
                                           JOptionPane.PLAIN_MESSAGE);

    if(retval != JOptionPane.OK_OPTION)
      return;

    movies.getMovie().gotoFrame(((Integer)spinner.getValue()).intValue() - 1);
  }
}
