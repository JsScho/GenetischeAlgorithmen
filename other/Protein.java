
package other;

/**
 *
 * @author Skurrrman
 */
public class Protein {
    
    public final String HPsequence;
    
    public Protein(String newseq){
        HPsequence = newseq;
    }
    public String getHPSequence(){
        return HPsequence;
    }
    public boolean get(int index){
        return Tools.CharBitToBool(HPsequence.charAt(index));        
    }
    public int length(){
        return HPsequence.length();
    }
}
