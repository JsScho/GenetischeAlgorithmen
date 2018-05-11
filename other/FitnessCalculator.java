/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package other;

import java.util.HashSet;
import phenotype.Grid;
import phenotype.Node;

/**
 *
 * @author Skurrrman
 */
public class FitnessCalculator {
    
    private final HashSet<String> hydrophobicContacts = new HashSet<>();
    
    private void addContact(Node a,Node b){
        if(a.getX() < b.getX() || a.getY() < b.getY()){
            hydrophobicContacts.add(String.valueOf(a.getX())+"_"+a.getY()+"_"+b.getX()+"_"+b.getY());
        }
        else hydrophobicContacts.add(String.valueOf(b.getX())+"_"+b.getY()+"_"+a.getX()+"_"+a.getY());
    }
    
    public String[] calculateFitness(Grid grid,String aminosequence){
              
        hydrophobicContacts.clear();
        
        Node currNode = grid.getStartNode();
        while(currNode != null){
            if(currNode.isHydrophobic()){
                Node right = grid.getNode(currNode.getX()+1,currNode.getY());  
                Node left = grid.getNode(currNode.getX()-1,currNode.getY());
                Node down = grid.getNode(currNode.getX(),currNode.getY()+1);
                Node up = grid.getNode(currNode.getX(),currNode.getY()-1);
                
                if (right.isHydrophobic() && right.getSuccessor() != currNode && right.getPredecessor() != currNode){
                    addContact(currNode,right);
                }
                if (left.isHydrophobic() && left.getSuccessor() != currNode && left.getPredecessor() != currNode){
                    addContact(currNode,left);
                }
                if (down.isHydrophobic() && down.getSuccessor() != currNode && down.getPredecessor() != currNode){
                    addContact(currNode,down);
                }
                if (up.isHydrophobic() && up.getSuccessor() != currNode && up.getPredecessor() != currNode){
                    addContact(currNode,up);
                }                
            }
            currNode = currNode.getSuccessor();
        }
        
        int numberOfhydrophobicContacts = hydrophobicContacts.size();
        long numberOfHydrophobicNodes = aminosequence.chars().filter(ch -> ch == '0').count();
        
        double fitness = ((double)hydrophobicContacts.size()/numberOfHydrophobicNodes)+0.1;
        
        String[] result= new String[2];
        result[0]=String.valueOf(numberOfhydrophobicContacts);
        result[1]=String.valueOf(fitness);
        
        return result;
    }
}
