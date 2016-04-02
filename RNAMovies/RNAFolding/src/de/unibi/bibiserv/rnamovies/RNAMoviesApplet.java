package de.unibi.bibiserv.rnamovies;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.FilePermission;

import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.border.BevelBorder;

public class RNAMoviesApplet extends JApplet {

  private RNAMovies movies;

  public RNAMoviesApplet() {
    boolean fileperm = false;
    SecurityManager sm;

    // Check if we are allowed to access local files. If so load a fully fleged
    // set of actions (OpenAction & Export).
    sm = System.getSecurityManager();
    if(sm != null) {
      try {
        sm.checkPermission(new FilePermission("<<ALL FILES>>", "read"));
        fileperm = true;
      } catch(SecurityException e) {
        fileperm = false;
      }
    }

    movies = new RNAMovies(getClass().getResourceAsStream("config.xml"),
                           getClass().getResourceAsStream(fileperm ? "applet-actions.xml"  : "unsigned-applet-actions.xml"));
    movies.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    setContentPane(movies);
  }

 /**
  * Set the movie script.
  *
  * @param data A String containing a valid script.
  */
  public void setData(String data) {
    try {
      movies.setData(data);
      movies.getMovie().zoomFit();
    } catch(IllegalArgumentException ex) {
      JOptionPane.showMessageDialog(movies,
                                    "Error in Input:\n"
                                    + ex.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

 /**
  * Set the movie script.
  *
  * @param data A String containing a valid script.
  * @param center true if the structures should be centered
  * @param fit true if the aspect should be zoomed to fit the
  *            largest structure
  */
  public void setData(String data, boolean center, boolean fit) {
    try {
      movies.setData(data, center);

      if(fit)
        movies.getMovie().zoomFit();
    } catch(IllegalArgumentException ex) {
      JOptionPane.showMessageDialog(movies,
                                    "Error in Input:\n"
                                    + ex.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }


 /**
  * Get the actual position within the script. Enumeration starts at 1.
  *
  * @return The position.
  */
  public int getPosition() {
    return movies.getMovie().getFrameIdx() + 1;
  }

}
