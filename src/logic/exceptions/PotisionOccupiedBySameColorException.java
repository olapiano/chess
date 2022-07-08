package logic.exceptions;

public class PotisionOccupiedBySameColorException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PotisionOccupiedBySameColorException()
    {
        super("The square is occupied by a piece of the same color");
    }
}
