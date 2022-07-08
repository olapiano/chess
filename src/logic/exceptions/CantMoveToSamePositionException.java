package logic.exceptions;

public class CantMoveToSamePositionException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CantMoveToSamePositionException()
    {
        super("A move must be different from current");
    }
}
