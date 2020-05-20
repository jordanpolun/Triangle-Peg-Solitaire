public class Triangle
{
    private int board_size;
    private int pegs_left;
    private int layers;
    private boolean[] board;
    private Triangle parent;
    public Triangle(int layers)
    {
        // Initialize new board
        this.layers = layers;
        this.board_size = layers * (layers + 1)/2;
        this.pegs_left = this.board_size;
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
        this.pegs_left = t.GetPegsLeft() - 1;
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

    public int GetPegsLeft()
    {
        return this.pegs_left;
    }

    public void Remove(int index)
    {
        this.board[index] = false;
        this.pegs_left -= 1;
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
        return this.pegs_left == 1;
    }

    public Triangle GetParent()
    {
        return this.parent;
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