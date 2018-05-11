


package phenotype;

public class Grid {
    private final Node[][] grid;
    private final int size;
    private Node startNode;
    
    public Grid(int newsize){
        size = newsize;
        grid = new Node[size][size];
        reset();
    }
    
    public Node getStartNode(){
        return startNode;
    }
    
    public Node getNode(int x,int y){
        return grid[x][y];
    }
    
    public final void reset(){
        for (int i =0; i< grid.length; ++i){
            for (int j = 0; j<grid[i].length; ++j){
                grid[i][j] = new Node(i,j);
            }
        }

        if (size % 2 == 0)
            startNode = grid[(size/2)-1][(size/2)-1];    
        else startNode = grid[(size-1) /2][(size-1) /2];
    }
    public int getSize(){
        return size;
    }
}
