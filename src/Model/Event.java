package Model;


import DBtools.DButil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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

    public void createEvent() throws SQLException {
        Connection connection = DButil.getConnection();
        Statement stmt = connection.createStatement();
        stmt.execute(String.format("INSERT INTO Event(startTime,endTime,courts) VALUES(\'%s\',\'%s\',%d)", StartTime, EndTime, CourtNum));
        ResultSet resultSet = stmt.executeQuery("SELECT LAST_INSERT_ID()");
        while(resultSet.next()) EventId = resultSet.getInt("LAST_INSERT_ID()");
        String EventPlayerSQL = "INSERT INTO EventPlayers(eventId,playerId) VALUES";
        for(Member member : Players) EventPlayerSQL += String.format("(%d,%d),", EventId, member.getMemberId());
        stmt.execute(EventPlayerSQL.substring(0, EventPlayerSQL.length() - 1));
        stmt.close();
    }

}
