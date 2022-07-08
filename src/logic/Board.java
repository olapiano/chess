package logic;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import logic.exceptions.CantMoveToSamePositionException;
import logic.exceptions.GameNotActive;
import logic.exceptions.IllegalMoveException;
import logic.exceptions.IllegalPromotion;
import logic.exceptions.IsCheckedException;
import logic.exceptions.NeedToPromoteExcpetion;
import logic.exceptions.NoPieceOnPositionException;
import logic.exceptions.NoPieceToPromoteException;
import logic.exceptions.PositionNotValidException;
import logic.exceptions.PotisionOccupiedBySameColorException;
import logic.exceptions.RouteBlockedException;
import logic.exceptions.TryingToTakeTheKingException;
import logic.exceptions.WrongTurnException;

public class Board
{
    private ArrayList<Piece> pieces;
    private ArrayList<ArrayList<Piece>> savedPositions;

    private GameStatus gameStatus;
    private int movesWithoutTakeOrPawnMove = 0;

    private Color playerTurn = Color.WHITE;
    private boolean isWhiteChecked = false;
    private boolean isBlackChecked = false;

    private Piece pieceToPromote = null;
    private boolean isTake = false;

    private int possibleMovesForWhite = 0;
    private int possibleMovesForBlack = 0;

    private GameInfo gameInfo;

    public Board()
    {
        pieces = new ArrayList<>();
        savedPositions = new ArrayList<>();
        gameStatus = GameStatus.INACTIVE;
        gameInfo = new GameInfo();
    }

    public void setUpStartPosition()
    {
        pieces.clear();
        savedPositions.clear();

        // white pieces
        pieces.add(new Rook(Color.WHITE, Position.a1));
        pieces.add(new Knight(Color.WHITE, Position.b1));
        pieces.add(new Bishop(Color.WHITE, Position.c1));
        pieces.add(new Queen(Color.WHITE, Position.d1));
        pieces.add(new King(Color.WHITE, Position.e1));
        pieces.add(new Bishop(Color.WHITE, Position.f1));
        pieces.add(new Knight(Color.WHITE, Position.g1));
        pieces.add(new Rook(Color.WHITE, Position.h1));
        pieces.add(new Pawn(Color.WHITE, Position.a2));
        pieces.add(new Pawn(Color.WHITE, Position.b2));
        pieces.add(new Pawn(Color.WHITE, Position.c2));
        pieces.add(new Pawn(Color.WHITE, Position.d2));
        pieces.add(new Pawn(Color.WHITE, Position.e2));
        pieces.add(new Pawn(Color.WHITE, Position.f2));
        pieces.add(new Pawn(Color.WHITE, Position.g2));
        pieces.add(new Pawn(Color.WHITE, Position.h2));

        // black pieces
        pieces.add(new Rook(Color.BLACK, Position.a8));
        pieces.add(new Knight(Color.BLACK, Position.b8));
        pieces.add(new Bishop(Color.BLACK, Position.c8));
        pieces.add(new Queen(Color.BLACK, Position.d8));
        pieces.add(new King(Color.BLACK, Position.e8));
        pieces.add(new Bishop(Color.BLACK, Position.f8));
        pieces.add(new Knight(Color.BLACK, Position.g8));
        pieces.add(new Rook(Color.BLACK, Position.h8));
        pieces.add(new Pawn(Color.BLACK, Position.a7));
        pieces.add(new Pawn(Color.BLACK, Position.b7));
        pieces.add(new Pawn(Color.BLACK, Position.c7));
        pieces.add(new Pawn(Color.BLACK, Position.d7));
        pieces.add(new Pawn(Color.BLACK, Position.e7));
        pieces.add(new Pawn(Color.BLACK, Position.f7));
        pieces.add(new Pawn(Color.BLACK, Position.g7));
        pieces.add(new Pawn(Color.BLACK, Position.h7));

        savePosition();

        gameStatus = GameStatus.ACTIVE;
        playerTurn = Color.WHITE;
        movesWithoutTakeOrPawnMove = 0;
        isWhiteChecked = false;
        isBlackChecked = false;
        pieceToPromote = null;
        gameInfo = new GameInfo();

    }

    public boolean move(Position currentPosition, Position newPosition)
            throws WrongTurnException, NoPieceOnPositionException, PositionNotValidException,
            CantMoveToSamePositionException, TryingToTakeTheKingException, PotisionOccupiedBySameColorException,
            RouteBlockedException, IllegalMoveException, IsCheckedException, GameNotActive, NeedToPromoteExcpetion
    {
        isTake = false;
        possibleMovesForWhite = 0;
        possibleMovesForBlack = 0;

        if(gameStatus != GameStatus.ACTIVE)
        {
            throw new GameNotActive(gameStatus);
        }
        if(pieceToPromote != null)
        {
            throw new NeedToPromoteExcpetion(pieceToPromote.getPosition());
        }
        Piece pieceOnCurrentPosition = searchPieceByPosition(currentPosition);

        if(pieceOnCurrentPosition == null)
        {
            throw new NoPieceOnPositionException();
        }

        if(playerTurn == Color.BLACK && pieceOnCurrentPosition.getColor() == Color.WHITE
                || playerTurn == Color.WHITE && pieceOnCurrentPosition.getColor() == Color.BLACK)
        {
            throw new WrongTurnException(playerTurn.toString().toLowerCase());
        }

        isWhiteChecked = false;
        isBlackChecked = false;

        if(!pieceOnCurrentPosition.move(newPosition, pieces, this))
        {
            throw new IllegalMoveException();
        }

        for(Piece piece : pieces)
        {
            piece.searchAllPossibleMoves(pieces, this);
        }

        if((playerTurn == Color.WHITE && isWhiteChecked) || (playerTurn == Color.BLACK && isBlackChecked))
        {
            pieceOnCurrentPosition.setPosition(currentPosition);
            throw new IsCheckedException(pieceOnCurrentPosition.getColor());
        }

        if(pieceOnCurrentPosition instanceof Pawn && pieceOnCurrentPosition.getPosition().isPromotionRank())
        {
            pieceToPromote = pieceOnCurrentPosition;

            throw new NeedToPromoteExcpetion(pieceOnCurrentPosition.getPosition());

        }
        playerTurn = playerTurn.changeTurn();
        savePosition();
        System.out.println("repeted positions: " + countRepetedPositions());
        if(countRepetedPositions() >= 5 || movesWithoutTakeOrPawnMove >= 75)
        {
            gameStatus = GameStatus.DRAW;
            gameInfo.end(gameStatus);

        }

        // TODO Test
        if((playerTurn == Color.WHITE && possibleMovesForWhite == 0)
                || playerTurn == Color.BLACK && possibleMovesForBlack == 0)
        {
            gameStatus = GameStatus.DRAW;
        }

        // TODO Test
        boolean whiteBishop = true, blackBishop = false, whiteKnight = false, blackKnight = false, otherPiece = false;
        boolean whiteBishopOnWhiteSquare = false, blackBishopOnWhiteSquare = false;
        process: for(Piece piece : pieces)
        {
            if(!piece.getIsActive())
            {
                continue;
            }
            switch(piece.getNotationLetter())
            {
                case 'K':
                    continue;
                case 'B':
                    if(piece.getColor() == Color.WHITE)
                    {
                        whiteBishop = true;
                        if(piece.getPosition().getColor() == Color.WHITE)
                        {
                            whiteBishopOnWhiteSquare = true;
                        }
                    }
                    else
                    {
                        blackBishop = true;
                        if(piece.getPosition().getColor() == Color.WHITE)
                        {
                            blackBishopOnWhiteSquare = true;
                        }
                    }
                    break;
                case 'N':
                    if(piece.getColor() == Color.WHITE)
                    {
                        whiteKnight = true;
                    }
                    else
                    {
                        blackKnight = true;
                    }
                    break;
                default:
                    otherPiece = true;
                    break process;
            }
        }
        if(!otherPiece)
        {
            if((!whiteKnight && !blackKnight
                    && ((!whiteBishop || !blackBishop) || whiteBishopOnWhiteSquare == blackBishopOnWhiteSquare))
                    || (!whiteBishop && !blackBishop && (!whiteKnight || !blackKnight)))
            {
                gameStatus = GameStatus.DRAW;

            }
        }

        if(whiteCheckMate())
        {
            gameStatus = GameStatus.WHITE_WINS;
        }

        if(blackCheckMate())
        {
            gameStatus = GameStatus.BLACK_WINS;
        }

        // TODO fix castling and checkmate!!!!
        gameInfo.addMove(newPosition, currentPosition, pieceOnCurrentPosition.getNotationLetter(),
                pieceOnCurrentPosition.getNotationLetter(), isTake,
                playerTurn == Color.WHITE ? isWhiteChecked : isBlackChecked, false, false, false);

        return true;
    }

    protected Piece searchPieceByPosition(Position position)
    {
        for(Piece piece : pieces)
        {
            if(piece.getPosition() == position)
            {
                return piece;
            }
        }

        return null;
    }

    private void savePosition()
    {
        ArrayList<Piece> temp = new ArrayList<>();
        for(Piece p : pieces)
        {
            Piece p2;
            if(p instanceof King)
            {
                p2 = new King(p.getColor(), p.getPosition());
            }
            else if(p instanceof Queen)
            {
                p2 = new Queen(p.getColor(), p.getPosition());
            }
            else if(p instanceof Rook)
            {
                p2 = new Rook(p.getColor(), p.getPosition());
            }
            else if(p instanceof Bishop)
            {
                p2 = new Bishop(p.getColor(), p.getPosition());
            }
            else if(p instanceof Knight)
            {
                p2 = new Knight(p.getColor(), p.getPosition());
            }
            else
            {
                p2 = new Pawn(p.getColor(), p.getPosition());
            }
            temp.add(p2);
        }

        savedPositions.add(temp);
    }

    public int countRepetedPositions()
    {
        int repetitions = 0;
        for(ArrayList<Piece> list : savedPositions)
        {
            int equalPositions = 0;
            for(int i = 0; i < list.size(); i++)
            {
                if(list.get(i).getPosition() == pieces.get(i).getPosition()
                        && list.get(i).getColor() == pieces.get(i).getColor()
                        && list.get(i).getNotationLetter() == pieces.get(i).getNotationLetter())
                {
                    equalPositions++;
                }
            }
            if(equalPositions == pieces.size())
            {
                repetitions++;
            }
        }
        return repetitions;
    }

    public boolean promote(PieceName pieceName) throws NoPieceToPromoteException, IllegalPromotion
    {
        if(pieceToPromote == null)
        {
            throw new NoPieceToPromoteException();
        }

        int index = -1;
        for(int i = 0; i < pieces.size(); i++)
        {
            if(pieces.get(i) == pieceToPromote)
            {
                index = i;
            }
        }

        if(index == -1)
        {
            throw new NoPieceToPromoteException();
        }

        switch(pieceName)
        {
            case QUEEN:
                pieces.set(index, new Queen(pieceToPromote.getColor(), pieceToPromote.getPosition()));
                break;

            case ROOK:
                pieces.set(index, new Rook(pieceToPromote.getColor(), pieceToPromote.getPosition()));
                break;

            case BISHOP:
                pieces.set(index, new Bishop(pieceToPromote.getColor(), pieceToPromote.getPosition()));
                break;

            case KNIGHT:
                pieces.set(index, new Knight(pieceToPromote.getColor(), pieceToPromote.getPosition()));
                break;

            default:
                throw new IllegalPromotion(pieceName);
        }

        playerTurn = playerTurn.changeTurn();
        savePosition();
        System.out.println("notationLetter: " + pieceToPromote.getNotationLetter());
        gameInfo.addMove(pieceToPromote.getPosition(),
                savedPositions.get(savedPositions.size() - 2).get(index).getPosition(),
                pieces.get(index).getNotationLetter(), 'P', isTake,
                playerTurn == Color.WHITE ? isWhiteChecked : isBlackChecked, false, false, false);
        pieceToPromote = null;
        return true;
    }

    // Fix check mate
    private boolean whiteCheckMate()
    {
        if(playerTurn == Color.BLACK && isBlackChecked)
        {
            for(Piece piece : pieces)
            {
                if(piece.getColor() == Color.BLACK)
                {

                }
            }
        }

        return false;
    }

    private boolean blackCheckMate()
    {

        return false;
    }

    // IO operations

    public boolean savePGNToFile(File file) throws IOException
    {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(gameInfo.toString());
        fileWriter.close();
        return true;
    }

    public boolean readPGNFromFile(File file) throws IOException
    {
        FileReader fileReader = new FileReader(file);
        int letter = 0;
        String text = "";
        while((letter = fileReader.read()) != -1)
        {
            text += (char) letter;
        }
        System.out.println(text);

        fileReader.close();
        return true;
    }

    // print methods

    public void printAllSavedPositions()
    {
        System.out.println("Saved positions (" + savedPositions.size() + ")");

        for(ArrayList<Piece> list : savedPositions)
        {
            System.out.println("\n----Piece positions------\n");
            for(int col = 7; col >= 0; col--)
            {
                System.out.print(col + 1 + " ");
                for(int row = 0; row < 8; row++)
                {
                    boolean tick = false;
                    for(Piece piece : list)
                    {
                        if(piece.getPosition() == Position.getPositionByIndex(col * 8 + row))
                        {
                            System.out.print(piece.getColor().getLetter() + "" + piece.getNotationLetter() + " ");
                            tick = true;
                        }
                    }
                    if(tick == false)
                    {
                        System.out.print("   ");
                    }
                }
                System.out.println("\n");
            }
            System.out.println("  a  b  c  d  e  f  g  h");
        }
    }

    public void printAllPossibleMoves()
    {

        for(Piece piece : pieces)
        {
            System.out.println(piece.toString());
            for(Position p : piece.searchAllPossibleMoves(pieces, this))
            {
                System.out.println(p);
            }
        }

        System.out.println("White is checked: " + isWhiteChecked + ", Black is checked: " + isBlackChecked);
    }

    public void printBoardPositions()
    {
        System.out.println("\n-----Board positions-----\n");
        for(int col = 7; col >= 0; col--)
        {
            System.out.print(col + 1 + " ");

            for(int row = 0; row < 8; row++)
            {
                System.out.print(Position.values()[col * 8 + row] + " ");

            }
            System.out.println("\n");
        }
        System.out.println("  a  b  c  d  e  f  g  h");
    }

    public void printBoardColors()
    {
        System.out.println("\n------Board colors------\n");
        for(int col = 7; col >= 0; col--)
        {
            System.out.print(col + 1 + " ");

            for(int row = 0; row < 8; row++)
            {
                System.out.print(Position.values()[col * 8 + row].getColor() + "  ");
            }
            System.out.println("\n");
        }
        System.out.println("  a  b  c  d  e  f  g  h");
    }

    public void printPositionOfPieces()
    {
        System.out.println("\n----Piece positions------\n");
        for(int col = 7; col >= 0; col--)
        {
            System.out.print(col + 1 + " ");
            for(int row = 0; row < 8; row++)
            {
                boolean tick = false;
                for(Piece piece : pieces)
                {
                    if(piece.getPosition() == Position.getPositionByIndex(col * 8 + row))
                    {
                        System.out.print(piece.getColor().getLetter() + "" + piece.getNotationLetter() + " ");
                        tick = true;
                    }
                }
                if(tick == false)
                {
                    System.out.print("   ");
                }
            }
            System.out.println("\n");
        }
        System.out.println("  a  b  c  d  e  f  g  h");
    }

    public void printPiecesList()
    {
        System.out.println("--------List of pieces--------");
        for(Piece peice : pieces)
        {
            System.out.println(peice.toString());
        }
    }

    // setters

    protected void resetMovesWithoutTakeOrPawnMove()
    {
        movesWithoutTakeOrPawnMove = 0;
    }

    protected void increaseMovesWithoutTakeOrPawnMove()
    {
        movesWithoutTakeOrPawnMove++;
    }

    protected void setIsWhiteChecked()
    {
        isWhiteChecked = true;
    }

    protected void setIsWhiteNotChecked()
    {
        isWhiteChecked = false;
    }

    protected void setIsBlackChecked()
    {
        isBlackChecked = true;
    }

    protected void setIsBlackNotChecked()
    {
        isBlackChecked = false;
    }

    protected void setIsTake(boolean isTake)
    {
        this.isTake = isTake;
    }

    public void addPossibleMovesForWhite(int possibleMoves)
    {
        possibleMovesForWhite += possibleMoves;
    }

    public void addPossibleMovesForBlack(int possibleMoves)
    {
        possibleMovesForBlack += possibleMoves;
    }

    public boolean drawAgreed()
    {
        if(gameStatus == GameStatus.ACTIVE)
        {
            gameStatus = GameStatus.DRAW;
            return true;
        }
        return false;
    }

    public boolean whiteResign()
    {
        if(gameStatus == GameStatus.ACTIVE)
        {
            gameStatus = GameStatus.BLACK_WINS;
            return true;
        }
        return false;
    }

    public boolean blackResign()
    {
        if(gameStatus == GameStatus.ACTIVE)
        {
            gameStatus = GameStatus.WHITE_WINS;
            return true;
        }
        return false;
    }

    // getters

    public boolean getIsWhiteChecked()
    {
        return isWhiteChecked;
    }

    public boolean getIsBlackChecked()
    {
        return isBlackChecked;
    }

    public Color getIsWhitesTurn()
    {
        return playerTurn;
    }

    public GameStatus getGameStatus()
    {
        return gameStatus;
    }

    public GameInfo getGameInfo()
    {
        return gameInfo;
    }

    public boolean needToPromote()
    {
        return pieceToPromote == null ? false : true;
    }

    public int getMovesWithoutTakeOrPawnMove()
    {
        return movesWithoutTakeOrPawnMove / 2;
    }

}