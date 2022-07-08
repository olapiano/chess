package logic;

final public class Pawn extends Piece
{
    protected Pawn(Color color, Position position)
    {
        super(PieceName.PAWN, color, position);
    }

    @Override
    protected Position[] isLegalMove(Position newPosition, boolean isTake)
    {
        int letterDifference = newPosition.getLetter() - super.getPosition().getLetter();
        int numberDifference = newPosition.getNumber() - super.getPosition().getNumber();

        if(super.getColor() == Color.WHITE && numberDifference < 1)
            return null;
        if(super.getColor() == Color.BLACK && numberDifference > -1)
            return null;

        if(isTake)
        {
            if(Math.abs(letterDifference) == Math.abs(numberDifference) && Math.abs(numberDifference) == 1)
                return new Position[] { newPosition };
        }
        else
        {
            if(Math.abs(numberDifference) == 1 && letterDifference == 0)
            {
                return new Position[] { newPosition };
            }

            if(!super.getHasMoved() && Math.abs(numberDifference) == 2 && letterDifference == 0)
            {
                Position firstStep = Position
                        .valueOf("" + newPosition.getLetter() + (newPosition.getNumber() - numberDifference / 2));

                return new Position[] { newPosition, firstStep };
            }
        }

        return null;
    }
}
