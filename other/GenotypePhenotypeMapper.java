/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package other;

import genotype.Direction;
import genotype.GenotypeString;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import phenotype.Grid;
import phenotype.Node;


//optionen:
//arrays groß machen -> bei großen aminostrings index errors-> funktion zum größer machen?
//nested maps/map mit composite key -> vorteil weniger nodes werden angelegt


public class GenotypePhenotypeMapper {
    private final Grid grid;
    private final Protein aminoString;
    private final GenotypeString genotypeString; 
    private int highestXset;
    private int lowestXset;
    private int highestYset;
    private int lowestYset;    
    boolean randErreicht =false;
    
    public GenotypePhenotypeMapper(Grid newgrid, Protein newamino, GenotypeString newgenotype){
        grid = newgrid;
        aminoString = newamino;
        genotypeString = newgenotype;
    }
    
    private boolean isDeadEnd(Node start, int emptyNodesRequired){ //gut testen 
        randErreicht = false;
        
        Set<Node> connectedFreeSpaceLeft = new HashSet<>();
        Set<Node> connectedFreeSpaceRight = new HashSet<>();
        Set<Node> connectedFreeSpaceUp = new HashSet<>();
        Set<Node> connectedFreeSpaceDown = new HashSet<>();        
        connectedFreeSpaceLeft.add(start);
        connectedFreeSpaceRight.add(start);
        connectedFreeSpaceUp.add(start);
        connectedFreeSpaceDown.add(start);        
        
        Node right = grid.getNode(start.getX()+1,start.getY());  
        Node left = grid.getNode(start.getX()-1,start.getY());
        Node down = grid.getNode(start.getX(),start.getY()+1);
        Node up = grid.getNode(start.getX(),start.getY()-1);
        
        checkDeadEndRecursively(left,connectedFreeSpaceLeft,emptyNodesRequired);
        checkDeadEndRecursively(right,connectedFreeSpaceRight,emptyNodesRequired);
        checkDeadEndRecursively(down,connectedFreeSpaceDown,emptyNodesRequired);
        checkDeadEndRecursively(up,connectedFreeSpaceUp,emptyNodesRequired);
        
        if(randErreicht)
            return false;
        if (connectedFreeSpaceLeft.size() < emptyNodesRequired || 
                connectedFreeSpaceRight.size() < emptyNodesRequired || 
                connectedFreeSpaceUp.size() < emptyNodesRequired || 
                connectedFreeSpaceDown.size() < emptyNodesRequired)
            return true;
        else return false;
    }
    
    private void checkDeadEndRecursively(Node toCheck, Set<Node> connectedFreeSpace,int emptyNodesRequired){
        if (connectedFreeSpace.size() >= emptyNodesRequired)
            return;
        if (randErreicht)
            return;
    
        if(toCheck.isEmpty()){
            connectedFreeSpace.add(toCheck);
            if ( (toCheck.getX() <= lowestXset) || (toCheck.getY() <= lowestYset) || 
                    (toCheck.getX() >= highestXset) || (toCheck.getY() >= highestYset)){
                randErreicht = true;
                return;
            }       
            if (!(connectedFreeSpace.contains(grid.getNode(toCheck.getX()-1, toCheck.getY())))){
                checkDeadEndRecursively(grid.getNode(toCheck.getX()-1, toCheck.getY()), connectedFreeSpace,emptyNodesRequired);
            }
            if (!(connectedFreeSpace.contains(grid.getNode(toCheck.getX()+1, toCheck.getY())))){
                checkDeadEndRecursively(grid.getNode(toCheck.getX()+1, toCheck.getY()), connectedFreeSpace,emptyNodesRequired);
            }
            if (!(connectedFreeSpace.contains(grid.getNode(toCheck.getX(), toCheck.getY()-1)))){
                checkDeadEndRecursively(grid.getNode(toCheck.getX(), toCheck.getY()-1), connectedFreeSpace,emptyNodesRequired);
            }
            if (!(connectedFreeSpace.contains(grid.getNode(toCheck.getX(), toCheck.getY()+1)))){
                checkDeadEndRecursively(grid.getNode(toCheck.getX(), toCheck.getY()+1), connectedFreeSpace,emptyNodesRequired);
            }            
        }
    }
    
    private ArrayList<Direction> findPossibleDirections(Node start,int emptyNodesRequired){
        ArrayList<Direction> possDirs = new ArrayList<>();
        
        Node right = grid.getNode(start.getX()+1,start.getY());  
        Node left = grid.getNode(start.getX()-1,start.getY());
        Node down = grid.getNode(start.getX(),start.getY()+1);
        Node up = grid.getNode(start.getX(),start.getY()-1);
        
        if( right.isEmpty() && !isDeadEnd(right,emptyNodesRequired))
            possDirs.add(Direction.RIGHT);
        if( left.isEmpty() && !isDeadEnd(left,emptyNodesRequired))
            possDirs.add(Direction.LEFT);
        if( down.isEmpty() && !isDeadEnd(down,emptyNodesRequired))
            possDirs.add(Direction.DOWN);
        if( up.isEmpty() && !isDeadEnd(up,emptyNodesRequired))
            possDirs.add(Direction.UP);

        return possDirs;
    }
    
    public void computePhenotype(){

        Node currNode = grid.getStartNode();
        currNode.setPolarity(aminoString.get(0));
        highestXset = lowestXset = currNode.getX();
        highestYset = lowestYset = currNode.getY();
        
        currNode = goStep(currNode, Direction.UP,aminoString.get(1)); //erster schritt immer up
      
        int aminoCounter = 2;
        int genoCounter = 0; //erste 2 knoten sind immer gleich, deshalb genotyp 2 kürzer als aminostring
        
        while(aminoCounter < aminoString.length()){
            ArrayList<Direction> possDirs = findPossibleDirections(currNode,aminoString.length()-aminoCounter-1);
            switch(possDirs.size()){
                case 1:
                    currNode = goStep(currNode, possDirs.get(0),aminoString.get(aminoCounter));
                    break;
                case 2:
                    currNode = goStep(currNode, possDirs.get(genotypeString.get(genoCounter).getMod2Number()),aminoString.get(aminoCounter));
                    break;
                case 3:
                    currNode = goStep(currNode, possDirs.get(genotypeString.get(genoCounter).getMod3Number()),aminoString.get(aminoCounter));
                    break;
            }
            ++aminoCounter;
            ++genoCounter;
        }
    }
    private Node goStep(Node fromNode, Direction dir, boolean is_polar){
        int x_start = fromNode.getX();
        int y_start = fromNode.getY();
        int x_end = 0;
        int y_end = 0;
        switch(dir){
            case UP:
                x_end=x_start;
                y_end=y_start-1;
                break;
            case LEFT:
                x_end=x_start-1;
                y_end=y_start;
                break;
            case DOWN:
                x_end=x_start;
                y_end=y_start+1;
                break;
            case RIGHT:
                x_end=x_start+1;
                y_end=y_start;
                break;
        }
        grid.getNode(x_start,y_start).setSuccessor(grid.getNode(x_end, y_end));
        grid.getNode(x_end, y_end).setPredecessor(grid.getNode(x_start,y_start));
        grid.getNode(x_end, y_end).setPolarity(is_polar);     
        
        if(x_end > highestXset)
            highestXset = x_end;
        if(x_end < lowestXset)
            lowestXset = x_end;
        if(y_end > highestYset)
            highestYset = y_end;
        if(y_end < lowestYset)
            lowestYset = y_end;
        
        return grid.getNode(x_end, y_end);
    }    
}

