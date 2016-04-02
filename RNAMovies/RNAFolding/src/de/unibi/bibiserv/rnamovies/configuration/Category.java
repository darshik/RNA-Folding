package de.unibi.bibiserv.rnamovies.configuration;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.swing.event.EventListenerList;

public class Category {

  private Map<String, TypeWrapper> values;

  private EventListenerList listenerList = new EventListenerList();

  private String name;

  private int id = -1;

  protected Category(String name, Map<String, TypeWrapper> values) {
    this.name = name;
    this.values = values;
  }

  protected Category(int id, String name, Map<String, TypeWrapper> values) {
    this.id = id;
    this.name = name;
    this.values = values;
  }

  public Map<String, TypeWrapper> getValues() {
    return values;
  }

  public void init() {
    int id = -1;
    String key;
    Iterator<String> keys;
    TypeWrapper tw;
    
    for(keys = values.keySet().iterator(); keys.hasNext();) {
      key = keys.next();
      tw = values.get(key);
      
      if(tw.contains("id")) {
        try {
          id = Integer.parseInt(tw.getAttribute("id"));
          id = id < -1 ? -1 : id;
        } catch(NumberFormatException e) {
          id = -1;
        }
      }
      fire(new ConfigChangedEvent(this, id, key, tw.getObject()));
    }
  }

  public void set(String key, Object value) {
    int id = -1;
    TypeWrapper tw;

    if(!values.containsKey(key))
      throw new NoSuchElementException("Key " + key + " does not exsist in Configuration.");

    tw = values.get(key);

    if(tw.contains("id")) {
      try {
        id = Integer.parseInt(tw.getAttribute("id"));
        id = id < -1 ? -1 : id;
      } catch(NumberFormatException e) {
        id = -1;
      }
    }    

    if(value.getClass() != tw.getObject().getClass())
      throw new IllegalArgumentException("Invalid argument: " + value.getClass().getName() + " where " + values.get(key).getObject().getClass().getName() + " is expected.");

    tw.setObject(value);
    fire(new ConfigChangedEvent(this, id, key, value));
  }

  public void set(String key, int value) {
    set(key, new Integer(value));
  }

  public void set(String key, boolean value) {
    set(key, new Boolean(value));
  }

  public Object get(String key) {
    if(!values.containsKey(key))
      throw new NoSuchElementException("Key " + key + " does not exsist in Configuration.");

    return values.get(key).getObject();
  }

  public int getInt(String key) {
    Object value;
  
    value = get(key);
    if(value instanceof Integer)
      return ((Integer) value).intValue();
    else
      throw new IllegalArgumentException("Invalid argument: " + key + ": " + value.getClass().getName() + " where java.lang.Integer is expected.");
 
  }

  public boolean getBoolean(String key) {
    Object value;
 
    value = get(key);
    if(value instanceof Boolean)
      return ((Boolean) value).booleanValue();
    else
      throw new IllegalArgumentException("Invalid argument: " + key + ": " + value.getClass().getName() + " where java.lang.Boolean is expected.");

  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void addConfigListener(ConfigListener listener) {
    listenerList.add(ConfigListener.class, listener);
  }

  public void removeConfigListener(ConfigListener listener) {
    listenerList.remove(ConfigListener.class, listener);
  }

  private void fire(ConfigChangedEvent e) {
    int i;
    Object[] listeners;

    listeners = listenerList.getListenerList();
    for (i = 0; i < listeners.length; i += 2)
      if (listeners[i] == ConfigListener.class)
        ((ConfigListener)listeners[i+1]).configurationChanged(e);
  }

}
