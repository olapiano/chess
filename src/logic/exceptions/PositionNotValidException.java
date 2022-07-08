package logic.exceptions;

public class PositionNotValidException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PositionNotValidException()
    {
        super("The position is not valid!");
    }
}
