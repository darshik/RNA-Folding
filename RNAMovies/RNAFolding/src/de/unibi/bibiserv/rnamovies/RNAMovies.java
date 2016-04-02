package de.unibi.bibiserv.rnamovies;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.io.InputStream;
import java.io.IOException;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import de.unibi.bibiserv.naview.Structure;
import de.unibi.bibiserv.naview.PairTable;
import de.unibi.bibiserv.rnamovies.actions.Export;
import de.unibi.bibiserv.rnamovies.configuration.Configuration;
import de.unibi.bibiserv.rnamovies.configuration.FieldAdapter;
import de.unibi.bibiserv.rnamovies.util.ActionXMLHandler;
import de.unibi.bibiserv.rnamovies.util.ActionContainer;
import de.unibi.bibiserv.rnamovies.util.LineScanner;
import de.unibi.bibiserv.rnamovies.util.ShapeOps;
import de.unibi.techfak.bibiserv.biodom.RNAStructML;
import de.unibi.techfak.bibiserv.biodom.exception.BioDOMException;
import de.unibi.techfak.bibiserv.rnamovies.thirdparty.AnimatedGifEncoder;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

/**
 * RNA Movies
 *
 * @author Alexander Kaiser <akaiser@TechFak.Uni-Bielefeld.DE>
 *
 *  RNAStructML,Commandline interface support added by
 *  Jan krueger <jkrueger@techfak.uni-bielefeld.de>
 *
 */
public class RNAMovies extends JPanel implements ActionContainer {
    
    public static final String TITLE = "RNAMovies";
    public static final String VERSION ="2.04";
    public static final String USAGE = TITLE+VERSION+"\n"+
            "usage java "+TITLE+VERSION+".jar [<arguments>] :\n\n"+
            "ATTENTION : cmdlineoptions in early beta state!\n\n"+
            "-nogui                         :: run RNAMovies without starting the GUI\n"+
            "-input <String>                :: set the input filename\n"+
            "-output <String>               :: set the output filename\n"+
            "[-xml]                         :: determines that the input file is in RNAStructML format\n"+
            "-[structure <int>|steps <int>] :: creates ONLY the given structure (single frame)\n"+
            "[-size <int>]                  :: set the size of the generated frame (in pixel)\n"+
            "[-zoom <int>]                  :: zoom factor inside"+
            "-(gif|png|svg|jpg)             :: set the image format\n"+
            "[-h[elp]]                      :: print out a usage message\n";
    
         
    
    public static final int SCALE = 15;
    
    public static Logger log = Logger.getLogger("RNAMovies");
    
    public Configuration config;
    
    public JMenuBar mb;
    public MoviePane mp;
    public JToolBar tb;
    
    public RNAMovies(InputStream configStream, InputStream actionStream) {
        super(new BorderLayout());
        
        FieldAdapter fa;
        XMLReader parser;
        
        // menu
        mb = new JMenuBar();
        
        // toolbar
        tb = new JToolBar();
        tb.setFloatable(false);
        tb.setLayout(new FlowLayout());
        
        // load configuration
        try {
            config = new Configuration(configStream);
        } catch(IOException e) {
            log.severe(e.getMessage());
            System.exit(1);
        } catch(SAXException e) {
            log.severe("Error while parsing config: " + e.getMessage());
            System.exit(1);
        }
        
        // load actions
        try {
            parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(new ActionXMLHandler((ActionContainer)this,
                    new Class[]{this.getClass()},
                    new Object[]{this}));
            parser.parse(new InputSource(actionStream));
        } catch(IOException e) {
            log.severe(e.getMessage());
            System.exit(1);
        } catch(SAXException e) {
            log.severe("Error while parsing action file: " + e.getMessage());
            System.exit(1);
        }
        
        add(mb, BorderLayout.NORTH);
        
        // movie pane
        mp = new MoviePane(640, 460);
        fa = new FieldAdapter(mp.getConfigurable());
        config.addConfigListener(fa);
        config.getCategory("animation").removeConfigListener(fa);
        config.addConfigListener(mp);
        config.initAll();
        add(mp, BorderLayout.CENTER);
        
        add(tb, BorderLayout.SOUTH);
    }
    
    /**
     * Public default constructor, standard menu and configurations are loaded.
     */
    public RNAMovies() {
        this(RNAMovies.class.getResourceAsStream("config.xml"),
                RNAMovies.class.getResourceAsStream("actions.xml"));
    }
    
    /**
     * Set the movie data from a String.
     *
     * @param data A String containing a valid script.
     */
    public void setData(String data) {
        parseScript(mp,new StringTokenizer(data, "\n"), false);
    }
    
    /**
     * Set the movie data from a RNAStructML object
     *
     * @param rml - A String containing a RNAStructML object
     *
     * JK
     */
    public void setData(RNAStructML rml){
        parseRNAStructML(mp,rml,false);
    }
    
    /**
     * Set the movie data from a String.
     *
     * @param data A String containing a valid script.
     * @param center Center structures to the maximal area of the whole script.
     */
    public void setData(String data, boolean center) {
        parseScript(mp,new StringTokenizer(data, "\n"), center);
    }
    
    /**
     * Set the movie data from a RNAStructML object
     *
     * @param rml - A String containing a RNAStructML object
     * @param center - Center all structures to the maximal area of the whole script.
     *
     * JK
     */
    public void setData(RNAStructML rml, boolean center) {
        parseRNAStructML(mp,rml,center);
    }
    
    /**
     * Set the movie data from an InputStream.
     *
     * @param in An InputStream (e.g. FileInputStream) containing a valid script.
     */
    public void setData(InputStream in) {
        parseScript(mp,new LineScanner(in), false);
    }
    
    /**
     * Set the movie data from an InputStream.
     *
     * @param in An InputStream (i.e. FileInputStream) containing a valid script.
     * @param center Center structures to the maximal area of the whole script.
     */
    public void setData(InputStream in, boolean center) {
        parseScript(mp,new LineScanner(in), center);
    }
    public static void parseScript(MoviePane mp, Enumeration enumer, boolean center){
         parseScript(mp,enumer, center,true);
    }
    public static void parseScript(MoviePane mp, Enumeration enumer, boolean center, boolean gui) {
        int i, w, h, length;
        int old_length = -1;
        int title_end = -1;
        boolean dcse;
        String name, sequence, structure, helices;
        List<Point2D[]> frames;
        List<Dimension> sizes;
        Dimension maxSize;
        List<PairTable> pairs;
        Structure struc = null;
        
        if(!enumer.hasMoreElements())
            throw new IllegalArgumentException("No Movie Data found!");
        else
            name = ((String)enumer.nextElement()).trim();
        
        if(name.equals(""))
            throw new IllegalArgumentException("No Movie Data found!");
        
        if(name.charAt(0) == '>')
            dcse = false;
        else if(name.charAt(0) == '<')
            dcse = true;
        else
            throw new IllegalArgumentException("Data Format Error: Missing '>' or '<' character!");
        
        if(!enumer.hasMoreElements())
            throw new IllegalArgumentException("No Movie Data found!");
        else
            sequence = ((String)enumer.nextElement()).trim();
        
        length = sequence.length();
        
        w = h = 0;
        pairs = new ArrayList<PairTable>(350);
        frames = new ArrayList<Point2D[]>(350);
        sizes = new ArrayList<Dimension>(350);
        while(enumer.hasMoreElements()) {
            
            if(struc != null)
                old_length = struc.length();
            
            if(dcse) {
                structure = ((String)enumer.nextElement());
                if(!enumer.hasMoreElements())
                    throw new IllegalArgumentException("Error in DCSE structure: missing helix numbering.");
                helices = ((String)enumer.nextElement());
                struc = new Structure(sequence, structure, helices);
            } else {
                structure = ((String)enumer.nextElement()).trim();
                struc = new Structure(sequence, structure);
            }
            
            if(old_length != -1 && old_length > struc.length())
                throw new IllegalArgumentException("Length of structures in descending order not allowed!");
            
            pairs.add(struc.getPairTable());
            frames.add(struc.getNormalizedCoordinates(SCALE, SCALE, 0, 0));
            sizes.add(new Dimension(struc.getWidth(SCALE), struc.getHeight(SCALE)));
            if(struc.getWidth(SCALE) > w)
                w = struc.getWidth(SCALE);
            if(struc.getHeight(SCALE) > h)
                h = struc.getHeight(SCALE);
        }
        
        if(frames.size() == 0)
            throw new IllegalArgumentException("No Movie Data found!");
        
        if(center) {
            maxSize = new Dimension(w, h);
            for(i = 0; i < frames.size(); i++)
                ShapeOps.center(frames.get(i), sizes.get(i), maxSize);
        }
        
        title_end = name.indexOf(' ');
        mp.setMovie(frames, pairs, name.substring(1, title_end == -1 ? name.length() : title_end), sequence, w, h,gui);
        log.info(frames.size() + " Structures loaded.");
    }
    
    /**
     * Get all necessary data from RNAStructML and pass it to the
     * moviepane (for internal use only)
     *
     * JK
     */
    public static void parseRNAStructML(MoviePane mp, RNAStructML rml, boolean center){
        parseRNAStructML(mp,rml,center,true);
    }
    
    /**
     * Get all necessary data from RNAStructML and pass it to the
     * moviepane (for internal use only)
     *
     * JK
     */
    public static void parseRNAStructML(MoviePane mp, RNAStructML rml, boolean center,boolean gui){
        // local used vars
        int w = 0;
        int h = 0;
        List<Point2D[]> frames = new ArrayList<Point2D[]>(350);
        List<Dimension> sizes = new ArrayList<Dimension>(350);
        List<PairTable> pairs = new ArrayList<PairTable>(350);
        String sequence;
        String name = "";
        
        // until now only the first structure of a RNAStructML is supported
        String structureid = rml.getRnastructureIds().get(0);
        // get Sequence information
        try {
            Hashtable<String,Object> sequenceInfo = rml.getSequence(structureid);
            sequence = (String)(sequenceInfo.get("sequence"));
            name = (String)(sequenceInfo.get("seqID"));
        } catch (BioDOMException e){
            throw new IllegalArgumentException("BioDOM Exception during call of getSequence!");
        }
        // get all structures
        try {
            List<Hashtable<String,Object>> structuresInfoList = rml.getStructures(structureid);
            // check if list contains at least one element
            if (structuresInfoList.size() == 0) {
                throw new IllegalArgumentException("RNAStructML contains no structure information!");
            }
            // iterate over all elements
            for (int i = 0; i < structuresInfoList.size(); ++i){
                if (structuresInfoList.get(i).containsKey("structure")){
                    String dotbracket = (String)structuresInfoList.get(i).get("structure");
                    if (sequence.length() != dotbracket.length()){
                        throw new IllegalArgumentException("Length of structure differs from length of sequence!");
                    }
                    // the following lines are more or less a copy from parseScript(...)
                    Structure struc = new Structure(sequence, dotbracket);
                    pairs.add(struc.getPairTable());
                    frames.add(struc.getNormalizedCoordinates(SCALE,SCALE,0,0));
                    sizes.add(new Dimension(struc.getWidth(SCALE),struc.getHeight(SCALE)));
                    if (struc.getWidth(SCALE) > w){
                        w = struc.getWidth(SCALE);
                    }
                    if (struc.getHeight(SCALE) > h){
                        h = struc.getHeight(SCALE);
                    }
                } else {
                    throw new IllegalArgumentException("RNAStructML contains no structure information!");
                }
            }
            
            
        } catch (BioDOMException e){
            throw new IllegalArgumentException("BioDOM Exception during call of getStructures!");
        }
        
        if (center){
            Dimension maxSize = new Dimension(w,h);
            for (int i = 0; i < frames.size(); ++i){
                ShapeOps.center(frames.get(i),sizes.get(i),maxSize);
            }
        }
        
        mp.setMovie(frames,pairs,name,sequence,w,h,gui);
    }
    
    public JToolBar getToolBar() {
        return tb;
    }
    
    public JMenuBar getMenuBar() {
        return mb;
    }
    
    /**
     * Accessor method for the Movie
     *
     * @return an instance of the Movie
     */
    public Movie getMovie() {
        return mp;
    }
    
    /**
     * Get an instance of the Configuration
     *
     * @return the current configuration
     */
    public Configuration getConfiguration() {
        return config;
    }
    
    /**
     * This method loads the Main class and puts the main panel on a JFrame.
     * It is to be run as a thread.
     */
    public static void createAndShowGUI() {
        JFrame frame;
        
        frame = new JFrame(TITLE);
        frame.setContentPane(new RNAMovies());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * The main method of this launcher. Sets the preferred look-and-feel and
     * starts the above method inside a thread.
     */
    
    
    /** static method parse supported Inputparameter
     *  into a Hashtable
     *  -nogui             :: run RNAMovies without starting the GUI
     *  -input <String>    :: set the input filename
     *  -output <String>   :: set the output filename
     *  [-xml]             :: determines that the input file is in RNAStructML format
     *  -[structure <int>|steps <int> ]:: creates ONLY the given structure (single frame)
     *  [-zoom <int>]      :: zoom factor inside
     *  [-size <int>]      :: set the size of the generated frame (in pixel)
     *  -(gif|png|svg|jpg) :: set the image format
     *  [-trans]           :: set transparent background (not for jpg) 
     *  [-zoom]            :: set zoom factor of structure within image
     *  [-help] [-h]       :: print out a usage message
     */
    public static Hashtable<String,Object> parseParameter(String args[]) {
        Hashtable<String,Object> prop = new Hashtable<String,Object>();
        prop.put("input", new String());
        prop.put("output", new String());
        prop.put("xml", new Boolean(true));
        prop.put("size", new Integer(0));
        prop.put("structure", new Integer(0));
        prop.put("steps", new Integer(10));
        prop.put("zoom", new Integer(0));
        prop.put("gif", new Boolean(true));
        prop.put("png", new Boolean(true));
        prop.put("svg", new Boolean(true));
        prop.put("jpg", new Boolean(true));
        prop.put("help", new Boolean(true));
        prop.put("h", new Boolean(true));
        prop.put("nogui", new Boolean(true));
        prop.put("trans",new Boolean(true));
        Hashtable ret = new Hashtable();
        String key = "";
        
        for (int i = 0; i < args.length; ++i) {
            String current = args[i];
        
            // found key
            if (current.startsWith("-")) {
                // remove --
                key = current.replaceAll("-", "");
                
                // check - maybe boolean
                if ((key != null) && (prop.get(key) != null)) {
                    Class c = (prop.get(key)).getClass();
                    if ((c.getName()).equals("java.lang.Boolean")) {
                        ret.put(key, new Boolean(true));
                    }
                    log.info("Found "+key+" as Boolean");
                }
                
            } else { // found value
                // check if current key exists in prop
                if (prop.get(key) != null) {
                    
                    // get Class of value
                    Class c = (prop.get(key)).getClass();
                    if ((c.getName()).equals("java.lang.Integer")) {
                        ret.put(key, new Integer(Integer.parseInt(current)));
                    } else if ((c.getName()).equals("java.lang.String")) {
                        ret.put(key, current);
                    } else if ((c.getName()).equals("java.lang.Double")) {
                        ret.put(key, new Double(Double.parseDouble(current)));
                    }
                }
                key = null;
            }
        }
        /* print usage message to STDOUT and exit */
        if (ret.containsKey("h") || ret.containsKey("help")){
            System.out.println(USAGE);
            System.exit(0);
            
        }
        /* check parameter dependencies */
        if (ret.containsKey("nogui") &&
                !ret.containsKey("input") && !ret.containsKey("output") &&
                !((ret.containsKey("gif") || ret.containsKey("png") || ret.containsKey("jpg") || ret.containsKey("svg")))){
            System.out.println(USAGE);
            System.exit(0);
        }
        
        log.info(ret.toString());
        return ret;
    }
    
}