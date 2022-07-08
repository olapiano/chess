package logic.exceptions;

public class TryingToTakeTheKingException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TryingToTakeTheKingException()
    {
        super("It is not possible to take the king.");
    }
}
