package logic.exceptions;

public class NoPieceOnPositionException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public NoPieceOnPositionException()
    {
        super("There is no piece on the move from position");
    }
}
