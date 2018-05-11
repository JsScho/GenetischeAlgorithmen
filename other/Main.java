
package other;

import genotype.GenotypeString;
import phenotype.Grid;

public class Main {
    public static void main(String[] args) {
        
        Grid grid = new Grid(105);
        Protein a= new Protein("010101111");
        GenotypeString genotype = new GenotypeString(a.getHPSequence().length()-2);
        
        GenotypePhenotypeMapper mapper = new GenotypePhenotypeMapper(grid,a,genotype);
        mapper.computePhenotype();
        
        PresentationTier p = new PresentationTier();
        p.printGrid(grid,a.getHPSequence());
    }    
}
