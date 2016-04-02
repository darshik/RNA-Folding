/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unibi.bibiserv.rnamovies;

public class Population {
    public static int LenghtOfSolution = 0;

    Individual[] individuals;

    /*
     * Constructors
     */
    // Create a population
    public Population(int populationSize, boolean initialise, int lengthOfSolution) {
        if(initialise)
        {
            Population.LenghtOfSolution=lengthOfSolution;
        }
        individuals = new Individual[populationSize];
        // Initialise population
        if (initialise) {
            // Loop and create individuals
            for (int i = 0; i < size(); i++) {
                Individual newIndividual = new Individual();
                //newIndividual.generateIndividual();
                newIndividual.structure= newIndividual.generateIndividual2(lengthOfSolution,newIndividual.structure);
                newIndividual.sequence=FitnessCalc.solutionSeq;
                saveIndividual(i, newIndividual);
            }
        }
    }

    /* Getters */
    public Individual getIndividual(int index) {
        return individuals[index];
    }

    public Individual getFittest() {
        Individual fittest = individuals[0];
        // Loop through individuals to find fittest
        for (int i = 0; i < size(); i++) {
            if (fittest.getFitness() <= getIndividual(i).getFitness()) {
                fittest = getIndividual(i);
            }
        }
        return fittest;
    }

    /* Public methods */
    // Get population size
    public final int size() {
        return individuals.length;
    }

    // Save individual
    public final void saveIndividual(int index, Individual indiv) {
        individuals[index] = indiv;
    }
}