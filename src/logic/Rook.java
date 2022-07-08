package logic;

final public class Rook extends Piece
{
    protected Rook(Color color, Position position)
    {
        super(PieceName.ROOK, color, position);
    }

    @Override
    protected Position[] isLegalMove(Position newPosition, boolean isTake)
    {
        int letterDifference = newPosition.getLetter() - super.getPosition().getLetter();
        int numberDifference = newPosition.getNumber() - super.getPosition().getNumber();

        if(letterDifference == numberDifference)
            return null;

        if(letterDifference == 0 || numberDifference == 0)
        {
            Position[] steps;

            if(letterDifference != 0)
            {
                steps = new Position[Math.abs(letterDifference)];
            }
            else
            {
                steps = new Position[Math.abs(numberDifference)];
            }

            for(int i = 0; i < steps.length; i++)
            {

                steps[i] = Position.valueOf("" + (char) (newPosition.getLetter() - i * letterDifference / steps.length)
                        + (newPosition.getNumber() - i * numberDifference / steps.length));
            }

            return steps;
        }

        return null;
    }
}
