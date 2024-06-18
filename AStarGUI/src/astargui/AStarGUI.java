package astargui;

import java.util.*;
import java.util.regex.Pattern;
/**
 * AStarGUI is a graphical user interface for the A* search algorithm.
 * It provides a 15x15 game board where the user can specify a start node and a goal node.
 * The algorithm then finds the shortest path from the start node to the goal node using the A* search algorithm.
 * 
 * Note: 
 * '0' --> Pathable Blocks
 * '1' --> Unpathable Blocks
 * "Green Path" --> Final Solution
 * "Red Block" --> Goal Node
 * "Blue Blocks" --> The system reading all of the blocks to properly choose correct path
 * 
 * @author Isaiah Lipford
 * @version 6/3/2024
 */
public class AStarGUI {
    /**
     * The size of the game board.
     */
    public static int boardSize = 15;

    /**
     * The open list of nodes to be explored.
     */
    public static List<Node> openList = new ArrayList<>();

    /**
     * The closed list of nodes that have been explored.
     */
    public static List<Node> closedList = new ArrayList<>();

    /**
     * The shortest path from the start node to the goal node.
     */
    public static List<Node> shortestPath = new ArrayList<>();

    /**
     * The start node of the search.
     */
    public static Node startNode = new Node(-1, -1, 0);

    /**
     * The goal node of the search.
     */
    public static Node goalNode = new Node(-1, -1, 0);
    
    /**
     * Creates a 15x15 game board with random pathable nodes.
     * @param boardSize the size of the game board
     * @return a 2D list of nodes representing the game board
     */
    public static List<List<Node>> createGameBoard(int boardSize) {
        List<List<Node>> gameBoard = new ArrayList<>();
        for (int r = 0; r < boardSize; r++) {
            gameBoard.add(new ArrayList<>());
            for (int c = 0; c < boardSize; c++) {
                int pathable = Math.random() * 100 <= 30 ? 1 : 0;
                gameBoard.get(r).add(new Node(r, c, pathable));
            }
        }
        return gameBoard;
    }
    
    /**
     * Prints the game board to the console.
     * 
     * @param gameBoard the game board to be printed
     */
    public static void printBoard(List<List<Node>> gameBoard) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clearConsole();
        System.out.println("                      A * SEARCH");
        System.out.println("                      -------------");

        System.out.println("     0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 \n   +---------------------------------------------+");

        for (int r = 0; r < boardSize; r++) {
            if (r < 10) {
                System.out.print(r + "  |");
            } else {
                System.out.print(r + " |");
            }

            for (int c = 0; c < boardSize; c++) {
                Node tempNode = gameBoard.get(r).get(c);
                int pathable = tempNode.getType();
                if (r == startNode.getRow() && c == startNode.getCol()) {
                    System.out.print(Colors.BLACK + Colors.GREEN_BACKGROUND + " " + pathable + " " + Colors.RESET);
                } else if (r == goalNode.getRow() && c == goalNode.getCol()) {
                    System.out.print(Colors.BLACK + Colors.RED_BACKGROUND + " " + pathable + " " + Colors.RESET);
                } else {
                    if (pathable == 0) {
                        if (shortestPath.contains(tempNode)) {
                            System.out.print(Colors.BLACK + Colors.GREEN_BACKGROUND + " " + pathable + " " + Colors.RESET);
                        } else if (closedList.contains(tempNode)) {
                            System.out.print(Colors.BLACK + Colors.BLUE_BACKGROUND + " " + pathable + " " + Colors.RESET);
                        } else {
                            System.out.print(Colors.BLACK + Colors.WHITE_BACKGROUND + " " + pathable + " " + Colors.RESET);
                        }
                    } else {
                        System.out.print(Colors.GREY_BACKGROUND + " " + pathable + " " + Colors.RESET);
                    }
                }
            }
            System.out.println(" | ");
        }
        System.out.println("   +---------------------------------------------+");
    }
    
    /**
     * The main method of the program.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<List<Node>> gameBoard = createGameBoard(boardSize);
        printBoard(gameBoard);

        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Enter " + Colors.GREEN + "Start Node" + Colors.RESET + " coordinates in the format: row,column");
            String temp = sc.nextLine();
            if (Pattern.matches(".*,.*", temp)) {
                try {
                    int r = Integer.parseInt(temp.split(",")[0]);
                    int c = Integer.parseInt(temp.split(",")[1]);
                    Node tempNode = new Node(r, c, 0);
                    if (isValid(tempNode, boardSize)) {
                        if (gameBoard.get(r).get(c).getType() == 0) {
                            startNode = tempNode;
                            break;
                        } else {
                            System.out.println("Unable to make the " + Colors.GREEN + "Starting Node" + Colors.RESET + " a blocked node");
                        }
                    } else {
                        System.out.println("Please enter coordinates that range from 0 - " + (boardSize - 1));
                    }
                } catch (Exception e) {
                    System.out.println("Only Numbers are accepted");
                }
            } else {
                System.out.println("Enter coordinates in the format: row,column");
            }
        } while (true);

        printBoard(gameBoard);

        do {
            System.out.println("Enter " + Colors.RED + "Goal Node" + Colors.RESET + " coordinates in the format: row,column");
            String temp = sc.nextLine();
            if (Pattern.matches(".*,.*", temp)) {
                try {
                    int r = Integer.parseInt(temp.split(",")[0]);
                    int c = Integer.parseInt(temp.split(",")[1]);
                    Node tempNode = new Node(r, c, 0);
                    if (isValid(tempNode, boardSize)) {
                        if (gameBoard.get(r).get(c).getType() == 0) {
                            goalNode = tempNode;
                            break;
                        } else {
                            System.out.println("Unable to make the " + Colors.RED + "Goal Node" + Colors.RESET + " a blocked node");
                        }
                    } else {
                        System.out.println("Please enter coordinates that range from 0 - " + (boardSize - 1));
                    }
                } catch (Exception e) {
                    System.out.println("Only Numbers are accepted");
                }
            } else {
                System.out.println("Enter coordinates in the format: row,column");
            }
        } while (true);

        printBoard(gameBoard);

        aStar(gameBoard);

        sc.close();
    }
    
    /**
     * Calculates the heuristic value of a node.
     * 
     * @param n the node to calculate the heuristic value for
     * @return the heuristic value of the node
     */
    public static int calculateH(Node n) {
        return Math.abs(n.getRow() - goalNode.getRow()) + Math.abs(n.getCol() - goalNode.getCol());
    }
    
    /**
     * Checks if a node is valid (i.e., within the game board boundaries).
     * 
     * @param n the node to check
     * @param boardSize the size of the game board
     * @return true if the node is valid, false otherwise
     */
    public static boolean isValid(Node n, int boardSize) {
        int r = n.getRow();
        int c = n.getCol();
        return r >= 0 && r < boardSize && c >= 0 && c < boardSize;
    }
    
    /**
     * Sorts a list of nodes based on their F values.
     * 
     * @param sortList the list of nodes to be sorted
     * @return the sorted list of nodes
     */
    public static List<Node> sort(List<Node> sortList) {
        Node n;
        for (int i = 0; i < sortList.size(); i++) {
            int fLow = i;
            for (int id = i; id < sortList.size() - 1; id++) {
                if (sortList.get(id + 1).getF() < sortList.get(fLow).getF()) {
                    fLow = id + 1;
                }
            }
            n = sortList.get(i);
            sortList.set(i, sortList.get(fLow));
            sortList.set(fLow, n);
        }
        return sortList;
    }
    
    /**
     * Implements the A* search algorithm.
     * 
     * @param gameBoard the game board to search
     */
    public static void aStar(List<List<Node>> gameBoard) {
        openList = new ArrayList<>();
        closedList = new ArrayList<>();

        openList.add(startNode);

        do {
            Node q = openList.get(0);
            openList.remove(q);

            List<Node> dependents = new ArrayList<>();
            dependents.add(new Node(q.getRow(), q.getCol() + 1, 0)); // right
            dependents.add(new Node(q.getRow(), q.getCol() - 1, 0)); // left
            dependents.add(new Node(q.getRow() + 1, q.getCol(), 0)); // down
            dependents.add(new Node(q.getRow() - 1, q.getCol(), 0)); // up

            for (int i = 0; i < dependents.size(); i++) {
                dependents.get(i).setParent(q);
                if (dependents.get(i).equals(goalNode)) {
                    closedList.add(q);
                    printBoard(gameBoard);

                    Node tempNode = dependents.get(i);
                    while (tempNode != startNode) {
                        shortestPath.add(tempNode);
                        printBoard(gameBoard);
                        tempNode = tempNode.getParent();
                    }
                    printBoard(gameBoard);
                    System.out.println("Solution Found!");
                    return;
                }

                if (!isValid(dependents.get(i), boardSize)) {
                    continue;
                }

                if (gameBoard.get(dependents.get(i).getRow()).get(dependents.get(i).getCol()).getType() == 1) {
                    continue;
                }

                int g = q.getG() + 1;
                dependents.get(i).setG(g);
                int h = calculateH(dependents.get(i));
                dependents.get(i).setH(h);
                dependents.get(i).setF();

                boolean skip = false;
                for (int o = 0; o < openList.size(); o++) {
                    if (dependents.get(i).equals(openList.get(o)) && openList.get(o).getF() < dependents.get(i).getF()) {
                        skip = true;
                        break;
                    }
                }
                if (skip) {
                    continue;
                }

                for (int o = 0; o < closedList.size(); o++) {
                    if (dependents.get(i).equals(closedList.get(o)) && closedList.get(o).getF() < dependents.get(i).getF()) {
                        skip = true;
                        break;
                    }
                }
                if (skip) {
                    continue;
                } else {
                    openList.add(dependents.get(i));
                }
            }
            printBoard(gameBoard);
            closedList.add(q);
            openList = sort(openList);
        } while (openList.size() != 0);
        System.out.println("No path could be found!");
    }
    
    /**
     * Clears the console.
     */
    public static void clearConsole() {
        try {
            String operatingSystem = System.getProperty("os.name");
            if (operatingSystem.contains("Windows")) {
                Process startProcess = new ProcessBuilder("cmd", "/c", "cls").inheritIO().start();
                startProcess.waitFor();
            } else {
                Process startProcess = new ProcessBuilder("clear").inheritIO().start();
                startProcess.waitFor();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
