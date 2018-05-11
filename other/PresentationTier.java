
package other;

//möglicherweise presentation tier als package name und anderen klassenname

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import javax.imageio.ImageIO;
import phenotype.Grid;
import phenotype.Node;

public class PresentationTier {
    
    private final int cellBorder = 3;
    private final int finalHeight = 500;
    private final int finalWidth = 1100;
    private final int aminoWindowWidth;        
    private final int infoWindowWidth = 300;  
    
    private final Color backgroundColor = Color.YELLOW;
    private final Color hydrophobicColor = new Color(240, 100, 70);
    private final Color polarColor = new Color(100, 185, 125);
    private final Color lineColor = Color.BLACK;
    
    private int aminoUsedHeight;
    private int aminoUsedWidth;
    private int cellSize;    
    private int biggestX;
    private int smallestX;
    private int biggestY;
    private int smallestY;    
    
    private boolean swapXandY;
    
    public PresentationTier(){
        aminoWindowWidth = finalWidth - infoWindowWidth;
    }
    
    public int getrelativeX(Node a){ //falls faltung um 90 grad gedreht wurde
        if (swapXandY) return a.getY();
        else return a.getX();
    }
    public int getrelativeY(Node a){ //falls faltung um 90 grad gedreht wurde
        if (swapXandY) return a.getX();
        else return a.getY();
    }
    
    public void calcCellSize(Grid grid){
        biggestX = grid.getStartNode().getX();
        smallestX = grid.getStartNode().getX();
        biggestY = grid.getStartNode().getY();
        smallestY = grid.getStartNode().getY();
        
        Node nodeIterator = grid.getStartNode();        
        while(nodeIterator.getSuccessor() != null){
            nodeIterator = nodeIterator.getSuccessor();
            if (nodeIterator.getX() > biggestX)
                biggestX = nodeIterator.getX();
            if (nodeIterator.getX() < smallestX)
                smallestX = nodeIterator.getX();
            if (nodeIterator.getY() > biggestY)
                biggestY = nodeIterator.getY();
            if (nodeIterator.getY() < smallestY)
                smallestY = nodeIterator.getY();
        } 
        
        if (biggestY-smallestY > biggestX-smallestX ){ //wenn y dimension größer -> faltung um 90 grad gedreht ausgeben
            swapXandY = true;
            int tmpX = biggestX;
            int tmpX2 = smallestX;
            biggestX = biggestY;
            smallestX = smallestY;
            biggestY=tmpX;
            smallestY=tmpX2;
        }
        else{
            swapXandY = false;          
        }
        Double possCellHeight = (double)finalHeight / (biggestY-smallestY+1);
        Double possCellWidth = (double)aminoWindowWidth / (biggestX-smallestX+1);        
        
        if (possCellHeight <= possCellWidth){
            cellSize = possCellHeight.intValue();
        }
        else cellSize = possCellWidth.intValue();
        
        if(cellSize > 90)
            cellSize =90;

        aminoUsedHeight = (biggestY-smallestY+1) * cellSize;
        aminoUsedWidth = (biggestX-smallestX+1) * cellSize;         
    }
    
    public void drawAminoSequence(Graphics2D g2,Grid grid){
        Node nodeIterator = grid.getStartNode();
        
        int counter = 0;
        
        while(nodeIterator != null){
            if(nodeIterator.isHydrophobic()){
                g2.setColor(hydrophobicColor);               
            }
            else{
                g2.setColor(polarColor);                       
            }              
            g2.fillRect(cellSize*(getrelativeX(nodeIterator)-smallestX)+((aminoWindowWidth-aminoUsedWidth)/2)+infoWindowWidth+cellBorder,
                    cellSize*(getrelativeY(nodeIterator)-smallestY)+((finalHeight-aminoUsedHeight)/2)+cellBorder,
                    cellSize-cellBorder,cellSize-cellBorder); 

            g2.setColor(Color.BLACK);
            String label = String.valueOf(counter);
            int fontsize = (int)((double) cellSize / 3.0);
            Font font = new Font("Serif", Font.PLAIN, fontsize);
            g2.setFont(font);
            FontMetrics metrics = g2.getFontMetrics();
            int ascent = metrics.getAscent();
            int labelWidth = metrics.stringWidth(label);
            g2.drawString(label, cellSize*(getrelativeX(nodeIterator)-smallestX)+((aminoWindowWidth-aminoUsedWidth)/2)+infoWindowWidth+cellBorder - labelWidth/2 + (int) ((double)cellSize * 0.7), 
                    cellSize*(getrelativeY(nodeIterator)-smallestY)+((finalHeight-aminoUsedHeight)/2)+cellBorder + (int) ((double)cellSize * 0.32) );               
            
            nodeIterator = nodeIterator.getSuccessor();
            ++counter;
        } 

        nodeIterator = grid.getStartNode().getSuccessor();
        while(nodeIterator != null){
            Node pre = nodeIterator.getPredecessor();
            g2.setColor(lineColor);
            g2.drawLine(cellSize*(getrelativeX(nodeIterator)-smallestX) + cellSize/2+ ((aminoWindowWidth-aminoUsedWidth)/2)+infoWindowWidth, 
                    cellSize*(getrelativeY(nodeIterator)-smallestY)+((finalHeight-aminoUsedHeight)/2) +cellSize/2, 
                    cellSize*(getrelativeX(pre)-smallestX)+cellSize/2+((aminoWindowWidth-aminoUsedWidth)/2)+infoWindowWidth, 
                    cellSize*(getrelativeY(pre)-smallestY)+cellSize/2+((finalHeight-aminoUsedHeight)/2));                  
            nodeIterator = nodeIterator.getSuccessor();
        }                             
    }
    
    public void printGrid(Grid grid, String aminosequence){
        
        calcCellSize(grid);

        BufferedImage image = new BufferedImage(finalWidth, finalHeight, BufferedImage.TYPE_INT_RGB);
	Graphics2D g2 = image.createGraphics();
	g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

	g2.setColor(backgroundColor);
	g2.fillRect(0, 0, finalWidth, finalHeight);
	
        drawAminoSequence(g2,grid);
        
        FitnessCalculator c = new FitnessCalculator();
        String[] result = c.calculateFitness(grid,aminosequence);
        
        g2.setColor(Color.BLACK);
        String label = String.valueOf(result[0])+" contacts";
        Font font = new Font("Serif", Font.PLAIN, 30);
        g2.setFont(font);
        FontMetrics metrics = g2.getFontMetrics();
        int ascent = metrics.getAscent();
        int labelWidth = metrics.stringWidth(label);
        g2.drawString(label, 50 , 50);           
        
        DecimalFormat fitnessformat = new DecimalFormat("#0.0000");        
        String fitness = "fitness = "+fitnessformat.format(Double.valueOf(result[1]));
        g2.drawString(fitness, 50 , 100);            
        
        g2.setColor(hydrophobicColor);         
        g2.fillRect(50,300,cellSize-cellBorder,cellSize-cellBorder);
        g2.setColor(Color.BLACK);
        String hydro = "= hydrophobic";
        font = new Font("Serif", Font.BOLD, 17);
        g2.setFont(font);
        g2.drawString(hydro, 50+cellSize-cellBorder + 10 , 300+(cellSize/2));           
        
        g2.setColor(polarColor);         
        g2.fillRect(50,400,cellSize-cellBorder,cellSize-cellBorder); 
        String polar = "= polar";
        g2.setColor(Color.BLACK);        
        g2.drawString(polar, 50+cellSize-cellBorder + 10 , 400+(cellSize/2));                          
        
        String folder = "C:\\Users\\Skurrrman\\Documents\\NetBeansProjects\\AminoFaltung\\Pictures";
        String filename = "bild.png";
        if (new File(folder).exists() == false) new File(folder).mkdirs();

        try {
                ImageIO.write(image, "png", new File(folder + File.separator + filename));
        } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
        }
    }        
}
