package logic.exceptions;

public class IllegalMoveException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public IllegalMoveException()
    {
        super("The move is not legal");
    }
}
