/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unibi.bibiserv.rnamovies;

import java.util.Random;

public class Algorithm {

    /* GA parameters */
    private static final double uniformRate = 0.5;
    private static final double mutationRate = startFrame.mutate;
    private static final int tournamentSize = 5;
    private static final boolean elitism = true;

    /* Public methods */
    
    // Evolve a population
    public static Population evolvePopulation(Population pop) {
        Population newPopulation = new Population(pop.size(), false,Population.LenghtOfSolution);

        // Keep our best individual
        if (elitism) {
            newPopulation.saveIndividual(0, pop.getFittest());
        }

        // Crossover population
        int elitismOffset;
        if (elitism) {
            elitismOffset = 1;
        } else {
            elitismOffset = 0;
        }
        // Loop over the population size and create new individuals with
        // crossover
        for (int i = elitismOffset; i < pop.size(); i++) {
            Individual indiv1 = tournamentSelection(pop);
            Individual indiv2 = tournamentSelection(pop);
            Individual newIndiv = crossover(indiv1, indiv2);
            newPopulation.saveIndividual(i, newIndiv);
        }
        // Mutate population
        for (int i = elitismOffset; i < newPopulation.size(); i++) {
            mutate(newPopulation.getIndividual(i));
        }
//        System.out.println("stru: --> "+newPopulation.individuals[0].structure);
        return newPopulation;
    }
    // Crossover individuals
    private static Individual crossover(Individual indiv1, Individual indiv2) {
        Individual newSol = new Individual();
        Random ran = new Random(7);
        boolean flag =false;
        int indexOfCrossover =0;
        int crossIndexEndIndiv1=0;
        newSol=indiv1;
        while(!flag)
        {
//            System.out.println("indiv1.size2(): " + indiv1.size2() );
            if(indiv1.size2() >1)
            {
                indexOfCrossover= ran.nextInt(indiv1.size2());
            }
            else
            {
                throw new IllegalArgumentException("*************");            
            }                
            //System.out.println("indiv1.size2()-indexOfCrossover-1: " + (indiv1.size2()-indexOfCrossover-1) );
            if((indiv1.size2())-(indexOfCrossover-1)>1)
            {
                crossIndexEndIndiv1 = indexOfCrossover + ran.nextInt((indiv1.size2())-(indexOfCrossover-1))+1;
            }
            else
            {
                throw new IllegalArgumentException("*************");
            }                
            flag = checkIndexOfCrossover(indiv1,indiv2,indexOfCrossover,crossIndexEndIndiv1);
        }
        // Crossover
        if(indexOfCrossover==0 && crossIndexEndIndiv1==indiv1.size2()-1)
        {
            //newSol= indiv1;
        }
        else if(indexOfCrossover==0 && crossIndexEndIndiv1<indiv1.size2()-1 )
        {                
            newSol.structure = indiv1.structure.substring(0, crossIndexEndIndiv1+1)+ indiv2.structure.substring(crossIndexEndIndiv1+1, indiv2.size2());
        }
        else
        {
            String s1 = indiv2.structure.substring(0, indexOfCrossover);
            String s2 = indiv1.structure.substring(indexOfCrossover, crossIndexEndIndiv1+1);
            String s3 = indiv2.structure.substring(crossIndexEndIndiv1+1, indiv2.size2());
            newSol.structure = s1 + s2 + s3 ;
        }
    return newSol;
}

    // Mutate an individual
    private static void mutate(Individual indiv) {
        boolean flag = true;
        // Loop through genes
        for (int i = 0; i < indiv.size2(); i++) {
            if (Math.random() <= mutationRate) {
                // Create random gene
                if(indiv.structure.charAt(i)=='(')
                {
                    char[] seq= indiv.structure.toCharArray();
                    seq[i] = '.';
                    for(int j=i;(j<indiv.size2());j++)
                    {
                        if(seq[j]==')')
                        {
                            seq[j] = '.';
                            break;
                        }
                    }
                    indiv.structure = String.valueOf(seq);
                }
                else if(indiv.structure.charAt(i)=='.')
                {
                    for(int j = indiv.structure.length()-1; j >= 0 ;j--)
                    {
                        if(i==j)
                            continue;
                        if(indiv.structure.charAt(j) == '.')
                        {
                            if(i<j)
                            {
                                char[] seq= indiv.structure.toCharArray();
                                seq[j] = ')';
                                seq[i] = '(';
                                indiv.structure = String.valueOf(seq);
                                flag=true;
                                break;
                            }
                            else
                            {
                                char[] seq= indiv.structure.toCharArray();
                                seq[i] = ')';
                                seq[j] = '(';
                                indiv.structure = String.valueOf(seq);
                                flag=true;
                                break;
                            }
                     }
                    }
                }
            }            
        }
    }

    // Select individuals for crossover
    private static Individual tournamentSelection(Population pop) {
        // Create a tournament population
        Population tournament = new Population(tournamentSize, false,Population.LenghtOfSolution);
        // For each place in the tournament get a random individual
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.size());
            tournament.saveIndividual(i, pop.getIndividual(randomId));
        }
        // Get the fittest
        Individual fittest = tournament.getFittest();
        return fittest;
    }

    private static boolean checkIndexOfCrossover(Individual indiv1, Individual indiv2, int indexOfCrossover, int crossIndexEndIndiv1) {
        int OP=0;
        int CP = 0;
        if((indexOfCrossover<0) || (indexOfCrossover> indiv1.size2()) ||  (crossIndexEndIndiv1 >= indiv1.size2())||(crossIndexEndIndiv1 <= indexOfCrossover))
        {
            return false;
        }
//        System.out.println("indexOfCrossover: "+indexOfCrossover);
//        System.out.println("crossIndexEndIndiv1:" + crossIndexEndIndiv1);
        for(int i= indexOfCrossover; i<= crossIndexEndIndiv1;i++)
            {
                if(indiv1.structure.charAt(i)== '(')
                {
                   OP++; 
                }
                else if(indiv1.structure.charAt(i)== ')')
                {
                    CP++;
                }
            }
        if(CP!=OP)
        {
            return false;
        }
        else
        {
            CP =0;
            OP = 0;
            for(int i= indexOfCrossover; i<= crossIndexEndIndiv1;i++)
            {
                if(indiv2.structure.charAt(i)== '(')
                {
                   OP++; 
                }
                else if(indiv2.structure.charAt(i)== ')')
                {
                    CP++;
                }
            }
            if(CP!=OP)
            {
                return false;
            }
        }
        return true;
    }
}