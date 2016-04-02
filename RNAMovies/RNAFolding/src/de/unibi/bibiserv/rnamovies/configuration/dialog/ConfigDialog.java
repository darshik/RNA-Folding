package de.unibi.bibiserv.rnamovies.configuration.dialog;

import java.util.Iterator;
import java.util.Map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import de.unibi.bibiserv.rnamovies.configuration.Configuration;
import de.unibi.bibiserv.rnamovies.configuration.Category;
import de.unibi.bibiserv.rnamovies.configuration.TypeWrapper;

public class ConfigDialog {

  private Configuration config;

  private JDialog dialog = new JDialog();

  public ConfigDialog(Configuration config) {
    String key;
    Map<String, Category> categories;
    Iterator<String> keys;
    Container contentPane;
    JButton done;
    JTabbedPane tabs;
    JPanel bottom;

    dialog.setTitle("Configuration");
    dialog.setResizable(false);
    contentPane = dialog.getContentPane();
    contentPane.setLayout(new BorderLayout());
    tabs = new JTabbedPane();
    tabs.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
    contentPane.add(tabs, BorderLayout.CENTER);
    bottom = new JPanel();
    bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    bottom.setLayout(new BorderLayout());
    done = new JButton("Done");
    done.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {dialog.setVisible(false);}});
    bottom.add(done, BorderLayout.EAST);
    contentPane.add(bottom, BorderLayout.SOUTH);

    this.config = config;
    categories = config.getCategories();
    for(keys = categories.keySet().iterator(); keys.hasNext();) {
      key = keys.next();
      tabs.addTab(key, makeInputMask(categories.get(key)));
    }
    dialog.pack();
  }

  public void showDialog(Component parent) {
    if(!dialog.isVisible()) {
      dialog.setLocationRelativeTo(parent);
      dialog.setVisible(true);
    }
  }

  private static JPanel makeInputMask(Category cat) {
    String key;
    Map<String, TypeWrapper> vals;
    TypeWrapper tw;
    Iterator<String> keys;
    Component c;
    JLabel l;
    JSlider slider;
    AbstractButton b;
    ButtonGroup group;
    GridBagLayout gridBag;
    GridBagConstraints gridC;

    JPanel jp;
    jp = new JPanel();
    jp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    gridBag = new GridBagLayout();
    gridC = new GridBagConstraints();
    gridC.fill = GridBagConstraints.NONE;
    gridC.insets = new Insets(2, 2, 2, 2);
    gridC.weightx = 1.0;
    gridC.anchor = GridBagConstraints.WEST;

    jp.setLayout(gridBag);

    vals = cat.getValues();
    for(keys = vals.keySet().iterator(); keys.hasNext();) {
      key = keys.next();
      tw = vals.get(key);

      if(tw.getObject() instanceof Boolean) {
        boolean value;
        value = ((Boolean)tw.getObject()).booleanValue();

        l = new JLabel(key);
        l.setHorizontalAlignment(JLabel.RIGHT);
        gridBag.setConstraints(l, gridC);
        jp.add(l);

        group = new ButtonGroup();
        b = new JRadioButton(tw.contains("trueLabel") ? tw.getAttribute("trueLabel") : "true", value);
        b.addChangeListener(new BooleanListener(cat, key));
        gridBag.setConstraints(b, gridC);
        jp.add(b); 
        group.add(b);

        gridC.gridwidth = GridBagConstraints.REMAINDER;
        b = new JRadioButton(tw.contains("falseLabel") ? tw.getAttribute("falseLabel") : "false", !value);
        gridBag.setConstraints(b, gridC);
        jp.add(b);
        group.add(b);
        gridC.gridwidth = 1; 
      } else if(tw.getObject() instanceof Integer) {
        JTextField input;
        l = new JLabel(key);
        l.setHorizontalAlignment(JLabel.RIGHT);
        gridBag.setConstraints(l, gridC);
        jp.add(l);

        input = new JTextField(((Integer)tw.getObject()).toString(), 10);
        gridC.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.setConstraints(input, gridC);
        jp.add(input);
        input.addActionListener(new IntegerListener(cat, key));
        gridC.gridwidth = 1;
      } else if(tw.getObject() instanceof Color) {
        l = new JLabel(key);
        l.setHorizontalAlignment(JLabel.RIGHT);
        gridBag.setConstraints(l, gridC);
        jp.add(l);

        gridC.gridwidth = GridBagConstraints.REMAINDER;
        b = new JButton(colorIcon((Color)tw.getObject()));
        b.addActionListener(new ColorListener(jp, cat, key, (Color)tw.getObject()));
        gridBag.setConstraints(b, gridC);
        jp.add(b);
        gridC.gridwidth = 1;
      } else if(tw.getObject() instanceof BoundedRangeModel) {
        l = new JLabel(key);
        l.setHorizontalAlignment(JLabel.RIGHT);
        gridBag.setConstraints(l, gridC);
        jp.add(l);

        gridC.gridwidth = GridBagConstraints.REMAINDER;
        slider = new JSlider((BoundedRangeModel)tw.getObject());
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing((slider.getMaximum()-slider.getMinimum())/4);
        slider.addChangeListener(new SliderListener(cat, key));
        gridBag.setConstraints(slider, gridC);
        jp.add(slider);
        gridC.gridwidth = 1;
      }
    }

    return jp;
  }

  private static class IntegerListener implements ActionListener {
    private Category category;
    private String name;

    public IntegerListener(Category category, String name) {
      this.category = category;
      this.name = name;
    }

    public void actionPerformed(ActionEvent e) {
      JTextField src;
      int value;

      if(!(e.getSource() instanceof JTextField))
        return;

      src = (JTextField)e.getSource();

      try {
        value = Integer.parseInt(src.getText());
        category.set(name, value);
      } catch(NumberFormatException ex) {
        src.setText(String.valueOf(category.getInt(name)));
      }
    }
  }

  private static class SliderListener implements ChangeListener {
    private Category category;
    private String name;

    public SliderListener(Category category, String name) {
      this.category = category;
      this.name = name;
    }

    public void stateChanged(ChangeEvent e) {
      JSlider src;

      if(!(e.getSource() instanceof JSlider))
        return;

      src = (JSlider) e.getSource();
      category.set(name, (javax.swing.DefaultBoundedRangeModel)src.getModel());
    }

  }

  private static class BooleanListener implements ChangeListener {
    private Category category;
    private String name;

    public BooleanListener(Category category, String name) {
      this.category = category;
      this.name = name;
    }

    public void stateChanged(ChangeEvent e) {
      AbstractButton src;

      if(!(e.getSource() instanceof AbstractButton))
        return;

      src = (AbstractButton) e.getSource();
      category.set(name, src.isSelected());
    }
  }

  private static class ColorListener implements ActionListener {
    private Category category;
    private Component parent;
    private String name;
    private Color c;

    public ColorListener(Component parent, Category category, String name, Color c) {
      this.parent = parent;
      this.category = category;
      this.name = name;
      this.c = c;
    }

    public void actionPerformed(ActionEvent e) {
      Color tmp;
      AbstractButton src;
      Graphics g;

      if(!(e.getSource() instanceof AbstractButton))
        return;

      src = (AbstractButton) e.getSource();

      if(!(src.getIcon() instanceof ImageIcon))
        return;

      g = ((ImageIcon)src.getIcon()).getImage().getGraphics();
      tmp = (new JColorChooser()).showDialog(parent, name, c);
      if(tmp == null)
        return;

      c = tmp;
      category.set(name, tmp);
      g.setColor(tmp);
      g.fillRect(2, 2, 28, 20);
    }
  }

  private static Icon colorIcon(Color c) {
    Graphics g;
    Image i;

    i = new BufferedImage(32, 24, BufferedImage.TYPE_3BYTE_BGR);
    g = i.getGraphics();
    g.setColor(c);
    g.fillRect(2, 2, 28, 20);

    return new ImageIcon(i);
  }

}
