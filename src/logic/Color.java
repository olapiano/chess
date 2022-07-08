package logic;

public enum Color
{
    WHITE('w'), BLACK('b');

    private char letter;

    Color(char letter)
    {
        this.letter = letter;
    }

    public Color changeTurn()
    {
        return values()[(ordinal() + 1) % values().length];
    }

    public char getLetter()
    {
        return letter;
    }
}
