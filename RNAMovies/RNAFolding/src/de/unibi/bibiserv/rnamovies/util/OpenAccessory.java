/*
 * OpenAccessory.java
 *
 * Created on January 25, 2007, 11:34 AM
 *

 */

package de.unibi.bibiserv.rnamovies.util;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Accessory component for RNAMovies file open dialog
 *
 * @author Jan Krueger <jkrueger@techfak.uni-bielefeld.de>
 *         Alexander Kaiser <akaiser@techfak.uni-bielefeld.de>
 */
public class OpenAccessory extends JPanel{
    
    private boolean fit = true;
    
    private boolean center = false;
    
    /** Creates a new instance of OpenAccessory */
    public OpenAccessory() {
      Border border;
      GridBagLayout gbl;
      GridBagConstraints gbc;
      JCheckBox jcb;

      border = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
      setBorder(BorderFactory.createTitledBorder(border, "Options"));

      gbl = new GridBagLayout();
      setLayout(gbl);
      gbc = new GridBagConstraints();
      gbc.fill = GridBagConstraints.BOTH;
      gbc.weightx = 1.0;

      gbc.gridwidth = GridBagConstraints.REMAINDER;
      jcb = new JCheckBox("Center", center);
      jcb.addChangeListener(
              new ChangeListener(){
                public void stateChanged(ChangeEvent e){
                    center=((AbstractButton)e.getSource()).isSelected();}});
      gbl.setConstraints(jcb, gbc);
      add(jcb);

      gbc.gridwidth = GridBagConstraints.REMAINDER;
      jcb = new JCheckBox("Fit Window", fit);
      gbl.setConstraints(jcb, gbc);
      jcb.addChangeListener(
                new ChangeListener(){
                    public void stateChanged(ChangeEvent e){
                        fit=((AbstractButton)e.getSource()).isSelected();}});
      add(jcb);
    }
    
    public boolean isFit(){
        return fit;
    }
    
    public boolean isCenter(){
        return center;
    }
    
    
    
    
}
