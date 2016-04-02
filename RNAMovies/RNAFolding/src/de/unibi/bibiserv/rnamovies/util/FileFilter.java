/*
 * FileFilter.java
 *
 * Created on January 25, 2007, 9:32 AM
 *
 */

package de.unibi.bibiserv.rnamovies.util;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

/**
 * Implements the abstract class FileFilter from the javax.swing.filechooser
 * package. Supports all basic stuff a FileFilter should support.
 *
 * @author Jan Krueger <jkrueger@techfak.uni-bielefeld.de>
 */
public class FileFilter extends javax.swing.filechooser.FileFilter{
    
    private String description;  
    private Set<String> extensions = new TreeSet<String>();
    
    /** Creates a new instance of FileFilter */
    public FileFilter() {
    }
    
    /** Creates a new instance of FileFilter
     *
     *  @param description - set the description of this FileFilter*/
    public FileFilter(String description){
        this.description = description;
    }
   
    /**
     * Returns true, if the given file is acceptd by this filter, false otherwise.
     * 
     * @param file - file to be accepted
     * @return Returns true, if given file is accepted by this filter, false
     *         otherwise      
     **/
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        String ext = getExtension(file);
        if ((ext != null) && (extensions.contains(ext))){
            return true;
        }
        return false;
    }
   
    private String getExtension(File file){    
        if(file != null) {
            String fn = file.getName();
            int i = fn.lastIndexOf('.');
            if ((i > 0) && (i < fn.length()-1)) {
                return fn.substring(i+1).toLowerCase();
            };
        }
        return null;
    }
    
    /**
     * Set the description (as String) of this FileFilter
     * 
     * @param description - set the description of this FileFilter
     */ 
    public void setDescription(String description){
        this.description = description;
    }
    
    /**
     * Returns the description of this FileFilter.
     * 
     * @return Returns the description of this FileFilter
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Add an accepted file extension.
     *
     * @param ext - add an accepted file extension .
     */
    public void addExtension(String ext){
        extensions.add(ext.toLowerCase());   
    }
   
    /**
     * Remove the specified file extension from FileFilter
     *
     * @param ext - file extension to be removed
     * @return Returns
     */
    public boolean removeExtension(String ext){
        return extensions.remove(ext.toLowerCase());
    }
    
    
    
    
}
