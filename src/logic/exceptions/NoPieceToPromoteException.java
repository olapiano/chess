package logic.exceptions;

public class NoPieceToPromoteException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public NoPieceToPromoteException()
    {
        super("There is no piece to promote");
    }
}
