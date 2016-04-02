package de.unibi.bibiserv.rnamovies.actions;

import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import de.unibi.bibiserv.rnamovies.RNAMovies;

public abstract class MovieAction extends AbstractAction {

  protected RNAMovies movies;
  
  public MovieAction(String name, String iconName){
      //super("Open...", loadIcon("eject", "Open.."));
      super(name, loadIcon(iconName, name));
  }

//  public MovieAction(String name, String iconName) {
//      name="Open...";
//      iconName="eject";
//    super(name, loadIcon(iconName, name));
//  }

  private static ImageIcon loadIcon(String name, String description) {
    URL imageURL = MovieAction.class.getResource("icons/" + name.replaceAll(" ", "_")+ ".png");
    ImageIcon icon = null;
    if (imageURL!= null)
      icon = new ImageIcon(imageURL, description);

    return icon;
  }
}
