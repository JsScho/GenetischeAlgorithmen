
package genotype;

import java.util.Random;


public class GenotypeNode {
    
    private Boolean[] bits;
    
    public GenotypeNode(){
        bits = new Boolean[4];
        randomize();
    }
    public GenotypeNode(String seq){
        bits = new Boolean[4];
        for(int i = 0; i< seq.length(); ++i){
            if(seq.charAt(i) == '0')
                bits[i] = false;
            else bits[i] = true;
        }
    }
    
    public Integer getMod2Number(){
        if(bits[0]^bits[1]^bits[2]^bits[3])
            return 1;
        else return 0;
    }
    
    public Integer getMod3Number(){
        int number = 0;
        if (bits[3] == true) number += 1;
        if (bits[2] == true) number += 2;
        if (bits[1] == true) number += 4;
        if (bits[0] == true) number += 8;
        return number % 3;
    }
    
    public final void randomize(){
        Random rand = new Random();
        bits[0] = rand.nextBoolean();
        bits[1] = rand.nextBoolean();
        bits[2] = rand.nextBoolean();
        bits[3] = rand.nextBoolean();        
    }
    @Override
    public String toString(){
        String string = new String("");
        for (Boolean node : bits){
            string = string.concat(node.toString());
        };
        return string;
    }
}