package logic;

import java.util.ArrayList;

import logic.exceptions.CantMoveToSamePositionException;
import logic.exceptions.IllegalMoveException;
import logic.exceptions.IsCheckedException;
import logic.exceptions.PositionNotValidException;
import logic.exceptions.PotisionOccupiedBySameColorException;
import logic.exceptions.RouteBlockedException;
import logic.exceptions.TryingToTakeTheKingException;

public abstract class Piece
{
    private final PieceName PIECE_NAME;
    private final Color COLOR;

    private Position currentPosition;
    private boolean isActive = true;
    private boolean hasMoved = false;

    protected Piece(PieceName pieceName, Color color, Position position)
    {
        PIECE_NAME = pieceName;
        COLOR = color;
        this.currentPosition = position;
    }

    public Piece(Piece p)
    {
        PIECE_NAME = p.getPieceName();
        COLOR = p.getColor();
        currentPosition = p.getPosition();
        isActive = p.getIsActive();
        hasMoved = p.getHasMoved();
    }

    protected abstract Position[] isLegalMove(Position newPosition, boolean isTake);

    protected boolean move(Position newPosition, ArrayList<Piece> pieces, Board board)
            throws PositionNotValidException, CantMoveToSamePositionException, TryingToTakeTheKingException,
            PotisionOccupiedBySameColorException, RouteBlockedException, IllegalMoveException, IsCheckedException
    {
        if(newPosition == null)
        {
            throw new PositionNotValidException();
        }

        if(newPosition == currentPosition)
        {
            throw new CantMoveToSamePositionException();
        }

        boolean isTake = false;

        Piece pieceOnNewPosition = board.searchPieceByPosition(newPosition);

        if(pieceOnNewPosition != null)
        {
            if(COLOR == pieceOnNewPosition.getColor())
            {
                throw new PotisionOccupiedBySameColorException();
            }
            // TODO Needed?
            else if(pieceOnNewPosition.getNotationLetter() == 'K')
            {
                throw new TryingToTakeTheKingException();
            }
            else
            {
                isTake = true;
            }
        }

        Position[] steps = isLegalMove(newPosition, isTake);

        if(steps != null)
        {
            for(int i = 1; i < steps.length; i++)
            {
                if(!squareIsEmpty(steps[i], pieces))
                {
                    throw new RouteBlockedException();
                }
            }

            if(isTake)
            {
                pieceOnNewPosition.setIsTaken();
            }

            if(isTake || PIECE_NAME.getNotationLetter() == 'P')
            {
                board.resetMovesWithoutTakeOrPawnMove();
            }
            else
            {
                board.increaseMovesWithoutTakeOrPawnMove();
            }

            currentPosition = newPosition;
            hasMoved = true;
            board.setIsTake(isTake);
            return true;
        }
        else
        {
            throw new IllegalMoveException();
        }

    }

    protected ArrayList<Position> searchAllPossibleMoves(ArrayList<Piece> pieces, Board board)
    {
        ArrayList<Position> possibleMoves = new ArrayList<>();

        if(!isActive)
        {
            return possibleMoves;
        }
        Piece pieceOnNewPosition;
        boolean isTake = false;
        boolean canCheck;
        process: for(Position p : Position.values())
        {
            canCheck = false;

            if(p == currentPosition)
            {
                continue;
            }
            pieceOnNewPosition = board.searchPieceByPosition(p);
            if(pieceOnNewPosition != null)
            {
                if(pieceOnNewPosition.getColor() == COLOR)
                {
                    continue;
                }
                isTake = true;
                if(pieceOnNewPosition.getNotationLetter() == 'K')
                {
                    canCheck = true;
                }
            }

            Position[] steps = isLegalMove(p, isTake);

            if(steps != null && steps.length > 0)
            {
                for(int i = 1; i < steps.length; i++)
                {
                    if(board.searchPieceByPosition(steps[i]) != null)
                    {
                        continue process;
                    }
                }

                possibleMoves.add(p);
                if(canCheck)
                {
                    if(COLOR == Color.WHITE)
                    {
                        board.setIsBlackChecked();
                    }
                    if(COLOR == Color.BLACK)
                    {
                        board.setIsWhiteChecked();
                    }
                }
            }
            isTake = false;

        }
        if(COLOR == Color.WHITE)
        {
            board.addPossibleMovesForWhite(possibleMoves.size());
        }
        else
        {
            board.addPossibleMovesForBlack(possibleMoves.size());
        }
        return possibleMoves;
    }

    private boolean squareIsEmpty(Position position, ArrayList<Piece> pieces)
    {
        for(Piece piece : pieces)
        {
            if(piece.getPosition() == position)
            {
                return false;
            }
        }
        return true;
    }

    protected void setIsTaken()
    {
        currentPosition = null;
        isActive = false;
    }

    protected void setPosition(Position p)
    {
        currentPosition = p;
    }

    protected void setHasMoved(boolean hasMoved)
    {
        this.hasMoved = hasMoved;
    }

    // getters

    protected Position getPosition()
    {
        return currentPosition;
    }

    protected char getNotationLetter()
    {
        return PIECE_NAME.getNotationLetter();
    }

    protected String getName()
    {
        return PIECE_NAME.getName();
    }

    protected PieceName getPieceName()
    {
        return PIECE_NAME;
    }

    protected Color getColor()
    {
        return COLOR;
    }

    protected boolean getIsActive()
    {
        return isActive;
    }

    protected boolean getHasMoved()
    {
        return hasMoved;
    }

    @Override
    public String toString()
    {
        return String.format("[Name: %s, Notation Letter: %s, Color: %s, Position %s, Is active: %s, Has Moved: %s]",
                PIECE_NAME.getName(), PIECE_NAME.getNotationLetter(), COLOR, currentPosition, isActive, hasMoved);
    }
}
