/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unibi.bibiserv.rnamovies;

public class FitnessCalc {

   public static String solutionStruct;
   public static String solutionSeq; 
    

    /* Public methods */
    // Set a candidate solution as a byte array
    public static void setSolution(String Seq, String Struct) {
        solutionSeq = Seq;
        solutionStruct = Struct;
    }

    // To make it easier we can use this method to set our candidate solution
    // with string of 0s and 1s
//    static void setSolution(String newSolution) {
//        solution = new byte[newSolution.length()];
//        // Loop through each character of our string and save it in our byte
//        // array
//        for (int i = 0; i < newSolution.length(); i++) {
//            String character = newSolution.substring(i, i + 1);
//            if (character.contains("0") || character.contains("1")) {
//                solution[i] = Byte.parseByte(character);
//            } else {
//                solution[i] = 0;
//            }
//        }
//    }

    // Calculate inidividuals fittness by comparing it to our candidate solution
    static int getFitness(Individual individual) {
        int fitness = 0;
        // Loop through our individuals genes and compare them to our cadidates
        //for (int i = 0; i < individual.size() && i < solution.length; i++) {
        for (int i = 0; i < individual.size2(); i++) {
            char ch=individual.getGene(i);
            if ( ch == '(')
            {
                fitness++;
            }
        }
        return fitness;
    }
    
    // Get optimum fitness
//    static int getMaxFitness() {
//        int maxFitness = solution.length;
//        return maxFitness;
//    }
}