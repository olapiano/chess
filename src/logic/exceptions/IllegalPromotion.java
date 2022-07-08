package logic.exceptions;

import logic.PieceName;

public class IllegalPromotion extends Exception
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public IllegalPromotion(PieceName piece)
    {
        super("Cannot promote to " + piece.toString());
    }

}
