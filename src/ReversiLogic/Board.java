/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex3                                                      *
 ****************************************************************************/
package ReversiLogic;


import java.util.ArrayList;
import java.util.List;

/*****************************************************************************
 * Board Class: this class contains the board of the game                    *
 ****************************************************************************/
public class Board {
    static public final Boolean BLACK = true;
    static public final Boolean WHITE = false;
    private enum direction {
        UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT
    }

    /*************************************************************************
     * Inner class Cell: holds unique cell in the board                      *
     ************************************************************************/
    class Cell {
        // initializing flag to be blank
        private int row, col;
        private Boolean colored;
        // 1 for black 0 for white
        private Boolean color;
        private Boolean outOfBound;
        private char flag;
        /*************************************************************************
         * Function name: isColored                                              *
         * Output: returns false if empty and true otherwise                     *
         ************************************************************************/
        public Boolean isColored()  { return colored; }

        /*************************************************************************
         * Function name: getColor                                               *
         * Output: returns 1 for BLACK, 0 for WHITE                              *
         ************************************************************************/
        public Boolean getColor()  { return color; }

        /*************************************************************************
         * Function name: markOutOfBound                                         *
         * Output: mark as "ERROR" cell - will be used to represent out of range *
         ************************************************************************/
        public void markOutOfBound() { outOfBound = true; }

        /*************************************************************************
         * Function name: isOutOfBoud                                            *
         * Output: true for "ERROR" cell false otherwise                         *
         ************************************************************************/
        public Boolean isOutOfBoud() { return outOfBound; }

        /*************************************************************************
         * Function name: getRow                                                 *
         * Output: returns cell row index                                        *
         ************************************************************************/
        public int getRow() { return row; }

        /*************************************************************************
         * Function name: getCol                                                 *
         * Output: returns cell column index                                     *
         ************************************************************************/
        public int getCol() { return col; }

        /*************************************************************************
         * Function name: index                                                  *
         * Input: row and column indexes                                         *
         * Output: initiate cell acording to location                            *
         ************************************************************************/
        public void index(int i, int j) {
            row = i;
            col = j;
        }

        /*************************************************************************
         * Function name: default consructor                                     *
         ************************************************************************/
        public Cell() {
            flag = ' ';
            colored =  false;
            outOfBound = false;
        }

        /*************************************************************************
         * Function name: MarkO                                                  *
         * Input: no input                                                       *
         * Output: marks cell as O                                               *
         ************************************************************************/
        public void markColor(Boolean inColor) {
            // checking that the flag is Blank
            colored = true;
            color = inColor;
            if (inColor)
                flag = 'X';
            else
                flag = 'O';

        }
    }

    //private variables for board....

    // parameters to hold the dimensions
    private int row, col;
    // special cell indicating out of range
    private Cell error;
    // ptr for the matrix of the board
    private Cell board[];

    /*************************************************************************
     * Function name: getCell                                                *
     * Input: row,col                                                        *
     * Output: returns the correct cell from the board array according       *
     * to the requested row and column                                       *
     ************************************************************************/
    private Cell getCell(int i, int j){
        // verify index
        if(i >= 0 && j >= 0 && i < row && j < col)
            // cell[i][j] is at i*col+j
            return board[i*col + j];
        return error;
    }

    /*************************************************************************
     * Function name: legalMoveDirection                                     *
     * Input: starting cell color(BLACK = 1, WHITE = 0) direction and a bool *
     * value to indicate if paint cells or just return answer for possibility*
     * Output: returns                                                       *
     * - true if there is a legal sequence to the arrow direction            *
     *      i.e Xooox is correct sequence to the right (start = X)           *
     * - false if it's not legal to paint thee cell in requested color       *
     ************************************************************************/
    private int legalMoveDirection(Cell start, Boolean color, int arrow, Boolean mark, List<Point> colored){
        // get next cell in the direction of the arrow
        Cell current = nextCellOnDirection(start, arrow);
        // check that the next cell in the arrow direction is in opposite color otherwise return Error
        if (current.isOutOfBoud() || !current.isColored() || current.getColor() == color )
            return 0;

        // vector to save cells to be colored if move is legal
        List<Cell> toMark = new ArrayList<Cell>();
        // first cell to mark in case that start is legal move (because it's color is for sure !color)
        toMark.add(start);
        toMark.add(current);

        // loop on all opposite color in arrow direction
        while(!(current = nextCellOnDirection(current, arrow)).isOutOfBoud() &&
                current.isColored() && current.getColor() != color)
        // if mark is true
        toMark.add(current);

        // if current cell is not colored or it's out of the board then there wasn't
        // a cell withe same color in the arrow direction
        if (current.isOutOfBoud() || !current.isColored())
            return 0;
        // if current cell is colored in the original color then the move is legal

        // counter for number of cells that are painted from one color to the other
        // - starts from -1 because the start point is not painted at all - thus doesn't need to be counted
        int toPaintCount = -1;
        for (int i = 0; i < toMark.size(); i++) {
            if(mark) {
                toMark.get(i).markColor(color);
                colored.add(new Point(toMark.get(i).getRow(), toMark.get(i).getCol()));
            }
            toPaintCount++;
        }


        return toPaintCount;
    }

    /*************************************************************************
     * Function name: nextCellOnDirection                                    *
     * Input: cell and direction                                             *
     * Output: returns the a reference to the directed neighbor of the cell  *
     ************************************************************************/
    private Cell nextCellOnDirection(Cell cell,int direct){
        switch (direct){
            case 0: //UP
                return getCell(cell.getRow()-1,cell.getCol());
            case 1: //UP_RIGHT
                return getCell(cell.getRow()-1,cell.getCol()+1);
            case 2: //RIGHT
                return getCell(cell.getRow(),cell.getCol()+1);
            case 3: //DOWN_RIGHT
                return getCell(cell.getRow()+1,cell.getCol()+1);
            case 4: //DOWN
                return getCell(cell.getRow()+1,cell.getCol());
            case 5: //DOWN_LEFT
                return getCell(cell.getRow()+1,cell.getCol()-1);
            case 6: //LEFT
                return getCell(cell.getRow(),cell.getCol()-1);
            case 7: //UP_LEFT
                return getCell(cell.getRow()-1,cell.getCol()-1);
            default:
                return getCell(cell.getRow()-1,cell.getCol()-1);
        }
    }
    // funcs for testing - other then that they are not needed
    public int getRows() { return  row;}
    public int getCols() { return  col;}
    // the numbers are shown 1..n hence the -1
    Boolean getColor(int i, int j) { return getCell(i, j).getColor(); }
    Boolean isColred(int i, int j) { return getCell(i, j).isColored(); }
    // end of funcs for testing

    /*************************************************************************
     * Function name: Board Constructor                                      *
     * Input: board dimensions row,col                                       *
     * operation: creates starting board col X row (default 8X8)             *
     ************************************************************************/
    public Board(int row, int col){
        if(row > 1 && col > 1) {
            // initializing row/col
            this.row = row;
            this.col = col;
        }
        else{
            this.row = row = 8;
            this.col = col = 8;
        }

        //initiate ERROR cell to return when needed
        error = new Cell();
        error.markOutOfBound();

        // creating new array to save the matrix
        board = new Cell[row*col];
        for(int i = 0; i < row*col; i++)
            board[i] = new Cell();
        //initialize cells with indexes
        int current = 0;
        for (int i = 0; i<row; i ++)
            for (int j = 0; j<col; j ++)
                board[current++].index(i,j);


        // marking middle cells to XOXO
        getCell(row/2-1,col/2-1).markColor(false);
        getCell(row/2,col/2).markColor(false);
        getCell(row/2-1,col/2).markColor(true);
        getCell(row/2,col/2-1).markColor(true);
    }

    /*************************************************************************
     * Function name: isMovePossible                                         *
     * Input: row, column and (BLACK = 1, WHITE = 0)                         *
     * Output: returns true if its legal to paint cell in requested color    *
     *          and false otherwise                                          *
     ************************************************************************/
    public Boolean isMovePossible(int moveRow, int moveCol, Boolean color){
        // only empty cells within the board can be legal
        if(getCell(moveRow, moveCol).isOutOfBoud() || getCell(moveRow, moveCol).isColored())
            return false;
        // if one of the following is true then the move is legal
        if (legalMoveDirection(getCell(moveRow, moveCol), color, 0, false, null) != 0) // UP
            return true;
        else if (legalMoveDirection(getCell(moveRow, moveCol), color, 1, false, null) != 0) // UP_RIGHT
            return true;
        else if (legalMoveDirection(getCell(moveRow, moveCol), color, 2, false, null) != 0) // RIGHT
            return true;
        else if (legalMoveDirection(getCell(moveRow, moveCol), color, 3, false, null) != 0) // DOWN_RIGHT
            return true;
        else if (legalMoveDirection(getCell(moveRow, moveCol), color, 4, false, null) != 0) // DOWN
            return true;
        else if (legalMoveDirection(getCell(moveRow, moveCol), color, 5, false, null) != 0) // DOWN_LEFT
            return true;
        else if (legalMoveDirection(getCell(moveRow, moveCol), color, 6, false, null) != 0) // LEFT
            return true;
        else if (legalMoveDirection(getCell(moveRow, moveCol), color, 7, false, null) != 0) // UP_LEFT
            return true;
        return false;
    }



    /*************************************************************************
     * Function name: playColor                                              *
     * Input: row, column and (BLACK = 1, WHITE = 0)                         *
     * Output: play othello move withe requested cell and color              *
     *          if the move isn't legal returns false                        *
     ************************************************************************/
    public Boolean playColor(int moveRow, int moveCol, Boolean color, List<Point> colored){
        if(getCell(moveRow, moveCol).isOutOfBoud())
            return false;
        // if the move is legal paint all directions
        if(isMovePossible(moveRow, moveCol, color)) {
            legalMoveDirection(getCell(moveRow, moveCol), color, 0, true, colored); // UP
            legalMoveDirection(getCell(moveRow, moveCol), color, 1, true, colored);  // UP_RIGHT
            legalMoveDirection(getCell(moveRow, moveCol), color, 2, true, colored);  // RIGHT
            legalMoveDirection(getCell(moveRow, moveCol), color, 3, true, colored); // DOWN RIGHT
            legalMoveDirection(getCell(moveRow, moveCol), color, 4, true, colored); // DOWN
            legalMoveDirection(getCell(moveRow, moveCol), color, 5, true, colored); // DOWN_LEFT
            legalMoveDirection(getCell(moveRow, moveCol), color, 6, true, colored); // LEFT
            legalMoveDirection(getCell(moveRow, moveCol), color, 7, true, colored); // UP_LEFT
            return true;
        }
        return false;

    };

    /*************************************************************************
     * Function name: possibleMoves                                          *
     * Input: color (BLACK = 1, WHITE = 0)                                   *
     * Output: returns string for possible moves for requested color         *
     ************************************************************************/
    public String possibleMoves(Boolean color){
        String result = "";
        Boolean firstComma = true;
        for(int i = 0; i < row; i++)
            for (int j=0 ; j < col ; j++) {
                // check if move is legal
                Boolean legalMove = isMovePossible(i,j,color);
                // if the move is legal add it to the string
                if (legalMove) {
                    // don't print before the first cell
                    if (!firstComma)
                        result +=  ",";
                    else
                        firstComma = false;
                    result = result + "(" + i + "," + j + ")";
                }
            }
        return result;
    };

    /*************************************************************************
     * Function name: score                                                  *
     * Input: color (BLACK = 1, WHITE = 0)                                   *
     * Output: returns number of cells painted with requested color          *
     ************************************************************************/
    public int score(Boolean color){
        int count = 0;
        for(int i = 0; i < row; i++)
            for(int j = 0; j < col; j++) {
                if (getCell(i, j).isColored() && getCell(i, j).getColor() == color)
                    count++;
            }
        return count;
    }

    /*************************************************************************
     * Function name: OpponentScoreForMove                                   *
     * Input: row column and color (BLACK = 1, WHITE = 0)                    *
     * Output: returns opponent's score after the move (row,col)             *
     ************************************************************************/
    public int OpponentScoreForMove(int moveRow, int moveCol, Boolean color){
        // score for opponent player before move
        int scoreOpponentColor = score(!color);
        // legalMoveDirection will return the number of cell that change color from the opponent color
        // to current color, the opponent score after the move will be the subtraction of the
        // those cells from the score before the move
        if(!getCell(moveRow, moveCol).isOutOfBoud() || !getCell(moveRow, moveCol).isColored()) {
            scoreOpponentColor -= legalMoveDirection(getCell(moveRow, moveCol), color, 0, false, null); // UP
            scoreOpponentColor -= legalMoveDirection(getCell(moveRow, moveCol), color, 1, false, null); // UP_RIGHT
            scoreOpponentColor -= legalMoveDirection(getCell(moveRow, moveCol), color, 2, false, null); // RIGHT
            scoreOpponentColor -= legalMoveDirection(getCell(moveRow, moveCol), color, 3, false, null); // DOWN_RIGHT
            scoreOpponentColor -= legalMoveDirection(getCell(moveRow, moveCol), color, 4, false, null); // DOWN
            scoreOpponentColor -= legalMoveDirection(getCell(moveRow, moveCol), color, 5, false, null); // DOWN_LEFT
            scoreOpponentColor -= legalMoveDirection(getCell(moveRow, moveCol), color, 6, false, null); // LEFT
            scoreOpponentColor -= legalMoveDirection(getCell(moveRow, moveCol), color, 7, false, null); // UP_LEFT
        }
        return scoreOpponentColor;
    };
}

