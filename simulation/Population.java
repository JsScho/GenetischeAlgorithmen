/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import genotype.GenotypeString;
import java.util.ArrayList;
import java.util.Random;
import other.FitnessCalculator;
import other.Protein;
import phenotype.Grid;
import phenotype.Phenotype;

/**
 *
 * @author Skurrrman
 */
public class Population {
    
    private final Protein protein;
    private final Phenotype[] population;
    private final Double[] cumulatedFitness;      
    private final FitnessCalculator fitnessCalculator = new FitnessCalculator();
    
    private double totalFitness;
    
    public Population(int size, Protein newprot){
        population = new Phenotype[size];
        cumulatedFitness = new Double[size];        
        protein = newprot;        
    }
    
    public void initRandomPopulation(){
        for(int i =0; i<population.length; ++i){
            GenotypeString genotype = new GenotypeString(protein.getHPSequence().length()-2);
            String[] result = fitnessCalculator.calculateFitness(new Grid((protein.getHPSequence().length()*2)+4),protein.getHPSequence());
            population[i] = new Phenotype(genotype,protein,Double.valueOf(result[1]),Integer.valueOf(result[0]));
        }
        calculateFitnessDistribution();
    }
    
    private void calculateFitnessDistribution(){ 
        totalFitness = 0;
        for(int i=0; i< population.length; ++i){
            totalFitness += population[i].getFitness();
        }
        
        cumulatedFitness[0] = population[0].getFitness();
        for (int i=1; i<population.length; ++i){
            cumulatedFitness[i] = (population[i].getFitness() / totalFitness) + cumulatedFitness[i-1];
        }
        
        System.out.println("cumulated fitness is "+cumulatedFitness[cumulatedFitness.length-1]);
        
    }
    
    public Population generateNextPopulation(double mutationRate, double crossOverRate){
        //Population nextPop = new Population(population.length,protein);
        
        ArrayList<GenotypeString> nextGenos = new ArrayList<>();
        
        //select candidates
        Random rand = new Random();
        
        for (int j = 0; j<population.length; ++j){
            double choice = rand.nextDouble();
            for (int i = 0; i< cumulatedFitness.length; ++i){
                if(cumulatedFitness[i] >= choice){
                    nextGenos.add(population[i].getGenotype());
                }
            }
        }
        
        mutate(nextGenos);
        crossover(nextGenos,crossOverRate);
    }
    
    public void crossover(ArrayList<GenotypeString> nextGenos, double crossoverRate){
        Random rand = new Random();
        int numberOfCrossovers = (Double.valueOf( (double)nextGenos.size() * crossoverRate)).intValue();
        
        ArrayList<GenotypeString> genoCopy = nextGenos;
        for(int i = 0; i< numberOfCrossovers; ++i){
            GenotypeString candidateOne = genoCopy.get(rand.nextInt(genoCopy.size()));
            genoCopy.remove(candidateOne);
            GenotypeString candidateTwo = genoCopy.get(rand.nextInt(genoCopy.size()));
            genoCopy.remove(candidateTwo);
            
            int bitpos = rand.nextInt(candidateOne.length());
            
        }
    }
    
    
    
    
}
