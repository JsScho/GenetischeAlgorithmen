/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phenotype;

import genotype.GenotypeString;
import other.Protein;

/**
 *
 * @author Skurrrman
 */
public class Phenotype {
    private GenotypeString genotype;
    private Protein mappedProtein;
    private double fitness;
    private int hydrophobicContacts;
    
    public Phenotype(GenotypeString newgeno, Protein newprot, double newfitn,int contacts){
        genotype = newgeno;
        mappedProtein =newprot;
        fitness = newfitn;
        hydrophobicContacts = contacts;
    }
    
    public int getHydrophobicContacts(){
        return hydrophobicContacts;
    }
    public double getFitness(){
        return fitness;
    }
    public GenotypeString getGenotype(){
        return genotype;
    }
    public Protein getProtein(){
        return mappedProtein;
    }
}
