package logic;

// TODO Nge2

public class MoveInfo
{
    private Position position;
    private Position lastPosition;
    private char notationLetter;
    private char lastNotationLetter;
    private boolean isTake;
    private boolean isCheck;
    private boolean isCheckMate;
    private boolean isShortCastling;
    private boolean isLongCastling;

    public MoveInfo(Position position, Position lastPosition, char notationLetter, char lastNotationLetter,
            boolean isTake, boolean isCheck, boolean isCheckMate, boolean isShortCastling, boolean isLongCastling)
    {
        this.position = position;
        this.lastPosition = lastPosition;
        this.notationLetter = notationLetter;
        this.lastNotationLetter = lastNotationLetter;
        this.isTake = isTake;
        this.isCheck = isCheck;
        this.isCheckMate = isCheckMate;
        this.isShortCastling = isShortCastling;
        this.isLongCastling = isLongCastling;
    }

    @Override
    public String toString()
    {
        if(isShortCastling)
        {
            return "0-0";
        }
        if(isLongCastling)
        {
            return "0-0-0";
        }
        String info = "";

        if(notationLetter == 'P' || lastNotationLetter == 'P')
        {
            if(isTake)
            {
                info += lastPosition.getLetter();
            }
        }
        else
        {
            info += notationLetter;
        }
        if(isTake)
        {
            info += "x";
        }
        info += position.toString();
        if(lastNotationLetter != notationLetter)
        {
            info += "=" + notationLetter;
        }
        if(isCheck)
        {
            info += "+";
        }
        else if(isCheckMate)
        {
            info += "#";
        }

        return info;
    }
}
