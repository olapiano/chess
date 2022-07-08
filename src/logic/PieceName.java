package logic;

public enum PieceName
{

    KING("king", 'K'), QUEEN("queen", 'Q'), ROOK("tower", 'R'), BISHOP("bishop", 'B'), KNIGHT("knight",
            'N'), PAWN("pawn", 'P');

    private String name;
    private char notationLetter;

    PieceName(String name, char notationLetter)
    {
        this.name = name;
        this.notationLetter = notationLetter;
    }

    public String getName()
    {
        return name;
    }

    public char getNotationLetter()
    {
        return notationLetter;
    }
}
