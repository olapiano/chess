package logic.exceptions;

import logic.Color;

public class IsCheckedException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public IsCheckedException(Color color)
    {
        super(String.format("%s is in check!", color == Color.WHITE ? "White" : "Black"));
    }
}
