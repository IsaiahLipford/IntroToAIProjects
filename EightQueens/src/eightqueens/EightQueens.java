package eightqueens;

import java.util.*;

/**
 * @author Isaiah Lipford
 * @version 05/26/2024
 *
 * Heuristic: How far away you are from the goal. In this case, it is the # of
 * conflicts.
 */
public class EightQueens {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int restarts = 0;
        int numStateChanges = 0;
        final int size = 8;

        HillClimbing hillClimbing = new HillClimbing(size);

        //While Loop
        while (!hillClimbing.isGoal()) {
            int h = hillClimbing.getHeuristic();
            //Display Info
            System.out.println("Current h: " + h);
            System.out.println("Current State");
            System.out.println(hillClimbing.toString());            

            //Info of state neighbors
            HashMap neighbors = hillClimbing.getAllNeighbors();
            Set set = neighbors.entrySet();
            Iterator i = set.iterator();

            int lowerNeighbors = 0; //The number of neighbors with the lowest heuristic
            HillClimbing lowestNeighbor = hillClimbing; //The lowest neighbor

            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                if ((int) me.getValue() < h) {
                    lowerNeighbors++;
                }

                if (((HillClimbing) me.getKey()).getHeuristic() < lowestNeighbor.getHeuristic()) {
                    lowestNeighbor = (HillClimbing) me.getKey();
                }
            }

            System.out.println("Neighbors found with lower h: " + lowerNeighbors);

            //If no lower neighbors, restart and generate new random state/hill-climbing algorithm
            if (lowerNeighbors == 0) {
                System.out.println("RESTART");
                hillClimbing = new HillClimbing(size);
                restarts++;
            } else {
                System.out.println("Setting new current state\n");
                hillClimbing = lowestNeighbor;
                numStateChanges++;
            }
        } //End of while loop

        //Display the solution and final info
        System.out.println(hillClimbing.toString());
        System.out.println("Solution Found!");
        System.out.println("State Changes: " + numStateChanges);
        System.out.println("Restarts: " + restarts);
    }
}
