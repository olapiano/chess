package logic.exceptions;

import logic.GameStatus;

public class GameNotActive extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public GameNotActive(GameStatus status)
    {
        super(String.format("Game is not active. Status: %s",
                status == GameStatus.DRAW ? "Draw"
                        : status == GameStatus.WHITE_WINS ? "White wins"
                                : status == GameStatus.BLACK_WINS ? "Black wins" : "Inactive"));
    }
}
