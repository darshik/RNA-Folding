package de.unibi.bibiserv.rnamovies.util;

import java.lang.reflect.Constructor;
import java.util.Stack;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JSeparator;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class ActionXMLHandler implements ContentHandler {

  private static Logger log = Logger.getLogger("ActionXMLHandler");

  private JMenuBar jmb;
  private JToolBar jtb;
  private Class[] consTypes;
  private Object[] consObjs;

  private Stack<Attributes> lastAtts = new Stack<Attributes>();
  private Stack<StringBuffer> lastChars = new Stack<StringBuffer>();

  private Stack<JMenu> menuPath = new Stack<JMenu>();

  public ActionXMLHandler(ActionContainer ac) {
    this(ac, new Class[]{}, new Object[]{});
  }

  public ActionXMLHandler(ActionContainer ac,
                          Class[] consTypes,
                          Object[] consObjs) {
    this.consTypes = consTypes;
    this.consObjs = consObjs;
    this.jmb = ac.getMenuBar();
    this.jtb = ac.getToolBar();
  }

  public void startElement(String uri,
                           String localName,
                           String qName,
                           Attributes atts)
  throws SAXException {
    String name;

    lastAtts.push(new AttributesImpl(atts));
    lastChars.push(new StringBuffer());

    if(qName.equalsIgnoreCase("menu")) {
      name = atts.getValue("name"); 
      menuPath.push(new JMenu(name == null ? "Unnamed" : name));
    } else if(qName.equalsIgnoreCase("separator")) {
      if(!menuPath.empty())
        menuPath.peek().add(new JSeparator());
    }
  }

  public void endElement(String uri, String localName, String qName)
  throws SAXException {
    int modifier;
    String text, toolBar, mnemonic, key;
    Attributes atts;
    KeyStroke accelerator;
    JMenu menu;
    JMenuItem item;
    Class class_ = null;
    Constructor cons_ = null;
    Object obj_ = null;

    atts = lastAtts.pop();
    text = lastChars.pop().toString();

    if(qName.equalsIgnoreCase("menu")) {
      menu = menuPath.pop();

      if((mnemonic = atts.getValue("mnemonic")) != null && mnemonic.length()>0)
        menu.setMnemonic(mnemonic.charAt(0));

      if(menuPath.empty())
        jmb.add(menu);
      else
        menuPath.peek().add(menu);

    } else if(qName.equalsIgnoreCase("action")) {

      try {
        class_ = Class.forName(atts.getValue("class"));
        cons_ = class_.getConstructor(consTypes);
        obj_ = cons_.newInstance(consObjs);

        if(!menuPath.empty() && obj_ instanceof AbstractAction) {
          item = new JMenuItem((AbstractAction) obj_);
          item.setIcon(null);

          key = atts.getValue("key");
          if(key != null && key.length() > 0) {
            try {
              modifier = Integer.parseInt(atts.getValue("modifier"));
              accelerator = KeyStroke.getKeyStroke(key.charAt(0), modifier);
              item.setAccelerator(accelerator);
            } catch(NumberFormatException e){
              log.warning(e.getMessage() + " is not a valid int.");
            }
          }
          menuPath.peek().add(item);
        }

        if((toolBar = atts.getValue("toolBar")) != null
           && (new Boolean(toolBar)).booleanValue()
           && obj_ instanceof AbstractAction)
          jtb.add((AbstractAction) obj_);

      } catch(NoSuchMethodException e) {
        log.warning("Could not find Constructor: " + e.getMessage());
      } catch(InstantiationException e) {
        log.warning("Could not instantiate: " + e.getMessage());
      } catch(IllegalAccessException e) {
        log.warning(e.getMessage());
      } catch(java.lang.reflect.InvocationTargetException e) {
        log.warning(e.getMessage());
      } catch(ClassNotFoundException e) {
        log.warning("Could not find class: " + e.getMessage());
      }
    }
  }

  public void setDocumentLocator(Locator locator){
  }

  public void startDocument()
  throws SAXException {
    log.info("Loading actions...");
  }

  public void endDocument()
  throws SAXException {
    log.info("Actions successfully loaded.");
  }

  public void characters(char[] ch, int start, int length)
  throws SAXException {
    lastChars.peek().append(ch, start, length);
  }

  public void ignorableWhitespace(char[] ch, int start, int length)
  throws SAXException {
  }

  public void processingInstruction(String target, String data)
  throws SAXException {
  }

  public void skippedEntity(String name)
  throws SAXException {
  }

  public void startPrefixMapping(String prefix, String uri)
  throws SAXException {
  }

  public void endPrefixMapping(String prefix)
  throws SAXException {
  }

}
