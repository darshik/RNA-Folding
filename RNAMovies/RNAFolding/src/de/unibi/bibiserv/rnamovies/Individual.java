/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unibi.bibiserv.rnamovies;

import java.util.Random;

public class Individual {
    public static double BindRate = 0.5;
    
    static int defaultGeneLength = 64;
    static int defaultGeneLength2 = 500;
    public String sequence = "";
    public String structure = "";
    private byte[] genes = new byte[defaultGeneLength];
    // Cache
    private int fitness = 0;
    private double fitness2 = 0.0;

 
    // Create a random individual
    public String generateIndividual2(int length, String structure ) {
    Random ran = new Random(5);
    boolean bindflag = false;
    String Bind = ".";
    int indexofbind = 0;
    for(int i=0;i<length;i++)
    {
        structure += '.';            
    }
    for(int i=0;i<length-1;i++)
    {
        if(structure.charAt(i) != '.')
        {
            continue;
        }
        if(Math.random()<BindRate)
        {
            Bind = "(";
//            System.out.println("length-i -1: " + (length-i-1) );
            int temp = length-i-1;
            if(temp >=1)
            {
                indexofbind = i + ran.nextInt(temp) + 1;
            }
            else
            {
                throw new IllegalArgumentException("*************");
                
            }
            if(structure.charAt(indexofbind) == '.')
            {
                char[] seq= structure.toCharArray();
                seq[indexofbind] = ')';
                structure = String.valueOf(seq);
                bindflag=true;
            }
            else
            {
                for(int j = indexofbind;j<length;j++)
                {
                    if(structure.charAt(j) == '.')
                    {
                        char[] seq= structure.toCharArray();
                        seq[j] = ')';
                        structure = String.valueOf(seq);
                        bindflag=true;
                        break;
                    }
                }
            }
            if(bindflag)
            {
                char[] seq= structure.toCharArray();
                seq[i] = '(';
                structure = String.valueOf(seq);
                bindflag = false;
            }
            }
//            else
//            {
//                char[] seq= structure.toCharArray();
//                seq[i] = '.';
//                structure = String.valueOf(seq);
//            }
    }
    return structure;
}

    /* Getters and setters */
    // Use this if you want to create individuals with different gene lengths
    public static void setDefaultGeneLength(int length) {
        defaultGeneLength = length;
    }
public char getGene(int index) {
        return structure.charAt(index);
    }
    public void setGene(int index, byte value) {
        genes[index] = value;
        fitness = 0;
    }

    /* Public methods */
//    public int size() {
//        return genes.length;
//    }
    public int size2() {
        return structure.length();
    }

    public int getFitness() {        
            fitness = FitnessCalc.getFitness(this);
        return fitness;
    }

    @Override
    public String toString() {        
        return structure;
    }
}