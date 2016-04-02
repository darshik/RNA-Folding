/*
 * OpenAction.java
 *
 * Modified on January 24, 2007, 11:14 AM
 *
 */

package de.unibi.bibiserv.rnamovies.actions;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import de.unibi.bibiserv.rnamovies.RNAMovies;
import de.unibi.bibiserv.rnamovies.util.FileFilter;
import de.unibi.bibiserv.rnamovies.util.OpenAccessory;

/**
 * OpenDialog for RNAMovies.
 *
 * @author Alexander Kaiser <akaiser@techfak.uni-bielefeld.de>
 *         Jan Krueger <jkrueger@techfak.uni-bielefeld.de>
 */
public class OpenAction extends MovieAction {

  private static Logger log = Logger.getLogger("OpenAction");

  private File lastDir = null;

  public OpenAction(RNAMovies movies) {
    super("Open...", "eject");
    this.movies = movies;
  }

  public void actionPerformed(ActionEvent e) {
    int retval;
    File f = null;
    FileInputStream fis;
    JFileChooser chooser;

   if(lastDir == null) {
      chooser = new JFileChooser(System.getProperty("user.dir"));
    } else {
      chooser = new JFileChooser(lastDir);
    }
    
    //create FileFilter for RNM/DSCE files
    FileFilter filefilter = new FileFilter("RNAMovie/DCSE Files (test.rnm)");
    filefilter.addExtension("rnm");
    filefilter.addExtension("dcse");
    
    //create Accessory
    OpenAccessory accessory = new OpenAccessory();
    
    //add FileFilter
    chooser.setFileFilter(filefilter);
    
    //add Accessory
    chooser.setAccessory(accessory);

    retval = chooser.showOpenDialog(movies);
    if(retval != JFileChooser.APPROVE_OPTION)
      return;

    try {
      f = chooser.getSelectedFile();
      lastDir = chooser.getCurrentDirectory();
      fis = new FileInputStream(f);
      movies.setData(fis, accessory.isCenter());
      fis.close();

      Component c = movies.getParent();
      while (c != null && !(c instanceof Frame)) {
        c = c.getParent();
      }

      if(c != null)
        ((Frame)c).setTitle(RNAMovies.TITLE + " : " + f.getName());

      if(accessory.isFit()) {
        movies.getMovie().zoomFit();
      }

    } catch(java.io.FileNotFoundException ex) {
      JOptionPane.showMessageDialog(movies,
                                    "Error reading file:\n"
                                    + ex.getMessage(),
                                    ex.getClass().getName(),
                                    JOptionPane.ERROR_MESSAGE);
      log.severe(ex.getMessage());
    } catch(IOException ex) {
      JOptionPane.showMessageDialog(movies,
                                    "Error reading file:\n"
                                    + ex.getMessage(),
                                    ex.getClass().getName(),
                                    JOptionPane.ERROR_MESSAGE);
      log.severe(ex.getMessage());
    } catch(IllegalArgumentException ex) {
      JOptionPane.showMessageDialog(movies,
                                    "Error in file:\n"
                                    + ex.getMessage(),
                                    ex.getClass().getName(),
                                    JOptionPane.ERROR_MESSAGE);
    }
  }
}
