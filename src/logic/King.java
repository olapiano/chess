package logic;

final public class King extends Piece
{
    protected King(Color color, Position position)
    {
        super(PieceName.KING, color, position);
    }

    @Override
    protected Position[] isLegalMove(Position newPosition, boolean isTake)
    {
        int letterDifference = newPosition.getLetter() - super.getPosition().getLetter();
        int numberDifference = newPosition.getNumber() - super.getPosition().getNumber();

        if(Math.abs(letterDifference) == 2 && numberDifference == 0)
        {
            castling();
        }
        else if(Math.abs(letterDifference) <= 1 && Math.abs(numberDifference) <= 1)
        {
            return new Position[] { newPosition };
        }

        return null;
    }

    // TODO fix castling
    private boolean castling()
    {
        if(super.getHasMoved() == true)
            return false;

        return false;
    }

}
