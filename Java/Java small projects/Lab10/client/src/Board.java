public class Board {
    boolean[][] blackOrWhite;//0->black; 1->white
    boolean[][] notEmpty;//0->empty; 1->not empty
    int size;

    public Board(int size) {
        if (size < 5)
            return;
        this.blackOrWhite = new boolean[size][size];
        this.notEmpty = new boolean[size][size];
        this.size = size;
    }


}
