

package genotype;

import java.util.ArrayList;

public class GenotypeString {
    
    private ArrayList<GenotypeNode> string;
    
    public GenotypeString (int length){
        string = new ArrayList<>();
        for (int i =0; i<length;++i){
            string.add(new GenotypeNode());
        };
    }
    
    public GenotypeString (int length,String seq){
        string = new ArrayList<>();
        for (int i =0; i<length;++i){
            string.add(new GenotypeNode(seq.substring(i*4, i*4 +4)));
        };
    }
    
    @Override
    public String toString(){
        String toPrint = new String("");
        for (GenotypeNode node : string){
            toPrint = toPrint.concat(node.toString()+" | ");
        }; 
        toPrint = toPrint.replace("true", "1");
        toPrint = toPrint.replace("false", "0");        
        return toPrint;
    }
    
    public GenotypeNode get(int index){
        return string.get(index);
    }
    
    public int length(){
        return string.size();
    }
    
    public void remove(int startindex, int length){
        for (int i=0; i< length; ++i){
            string.remove(startindex);
        }
    }
    public GenotypeString substring (int startindex , int length){
        
    }
    public void insert(int startindex, String toInsert){
        
    }
}
