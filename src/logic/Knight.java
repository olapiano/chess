package logic;

final public class Knight extends Piece
{
    protected Knight(Color color, Position position)
    {
        super(PieceName.KNIGHT, color, position);
    }

    @Override
    protected Position[] isLegalMove(Position newPosition, boolean isTake)
    {
        int letterDifference = newPosition.getLetter() - super.getPosition().getLetter();
        int numberDifference = newPosition.getNumber() - super.getPosition().getNumber();

        if(Math.abs(letterDifference) == 2 && Math.abs(numberDifference) == 1
                || Math.abs(letterDifference) == 1 && Math.abs(numberDifference) == 2)
        {
            return new Position[] { newPosition };
        }

        return null;
    }
}
