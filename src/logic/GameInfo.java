package logic;

import java.time.LocalDateTime;
import java.util.ArrayList;

// TODO add optional data
public class GameInfo
{
    String event = "", site = "", result = "", whiteName = "", blackName = "", annotator;
    LocalDateTime dateTime, eventDate = null;
    int round = -1, whiteElo = -1, blackElo = -1, playCount = -1;
    Mode mode;

    ArrayList<MoveInfo> moves;

    GameStatus gameStatus;

    public GameInfo()
    {
        super();

        dateTime = LocalDateTime.now();
        mode = null;
        moves = new ArrayList<>();
        gameStatus = GameStatus.ACTIVE;
    }

    public GameInfo(String event, String site, String result, String whiteName, String blackName)
    {

        this();

        this.event = event;
        this.site = site;
        this.result = result;
        this.whiteName = whiteName;
        this.blackName = blackName;

    }

    public void addMove(Position position, Position lastPosition, char notationLetter, char lastNotationLetter,
            boolean isTake, boolean isCheck, boolean isCheckMate, boolean isShortCastling, boolean isLongCastling)
    {
        moves.add(new MoveInfo(position, lastPosition, notationLetter, lastNotationLetter, isTake, isCheck, isCheckMate,
                isShortCastling, isLongCastling));
    }

    @Override
    public String toString()
    {
        String dateString = "?";
        if(dateTime != null)
        {
            dateString = String.format("%d.%02d.%02d", dateTime.getYear(), dateTime.getMonthValue(),
                    dateTime.getDayOfMonth());

        }

        StringBuilder moveString = new StringBuilder();
        for(int i = 0; i < moves.size(); i++)
        {
            if(i % 2 == 0)
            {
                moveString.append((i / 2 + 1) + ". ");
            }
            moveString.append(moves.get(i).toString() + " ");
        }

        String status;
        switch(gameStatus)
        {
            case DRAW:
                status = "1/2-1/2";
                break;
            case WHITE_WINS:
                status = "1-0";
                break;
            case BLACK_WINS:
                status = "0-1";
            default:
                status = "";
        }

        return String.format(
                "[Event \"%s\"]%n[Site \"%s\"]%n[Date \"%s\"]%n[Round \"%s\"]%n[White \"%s\"]%n[Black \"%s\"]%n[Result \"%s\"]%n%n%s%s",
                event.isBlank() ? "?" : event, site.isBlank() ? "?" : site, dateString,
                round < 0 ? "?" : String.valueOf(round), whiteName.isBlank() ? "?" : whiteName,
                blackName.isBlank() ? "?" : blackName, result.isBlank() ? "?" : result,
                moves.isEmpty() ? "" : moveString.toString(), status);
    }

    public void end(GameStatus gameStatus)
    {
        this.gameStatus = gameStatus;
    }

}
