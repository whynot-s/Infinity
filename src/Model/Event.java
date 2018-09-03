package Model;


import DBtools.DButil;
import org.bson.Document;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

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

    public Map<String, Document> FirstSnapShot(){
        Map<String, Document> snapshot = new HashMap<>();
        Document sample = MemberAttrs.defaultDoc();
        //MemberQueue
        Document MemberQueue = new Document().append("EventId", EventId).append("Total", Players.size());
        Document members = new Document();
        for(int i = 0; i < Players.size(); i++) {
            sample.put(MemberAttrs.MEMBERID.getField(), Players.get(i).getMemberId());
            sample.put(MemberAttrs.NAME.getField(), Players.get(i).getName());
            sample.put(MemberAttrs.GENDER.getField(), Players.get(i).getGender());
            sample.put(MemberAttrs.POINT.getField(), Players.get(i).getPoint());
            sample.put(MemberAttrs.STATUS.getField(), 9);
            sample.put(MemberAttrs.RANK.getField(), i + 1);
            members.put(String.format("M%d", Players.get(i).getMemberId()), sample);
            sample = MemberAttrs.defaultDoc();
        }
        MemberQueue.put("Queue", members);
        snapshot.put("MemberQueue", MemberQueue);
        //Upcoming
        Document Upcoming = new Document().append("EventId", EventId);
        Document courts = new Document();
        for(int i = 0; i < CourtNum; i++){
            Document court = new Document();
            court.put("A1", sample);
            court.put("A2", sample);
            court.put("B1", sample);
            court.put("B2", sample);
            court.put("courtId", i + 1);
            courts.put(String.format("U%d", i + 1), court);
        }
        Upcoming.put("UpCourts", courts);
        snapshot.put("Upcoming", Upcoming);
        //Games
        Document Games = new Document().append("EventId", EventId);
        courts = new Document();
        for(int i = 0; i < CourtNum; i++){
            Document court = new Document();
            court.put("A1", sample);
            court.put("A2", sample);
            court.put("B1", sample);
            court.put("B2", sample);
            court.put("courtId", i + 1);
            courts.put(String.format("G%d", i + 1), court);
        }
        Games.put("OnCourts", courts);
        snapshot.put("Games", Games);
        return snapshot;
    }

    public static boolean verify(int eventId, String code){
        boolean result = false;
        Connection connection = DButil.getConnection();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(String.format("SELECT * FROM Event WHERE eventId=%d AND code=\'%s\'", eventId, code));
            if(resultSet.next()) if(!resultSet.next()) result = true;
        } catch (SQLException e) {
            result = false;
        }
        return result;
    }

    public int getEventId(){return  EventId;}

}
