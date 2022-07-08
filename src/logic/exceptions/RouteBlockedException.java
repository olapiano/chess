package logic.exceptions;

public class RouteBlockedException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RouteBlockedException()
    {
        super("The route to new position is blocked by another piece");
    }
}
