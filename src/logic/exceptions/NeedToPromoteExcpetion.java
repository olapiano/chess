package logic.exceptions;

import logic.Position;

public class NeedToPromoteExcpetion extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public NeedToPromoteExcpetion(Position p)
    {
        super("Promote pawn on position: " + p.toString());
    }
}
