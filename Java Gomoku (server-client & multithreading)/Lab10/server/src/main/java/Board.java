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


    /**
     * updates the Board with a new piece
     *
     * @param x     axis coordinate of the piece
     * @param y     axis coordinate of the piece
     * @param color of the piece
     * @return
     */
    public boolean putPiece(int x, int y, boolean color) {
        if (this.notEmpty[x][y])
            return false;
        this.blackOrWhite[x][y] = color;
        this.notEmpty[x][y] = true;
        return true;
    }

    /**
     * Checks if the game has ended.
     * 5 pieces of the same color in a line
     *
     * @return state of the game; 0->game has not ended. 1->white won, 2->black won
     */

    public int gameEnded() {
        //horizontal
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - 4; j++) {
                if (notEmpty[i][j] && notEmpty[i][j + 1] && notEmpty[i][j + 2] && notEmpty[i][j + 3] && notEmpty[i][j + 4]) {
                    if (blackOrWhite[i][j] && blackOrWhite[i][j + 1] && blackOrWhite[i][j + 2] && blackOrWhite[i][j + 3] && blackOrWhite[i][j + 4])
                        return 1;
                    if (!blackOrWhite[i][j] && !blackOrWhite[i][j + 1] && !blackOrWhite[i][j + 2] && !blackOrWhite[i][j + 3] && !blackOrWhite[i][j + 4])
                        return 2;
                }
            }
        }


        //vertical
        for (int i = 0; i < size - 4; i++) {
            for (int j = 0; j < size; j++) {
                if (notEmpty[i][j] && notEmpty[i + 1][j] && notEmpty[i + 2][j] && notEmpty[i + 3][j] && notEmpty[i + 4][j]) {
                    if (blackOrWhite[i][j] && blackOrWhite[i + 1][j] && blackOrWhite[i + 2][j] && blackOrWhite[i + 3][j] && blackOrWhite[i + 4][j])
                        return 1;
                    if (!blackOrWhite[i][j] && !blackOrWhite[i + 1][j] && !blackOrWhite[i + 2][j] && !blackOrWhite[i + 3][j] && !blackOrWhite[i + 4][j])
                        return 2;
                }
            }
        }

        //diagonals
        for (int i = 0; i < size - 4; i++) {
            for (int j = 0; j < size - 4; j++) {
                //first diagonal
                if (notEmpty[i][j] && notEmpty[i + 1][j + 1] && notEmpty[i + 2][j + 2] && notEmpty[i + 3][j + 3] && notEmpty[i + 4][j + 4]) {
                    if (blackOrWhite[i][j] && blackOrWhite[i + 1][j + 1] && blackOrWhite[i + 2][j + 2] && blackOrWhite[i + 3][j + 3] && blackOrWhite[i + 4][j + 4])
                        return 1;
                    if (!blackOrWhite[i][j] && !blackOrWhite[i + 1][j + 1] && !blackOrWhite[i + 2][j + 2] && !blackOrWhite[i + 3][j + 3] && !blackOrWhite[i + 4][j + 4])
                        return 2;
                }
                //second diagonal
                if (notEmpty[i][j + 4] && notEmpty[i + 1][j + 3] && notEmpty[i + 2][j + 2] && notEmpty[i + 3][j + 1] && notEmpty[i + 4][j]) {
                    if (blackOrWhite[i][j + 4] && blackOrWhite[i + 1][j + 3] && blackOrWhite[i + 2][j + 2] && blackOrWhite[i + 3][j + 1] && blackOrWhite[i + 4][j])
                        return 1;
                    if (!blackOrWhite[i][j + 4] && !blackOrWhite[i + 1][j + 3] && !blackOrWhite[i + 2][j + 2] && !blackOrWhite[i + 3][j + 1] && !blackOrWhite[i + 4][j])
                        return 2;
                }
            }
        }

        //if all fails. Game has not ended yet.
        return 0;
    }
}
