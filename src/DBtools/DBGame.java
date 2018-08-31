package DBtools;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DBGame {

    public static void saveGame(int eventId, int courtId, int playerA1, int playerA2, int playerB1, int playerB2) throws SQLException {
        Connection conn = DButil.getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute(String.format("INSERT INTO EventGame(eventId,courtId,playerA1,playerA2,playerB1,playerB2,startTime) " +
                        "VALUES(%d,%d,%d,%d,%d,%d,\'%s\')",
                eventId, courtId, playerA1, playerA2, playerB1, playerB2,LocalDateTime.now(ZoneId.of("Europe/London")).toString()));
        stmt.close();
    }

    public static void finishGame(int eventId, int gameId, int scoreA, int scoreB) throws SQLException {
        Connection conn = DButil.getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute(String.format("UPDATE EventGame SET scoreA=%d, scoreB=%d, endTime=\'%s\' WHERE eventId=%d AND gameId=%d",
                scoreA, scoreB, LocalDateTime.now(ZoneId.of("Europe/London")).toString(), eventId, gameId));
        stmt.close();
    }

}
