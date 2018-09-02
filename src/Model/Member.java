package Model;

import DBtools.DButil;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Member {

    private int MemberId;
    private String Name;
    private String Gender;
    private Double Point;

    public Member(String name, String gender) {
        Name = name;
        Gender = gender;
        Point = 0.0;
    }

    public Member(int memberId, String name, String gender, Double point) {
        MemberId = memberId;
        Name = name;
        Gender = gender;
        Point = point;
    }

    public static void createMembers(ArrayList<Member> members) throws SQLException {
        Connection conn = DButil.getConnection();
        Statement stmt = conn.createStatement();
        String insertSQL = "INSERT INTO Member(name,gender) VALUES(\'%s\',\'%s\')";
        for (Member member : members) {
            stmt.execute(String.format(insertSQL, member.Name, member.Gender));
            ResultSet resultSet = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            while(resultSet.next()) member.MemberId = resultSet.getInt("LAST_INSERT_ID()");
        }
        stmt.close();
    }

    public static void loadMembers(ArrayList<Member> members, String condition) throws SQLException {
        Connection conn = DButil.getConnection();
        Statement stmt = conn.createStatement();
        if(condition.startsWith("(")) condition = " WHERE playerId in " + condition;
        else if(!condition.equals("")) condition = String.format(" WHERE UPPER(name) LIKE \'%%%s%%\'", condition.toUpperCase());
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM Member" + condition);
        while (resultSet.next())
            members.add(new Member(
                    resultSet.getInt("playerId") ,
                    resultSet.getString("Name"),
                    resultSet.getString("gender"),
                    resultSet.getDouble("point")));
        stmt.close();
    }

    public int getMemberId(){
        return MemberId;
    }

    public JSONObject toJSON(){
        JSONObject member = new JSONObject();
        member.put("MemberId", MemberId);
        member.put("Name", Name);
        member.put("Gender", Gender);
        member.put("Point", Point);
        return member;
    }

}