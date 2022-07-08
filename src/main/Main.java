package main;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import logic.Board;
import logic.Color;
import logic.PieceName;
import logic.Position;
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

/*
 * TODO
 * 
 * schackmatt: Om schack och kontrollera alla möjliga drag och se om det finns
 * något drag som kan ta sig ur schack
 * 
 * en passant???
 * 
 * rockad: flytta kungen två steg, kolla schack på alla tre rutor. hasMoved ==
 * false?
 * 
 * Genomtänkt API
 * 
 * read PGN
 * 
 * Om två hästar kan gå till samma ruta måste man specificera vilken
 * 
 */

public class Main
{
    public static void main(String[] args) throws WrongTurnException, NoPieceOnPositionException,
            PositionNotValidException, CantMoveToSamePositionException, TryingToTakeTheKingException,
            PotisionOccupiedBySameColorException, RouteBlockedException, IllegalMoveException, IsCheckedException,
            GameNotActive, NeedToPromoteExcpetion, IOException
    {
        Board board = new Board();

        board.setUpStartPosition();

        System.out.println(board.getGameInfo().toString());

        Scanner keyboard = new Scanner(System.in);
        int menuChoice = -1;

        // board.printAllPossibleMoves();

        board.move(Position.e2, Position.e4);
        board.move(Position.d7, Position.d5);
        board.move(Position.e4, Position.d5);
        board.move(Position.e7, Position.e6);
        board.move(Position.f1, Position.e2);
        board.move(Position.d8, Position.d7);
        board.move(Position.e2, Position.f1);
        board.move(Position.d7, Position.d8);
        board.move(Position.f1, Position.e2);
        board.move(Position.d8, Position.d7);
        board.move(Position.e2, Position.f1);
        board.move(Position.d7, Position.d8);
        board.move(Position.f1, Position.e2);
        board.move(Position.d8, Position.d7);
        board.move(Position.e2, Position.f1);
        board.move(Position.d7, Position.d8);

        board.move(Position.a2, Position.a4);
        board.move(Position.f7, Position.f5);
        board.move(Position.f1, Position.b5);
        board.move(Position.c7, Position.c6);
        board.move(Position.d5, Position.d6);
        board.move(Position.a7, Position.a6);
        board.move(Position.d6, Position.d7);
        board.move(Position.e8, Position.e7);

        try
        {
            board.move(Position.d7, Position.c8);
        }
        catch(Exception e)
        {
            System.out.println("Promote");
        }

        board.printPiecesList();
        board.printPositionOfPieces();

        while(menuChoice != 0)
        {

            if(board.needToPromote())
            {
                System.out.print("Promote pawn (q, r, b, n): ");
                try
                {
                    switch(keyboard.next().substring(0, 1))
                    {
                        case "q":
                            board.promote(PieceName.QUEEN);
                            break;
                        case "r":
                            board.promote(PieceName.ROOK);
                            break;
                        case "b":
                            board.promote(PieceName.BISHOP);
                            break;
                        case "n":
                            board.promote(PieceName.KNIGHT);
                            break;
                        default:
                            System.out.println("Wrong input");

                    }
                }
                catch(NoPieceToPromoteException | IllegalPromotion e)
                {
                    e.printStackTrace();
                }
            }
            board.printPositionOfPieces();
            if(board.getIsWhiteChecked())
            {
                System.out.println("White is checked!");
            }
            if(board.getIsBlackChecked())
            {
                System.out.println("Black is checked!");
            }
            System.out.println((board.getIsWhitesTurn() == Color.WHITE ? "White " : "Black ") + "to move");
            System.out.print("Insert move from position: ");
            String fromString = keyboard.next();
            System.out.print("Insert move to position: ");
            String toString = keyboard.next();

            Position from = searchPosition(fromString);
            Position to = searchPosition(toString);

            if(from == null || to == null)
            {
                System.out.println("Position not valid!");
            }
            else
            {
                try
                {
                    board.move(from, to);
                }
                catch(WrongTurnException | NoPieceOnPositionException | PositionNotValidException
                        | CantMoveToSamePositionException | TryingToTakeTheKingException
                        | PotisionOccupiedBySameColorException | RouteBlockedException | IllegalMoveException
                        | IsCheckedException | GameNotActive | NeedToPromoteExcpetion e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            System.out.print("Continue? (y/n) ");
            if(!keyboard.next().startsWith("y"))
            {
                menuChoice = 0;
            }
            System.out.println(board.getGameInfo().toString());
        }
        board.printPositionOfPieces();
        System.out.println("*********************************");
        board.printAllSavedPositions();
        board.savePGNToFile(new File("test.pgn"));
        board.readPGNFromFile(new File("test.pgn"));
        keyboard.close();
    }

    public static Position searchPosition(String s)
    {
        for(Position p : Position.values())
        {
            if(p.toString().equals(s))
            {
                return p;
            }
        }
        return null;
    }
}
