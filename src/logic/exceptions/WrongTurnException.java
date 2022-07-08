package logic.exceptions;

public class WrongTurnException extends Exception
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public WrongTurnException(String turn)
    {
        super(String.format("It is %ss turn to move!", turn));
    }

}
