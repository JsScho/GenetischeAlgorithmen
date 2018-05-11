/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phenotype;

/**
 *
 * @author Skurrrman
 */
public class Node {
    private boolean isHydrophobic;
    private boolean isPolar;
    private Node predecessor;
    private Node successor;
    private int xval;
    private int yval;
    
    public Node(int newx, int newy){
        isHydrophobic = false;
        isPolar = false;
        predecessor = null;
        successor = null;
        xval=newx;
        yval=newy;
    }
    public void setPolarity(boolean ispolar){
        if(ispolar){
            isPolar = true;
            isHydrophobic = false;
        }
        else{
            isHydrophobic = true;
            isPolar = false;
        }
    }
    public void setPredecessor(Node pre){
        predecessor = pre;
    }
    public void setSuccessor(Node suc){
        successor = suc;
    }
    public boolean isHydrophobic(){
        return isHydrophobic;
    }
    public boolean isPolar(){
        return isPolar;
    }
    public boolean isEmpty(){
        return (!isPolar&&!isHydrophobic);
    }
    public Node getPredecessor(){
        return predecessor;
    }
    public Node getSuccessor(){
        return successor;
    }
    public int getX(){
        return xval;
    }
    public int getY(){
        return yval;
    }
    
}
