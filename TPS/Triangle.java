public class Triangle
{
    private int board_size;
    private int layers;
    private boolean[] board;
    private Triangle parent;
    public Triangle(int layers)
    {
        // Initialize new board
        this.layers = layers;
        this.board_size = layers * (layers + 1)/2;
        this.board = new boolean[this.board_size];
        this.parent = null;

        // Fill board with pegs
        for (int i = 0; i < this.board_size; i++)
        {
            this.board[i] = true;
        }
    }

    public Triangle(Triangle t, int[] move)
    {
        // Create duplicate of board
        this.layers = t.GetLayers();
        this.board_size = t.GetBoardSize();
        this.board = new boolean[this.board_size];
        for (int i = 0; i < this.board_size; i++)
        {
            this.board[i] = t.GetBoardValue(i);
        }
        this.parent = t;

        // Make move
        int start_index = move[0];
        int end_index = move[1];
        this.board[start_index] = false;
        this.board[end_index] = true;
        this.board[(start_index + end_index)/2] = false;
    }

    public int GetLayers()
    {
        return this.layers;
    }

    public boolean GetBoardValue(int index)
    {
        return  index < this.board_size && // Make sure index is on the board
        index >= 0 && // Make sure index isn't negative
        this.board[index]; // Check if it's there
    }

    public int GetBoardSize()
    {
        return this.board_size;
    }

    public void Remove(int index)
    {
        this.board[index] = false;
    }

    public int CalculatePegsLeft()
    {
        int count = 0;
        for (int i = 0; i < this.board_size; i++)
        {
            if (this.board[i])
            {
                count++;
            }
        }
        return count;
    }

    public boolean IsComplete()
    {
        return this.CalculatePegsLeft() == 1;
    }

    public Triangle GetParent()
    {
        return this.parent;
    }

    public int PrintMoves()
    {
        int n = 1;
        if (this.parent != null)
        {
            n = this.parent.PrintMoves() + 1;
        }

        this.PrintBoard();
        return n;
    }

    public int AsNumber()
    {
        // Convert board of true (1) and false (0) into decimal as if binary
        int num = 0;
        int multiplier = 1;
        for (boolean b : this.board)
        {
            if (b)
            {
                num += multiplier;
            }
            multiplier *= 2;
        }
        return num;
    }

    public void PrintBoard()
    {
        // outer loop to handle number of rows 
        //  n in this case 
        int index = 0;
        for (int layer = 0; layer < this.layers; layer++) 
        { 

            // inner loop to handle number spaces 
            // values changing acc. to requirement 
            for (int j = this.layers - layer; j > 1; j--) 
            { 
                // printing spaces 
                System.out.print(" "); 
            } 

            //  inner loop to handle number of columns 
            //  values changing acc. to outer loop 
            for (int col = 0; col <= layer; col++ ) 
            { 
                // printing stars 
                if (this.board[index])
                {
                    System.out.print("X "); 
                }
                else
                {
                    System.out.print("O ");
                }
                index++;
            } 

            // ending line after each row 
            System.out.println(); 
        } 
    }
}