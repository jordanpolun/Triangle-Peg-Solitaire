import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
public class Driver
{
    public static void Main(String[] args)
    {
        Triangle initial_triangle = new Triangle(6);
        initial_triangle.Remove(1);
        System.out.println("Initial triangle:");
        initial_triangle.PrintBoard();

        // Queue of triangles to test, each one move at a time from its parent
        Queue<Triangle> queue = new LinkedList<>(); 
        queue.add(initial_triangle);
        int peak_queue_size = 0;

        // Keep adding new triangle formations to the queue until one is complete
        Triangle t = initial_triangle;
        while (queue.size() > 0 && !t.IsComplete())
        {
            t = queue.remove();
            ArrayList<int[]> moves = FindMoves(t);
            for (int[] move : moves)
            {
                queue.add(new Triangle(t, move));
            }

            // Record how large the queue got at its worst
            if (queue.size() > peak_queue_size)
            {
                peak_queue_size = queue.size();
            }
        }
        queue.clear();

        // Print moves
        while (t.GetParent() != null)
        {
            t.PrintBoard();
            System.out.println();
            t = t.GetParent();
        }
        t.PrintBoard();

        System.out.println("Working backwards ^^");
        System.out.println("Peak queue size: " + peak_queue_size);
    }

    public static ArrayList<int[]> FindMoves(Triangle t)
    {
        ArrayList<int[]> moves = new ArrayList<int[]>(); // Each move is an array of [start, end]

        // Find where all possible moves could end
        int[] end_targets = new int[t.GetBoardSize() - t.GetPegsLeft()];
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