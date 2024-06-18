package eightqueens;
import java.util.*;

/**
 *
 * @author Isaiah Lipford
 * @version 05/24/2024
 */
public class HillClimbing {

    private int N; // Size of the grid
    private int[][] grid;
    private int heuristic;
    
    /**
     * Constructs a new HillClimbing object with a default grid size of 8.
     */
    public HillClimbing() {
        this.N = 8;
        grid = new int[N][N];
        
        //Generate new grid immediately
        generateRandomGrid();
        
        //Check and save the heuristic value
        this.heuristic = checkHeuristic();
    }
    
    /**
     * Constructs a new HillClimbing object with a specified grid size.
     *
     * @param n the size of the grid
     */
    public HillClimbing(int n) {
        this.N = n;
        grid = new int[N][N];
        
        //Generate new grid immediately
        generateRandomGrid();
        
        //Check and save the heuristic value
        this.heuristic = checkHeuristic();
    }
    
    /**
     * Constructs a new HillClimbing object with a specified grid.
     *
     * @param g the grid representing the state of the queens
     */
    public HillClimbing(int[][] g) {
        if (g.length == g[0].length) {
            //set the grid to the parameter
            this.grid = g;
            //set the size
            N = g.length;
            //Check and save the heuristic value
            this.heuristic = checkHeuristic();
        } else {
            System.out.println("ERROR! N*N grid was not entered!");
            //Generate new grid
            generateRandomGrid();
            //Check and save the heuristic value
            this.heuristic = checkHeuristic();
        }
    }
    
    // Getter methods
    /**
     * Returns the grid representing the state of the queens.
     *
     * @return the grid
     */
    public int[][] getGrid() {
        return grid;
    }
    
    /**
     * Returns the heuristic value of the current state.
     *
     * @return the heuristic value
     */
    public int getHeuristic() {
        return heuristic;
    }
    
    // Other Supporting Stuff
    
    /**
    * Generates a random grid with one queen per column.
    */
    private void generateRandomGrid() {
        //Starts with all 0's
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = 0;
            }
        }

        //Place 8 Queens(Value of 1) in a random row per column
        for (int i = 0; i < N; i++) {
            int random = (int)(Math.random() * N);
            grid[random][i] = 1;
        }
    }
    
    /**
    * Checks if the current state is a goal state.
    *
    * @return true if the current state is a goal state, false otherwise
    */
    public boolean isGoal() {
        if (heuristic == 0) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Moves a queen from a start position to an end position.
     *
     * @param startRow    the row of the start position
     * @param startCol    the column of the start position
     * @param endRow      the row of the end position
     * @param endCol      the column of the end position
     */
    public void moveQueen(int startRow, int startCol, int endRow, int endCol) {
        // Only move if the entered queen is actually a queen (value of 1 and not 0)
        if (grid[startRow][startCol] == 1) {
            grid[startRow][startCol] = 0; // Make it empty
            grid[endRow][endCol] = 1; // Make it a Queen
        }

        // Check Heuristic Again
        heuristic = checkHeuristic();
    }
    
    //Finding the Heuristic
    
    /**
     * Checks the heuristic value of the current state.
     *
     * @return the heuristic value
     */
    private int checkHeuristic() {
        int conflict = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == 1) {
                    conflict += checkHorizontal(i, j);
                    conflict += checkVertical(i, j);
                    conflict += checkDiagonal(i, j);
                }
            }
        }
        return conflict / 2;
    }

    /**
     * Checks the number of conflicts that arise vertically.
     *
     * @param row the row of the queen to check
     * @param col the column of the queen to check
     * @return the number of conflicts
     */
    private int checkVertical(int row, int col) {
        int conflict = 0;
        for (int i = 0; i < N; i++) {
            if (i == row) {
                continue;
            }
            if (grid[i][col] == 1) {
                conflict++;
            }
        }
        return conflict;
    }

    /**
     * Checks the number of conflicts that arise horizontally.
     *
     * @param row the row of the queen to check
     * @param col the column of the queen to check
     * @return the number of conflicts
     */
    private int checkHorizontal(int row, int col) {
        int conflict = 0;

        for (int i = 0; i < N; i++) {
            if (i == col) {
                continue;
            }
            if (grid[row][i] == 1) {
                conflict++;
            }
        }
        return conflict;
    }
    
    /**
    * Checks the number of conflicts that may happen diagonally.
     *
     * @param row the row of the queen to check
     * @param col the column of the queen to check
     * @return the number of conflicts
     */
    private int checkDiagonal(int row, int col) {
        int conflict = 0;
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if (grid[i][j] == 1) {
                conflict++;
            }
        }
        
        for (int i = row + 1, j = col + 1; i < N && j < N; i++, j++) {
            if (grid[i][j] == 1) {
                conflict++;
            }
        }
        
        for (int i = row + 1, j = col - 1; i < N && j >= 0; i++, j--) {
            if (grid[i][j] == 1) {
                conflict++;
            }
        }
        
        for (int i = row - 1, j = col + 1; i >= 0 && j < N; i--, j++) {
            if (grid[i][j] == 1) {
                conflict++;
            }
        }
        return conflict;
    }
    
    /**
     * Returns a string representation of the grid.
     *
     * @return the string representation of the grid
     */
    @Override
    public String toString() {
        String stringGrid = "";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                stringGrid += grid[i][j] + " ";
            }
            stringGrid += "\n";
        }
        return stringGrid.substring(0, stringGrid.length() - 2);
    }
    
    /**
     * Returns a hash map of all neighbors of the current state.
     *
     * @return the hash map of all neighbors
     */
    public HashMap getAllNeighbors() {
        HashMap neighbors = new HashMap();
        
        //Iterate through the grid
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                //Find each Queen
                if (grid[row][col] == 1) {
                    //Move the current Queen to each row in the column
                    for (int i = 0; i < N; i++) {
                        if (i == row) {
                            continue;
                        }
                        HillClimbing p = new HillClimbing(copyArray(grid, N));
                        p.moveQueen(row, col, i, col);
                        neighbors.put(p, p.getHeuristic());
                    }
                }
            }
        }
        return neighbors;
    }
    
    /**
     * Copies a 2D array.
     *
     * @param g the 2D array to copy
     * @param size the size of the 2D array
     * @return the copied 2D array
     */
    private int[][] copyArray(int[][] g, int size) {
        int[][] a = new int[size][];
        for (int i = 0; i < size; i++) {
            a[i] = Arrays.copyOf(g[i], g[i].length);
        }
        return a;
    }
}