package de.unibi.bibiserv.rnamovies.actions;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import de.unibi.bibiserv.rnamovies.RNAMovies;

public class About extends MovieAction {

  public About(RNAMovies movies) {
    super("About...", "");
    this.movies = movies;
  }

  public void actionPerformed(ActionEvent e) {
    JTextArea jta;

    jta = new JTextArea("RNA Movies 2.04 by Alexander Kaiser <akaiser@techfak.uni-bielefeld.de>\n"+
            "Developed at the Bielefeld Bioinformatics Server (BiBiServ) at Bielefeld University\n\n"+
            "References:\n\n"+
            "Robert Giegerich, Dirk J. Evers\n"+
            "RNA Movies: visualizing RNA secondary structure spaces\n   "+
            "Bioinformatics (formerly CABIOS), Volume 15, Issue 1, January 1999, pp. 32-37, OUP Press\n\n  "+
            "Robert E. Bruccoleri, Gerhard Heinrich\n   "+
            "An improved algorithm for nucleic acid secondary structure display\n   "+
            "CABIOS, Volume 4, Issue 1, 1988, pp. 167-173, IRL Press");
  
    jta.setEditable(false);
    JOptionPane.showMessageDialog(movies,
                                  jta,
                                  "About...",
                                  JOptionPane.INFORMATION_MESSAGE);
  }
}
