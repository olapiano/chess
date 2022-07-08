package logic;

final public class Bishop extends Piece
{
    protected Bishop(Color color, Position position)
    {
        super(PieceName.BISHOP, color, position);
    }

    @Override
    protected Position[] isLegalMove(Position newPosition, boolean isTake)
    {
        int letterDifference = newPosition.getLetter() - super.getPosition().getLetter();
        int numberDifference = newPosition.getNumber() - super.getPosition().getNumber();

        if(Math.abs(letterDifference) != Math.abs(numberDifference))
        {
            return null;
        }

        Position[] steps = new Position[Math.abs(numberDifference)];

        for(int i = 0; i < steps.length; i++)
        {
            String positionText = "" + (char) (newPosition.getLetter() - i * letterDifference / steps.length)
                    + (newPosition.getNumber() - i * numberDifference / steps.length);

            steps[i] = Position.valueOf(positionText);
        }

        return steps;
    }

}
