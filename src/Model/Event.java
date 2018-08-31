package Model;


import DBtools.DButil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

public class Event {

    private int EventId;
    private String StartTime;
    private String EndTime;
    private int CourtNum;
    private ArrayList<Member> Players;

    public Event(String startTime, String endTime, int courtNum){
        StartTime = startTime;
        EndTime = endTime;
        CourtNum = courtNum;
        Players = new ArrayList<>();
    }

    public void addMembers(String members) throws SQLException {
        Member.loadMembers(Players, members);
    }

    public String createEvent() throws SQLException {
        String code = "";
        for(int i = 0; i < 6; i++){
            int tmp = new Random().nextInt(36);
            if(tmp <= 9) code += String.format("%d", tmp);
            else code += String.format("%c", tmp + 87);
        }
        Connection connection = DButil.getConnection();
        Statement stmt = connection.createStatement();
        stmt.execute(String.format("INSERT INTO Event(startTime,endTime,courts,code) VALUES(\'%s\',\'%s\',%d,\'%s\')", StartTime, EndTime, CourtNum, code));
        ResultSet resultSet = stmt.executeQuery("SELECT LAST_INSERT_ID()");
        while(resultSet.next()) EventId = resultSet.getInt("LAST_INSERT_ID()");
        String EventPlayerSQL = "INSERT INTO EventPlayers(eventId,playerId) VALUES";
        for(Member member : Players) EventPlayerSQL += String.format("(%d,%d),", EventId, member.getMemberId());
        stmt.execute(EventPlayerSQL.substring(0, EventPlayerSQL.length() - 1));
        stmt.close();
        return code;
    }

}
