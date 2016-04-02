package de.unibi.bibiserv.rnamovies.util;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Component;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

import javax.swing.event.*;

/** A class to monitor the progress of some operation. If it looks
 * like the operation will take a while, a progress dialog will be popped up.
 * When the ProgressMonitor is created it is given a numeric range and a
 * descriptive string. As the operation progresses, call the setProgress method
 * to indicate how far along the [min,max] range the operation is.
 * Initially, there is no ProgressDialog. After the first millisToDecideToPopup
 * milliseconds (default 500) the progress monitor will predict how long
 * the operation will take.  If it is longer than millisToPopup (default 2000,
 * 2 seconds) a ProgressDialog will be popped up.
 * <p>
 * From time to time, when the Dialog box is visible, the progress bar will
 * be updated when setProgress is called.  setProgress won't always update
 * the progress bar, it will only be done if the amount of progress is
 * visibly significant.
 *
 * <p>
 * 
 * For further documentation and examples see
 * <a
 href="http://java.sun.com/docs/books/tutorial/uiswing/components/progress.html">How to Monitor Progress</a>,
 * a section in <em>The Java Tutorial.</em>
 *
 * @see ProgressMonitorInputStream
 * @author James Gosling
 * @author Lynn Monsanto (accessibility) 
 * @version 1.30 04/15/04
 */
public class ModalProgressMonitor extends Object {
  private JDialog         dialog;
  private JOptionPane     pane;
  private JProgressBar    myBar;
  private JLabel          noteLabel;
  private String          note;
  private String          message;
  private Object[]        cancelOption = null;
  private int             min;
  private int             max;
  private int             v;
  private int             lastDisp;
  private int             reportDelta;

  /**
   * Constructs a graphic object that shows progress, typically by filling
   * in a rectangular bar as the process nears completion.
   *
   * @param message a descriptive message that will be shown
   *        to the user to indicate what operation is being monitored.
   *        This does not change as the operation progresses.
   *        See the message parameters to methods in
   *        {@link JOptionPane#message}
   *        for the range of values.
   * @param note a short note describing the state of the
   *        operation.  As the operation progresses, you can call
   *        setNote to change the note displayed.  This is used,
   *        for example, in operations that iterate through a
   *        list of files to show the name of the file being processes.
   *        If note is initially null, there will be no note line
   *        in the dialog box and setNote will be ineffective
   * @param min the lower bound of the range
   * @param max the upper bound of the range
   * @see JDialog
   * @see JOptionPane
   */
  public ModalProgressMonitor(String message, String note, int min, int max) {
    Container contentPane;

    this.min = min;
    this.max = max;
    this.message = message;
    this.note = note;

    noteLabel = new JLabel(note);
    myBar = new JProgressBar(min, max);

    cancelOption = new Object[1];
    cancelOption[0] = UIManager.getString("OptionPane.cancelButtonText");

    reportDelta = (max - min) / 100;
    if(reportDelta < 1)
      reportDelta = 1;
    v = min;

    pane = new ProgressOptionPane(new Object[] {noteLabel, myBar});

    dialog = new JDialog((Frame)null, message, true);
    dialog.setResizable(false);
    dialog.addWindowListener(new WindowAdapter() {
      boolean gotFocus = false;

      public void windowClosing(WindowEvent we) {
        pane.setValue(cancelOption[0]);
      }

      public void windowActivated(WindowEvent we) {
        // Once window gets focus, set initial focus
        if (!gotFocus) {
          pane.selectInitialValue();
          gotFocus = true;
        }
      }
    });

    pane.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent event) {
        if(dialog.isVisible() && event.getSource() == pane &&
           (event.getPropertyName().equals(JOptionPane.VALUE_PROPERTY) ||
            event.getPropertyName().equals(JOptionPane.INPUT_VALUE_PROPERTY))){
          dialog.setVisible(false);
          dialog.dispose();
        }
      }
    });

    contentPane = dialog.getContentPane();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(pane, BorderLayout.CENTER);

  }

  /**
   * Show this ModalProgressMonitor.
   *
   * @param parent The parent Component of this Dialog.
   *
   * @see #close
   */
  public void show(Component parent) {
    dialog.setLocationRelativeTo(parent);
    dialog.pack();
    dialog.setVisible(true);
  } 

  /** 
   * Indicate that the operation is complete.  This happens automatically
   * when the value set by setProgress is >= max, but it may be called
   * earlier if the operation ends early.
   */
  public void close() {
    if(dialog != null) {
      dialog.setVisible(false);
      dialog.dispose();
      dialog = null;
      pane = null;
      myBar = null;
    }
  }

  /** 
   * Indicate the progress of the operation being monitored.
   * If the specified value is >= the maximum, the progress
   * monitor is closed. 
   * @param nv an int specifying the current value, between the
   *        maximum and minimum specified for this component
   * @see #setMinimum
   * @see #setMaximum
   */
  public void setProgress(int nv) {
    v = nv;
    if (nv >= max) {
      close();
    } else if (nv >= lastDisp + reportDelta) {
      lastDisp = nv;
      myBar.setValue(nv);
    }
  }

  /**
   * Returns the minimum value -- the lower end of the progress value.
   *
   * @return an int representing the minimum value
   * @see #setMinimum
   */
  public int getMinimum() {
    return min;
  }

  /**
   * Specifies the minimum value.
   *
   * @param m  an int specifying the minimum value
   * @see #getMinimum
   */
  public void setMinimum(int m) {
    min = m;
  }

  /**
   * Returns the maximum value -- the higher end of the progress value.
   *
   * @return an int representing the maximum value
   * @see #setMaximum
   */
  public int getMaximum() {
    return max;
  }

  /**
   * Specifies the maximum value.
   *
   * @param m  an int specifying the maximum value
   * @see #getMaximum
   */
  public void setMaximum(int m) {
    max = m;
  }

  /** 
   * Returns true if the user hits the Cancel button in the progress dialog.
   */
  public boolean isCanceled() {
    Object obj;

    if (pane == null) 
      return false;

    obj = pane.getValue();
    return ((obj != null) && (cancelOption.length == 1) &&
            (obj.equals(cancelOption[0])));
  }

  /**
   * Specifies the additional note that is displayed along with the
   * progress message. Used, for example, to show which file the
   * is currently being copied during a multiple-file copy.
   *
   * @param note  a String specifying the note to display
   * @see #getNote
   */
  public void setNote(String note) {
    this.note = note;
    noteLabel.setText(note);
  }

  /**
   * Specifies the additional note that is displayed along with the
   * progress message.
   *
   * @return a String specifying the note to display
   * @see #setNote
   */
  public String getNote() {
    return note;
  }

  private class ProgressOptionPane extends JOptionPane {
    ProgressOptionPane(Object messageList) {
      super(messageList,
            JOptionPane.INFORMATION_MESSAGE,
            JOptionPane.DEFAULT_OPTION,
            null,
            ModalProgressMonitor.this.cancelOption,
            null);
    }

    public int getMaxCharactersPerLineCount() {
      return 60;
    }
  }
}
