import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
public class Driver
{
    public static void Main(String[] args)
    {
        // Start with an initial triangle
        Triangle initial_triangle = new Triangle(4);
        initial_triangle.Remove(1);
        System.out.println("Initial triangle:");
        initial_triangle.PrintBoard();

        Triangle t = RunTests(initial_triangle);

        // Print moves if solution found
        if (t != null)
        {
            int num_moves = t.PrintMoves();
            System.out.println(num_moves + " moves.");
        }
        else
        {
            System.out.println("No solution found.");
        }
    }

    public static Triangle RunTests(Triangle initial_triangle)
    {
        // Queue of triangles to test, each one move at a time from its parent
        // Because this is depth-first, we will find the quickest possible way
        Queue<Triangle> queue = new LinkedList<>(); 
        queue.add(initial_triangle);
        int peak_queue_size = 0;

        // boolean[] will check if triangle formation has already been checked before adding it to queue
        boolean[] checked = new boolean[(int)Math.pow(2, initial_triangle.GetBoardSize())];

        // Keep adding new triangle formations to the queue until one is complete
        Triangle t = initial_triangle;
        while (queue.size() > 0 && !t.IsComplete())
        {
            t = queue.remove();
            checked[t.AsNumber()] = true; // Add this to the list of triangles we've checked
            ArrayList<int[]> moves = FindMoves(t); // Find all possible moves
            for (int[] move : moves)
            {
                Triangle child = new Triangle(t, move);
                if (!checked[child.AsNumber()]) // If we already checked this formation, don't add it
                {
                    queue.add(child);
                }
            }

            // Record how large the queue got at its worst
            if (queue.size() > peak_queue_size)
            {
                peak_queue_size = queue.size();
            }
        }
        queue.clear();

        System.out.println("Peak queue size: " + peak_queue_size);
        if (t.IsComplete())
        {
            return t;
        }
        return null;
    }

    public static ArrayList<int[]> FindMoves(Triangle t)
    {
        ArrayList<int[]> moves = new ArrayList<int[]>(); // Each move is an array of [start, end]

        // Find where all possible moves could end
        int[] end_targets = new int[t.GetBoardSize() - t.CalculatePegsLeft()];
        int end_targets_index = 0;
        for (int i = 0; i < t.GetBoardSize(); i++)
        {
            if (!t.GetBoardValue(i))
            {
                end_targets[end_targets_index] = i;
                end_targets_index++;
            }
        }

        // Find starting places that could end in each possible end_target
        // At most 6 possible
        for (int end : end_targets)
        {
            int[] start_targets = GetStarts(end);

            // Only add to moves if start is a valid placement
            for (int start : start_targets)
            {
                if (ValidMove(t, start, end))
                {
                    moves.add(new int[] {start, end});
                }
            }
        }

        return moves;
    }

    public static int CalculateLayer(int x)
    {
        return (int)(Math.sqrt((8 * x) + 1) - 1)/2;
    }

    public static int[] GetStarts(int x)
    {
        int[] start_targets = new int[6];
        int layer = CalculateLayer(x);

        // Layers above
        start_targets[0] = x - (2 * layer) - 1;
        start_targets[1] = x - (2 * layer) + 1;

        // Same layer
        start_targets[2] = x - 2;
        start_targets[3] = x + 2;

        // Layers below
        start_targets[4] = x + (2 * layer) + 3;
        start_targets[5] = x + (2 * layer) + 5;

        return start_targets;
    }

    public static boolean ValidMove(Triangle t, int start, int end)
    {
        // There has to be a peg there
        if (!t.GetBoardValue(start))
        {
            return false;
        }

        // There should be a peg to jump
        if (!t.GetBoardValue((start + end)/2))
        {
            return false;
        }

        // Make sure it is an even number of layers away
        int start_layer = CalculateLayer(start);
        int end_layer = CalculateLayer(end);
        if ((start_layer - end_layer) % 2 == 0)
        {
            return true;
        }

        return false;
    }
}