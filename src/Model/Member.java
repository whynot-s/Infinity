package Model;

import DBtools.DButil;

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
        if(!condition.equals("")) condition = " WHERE playerId in " + condition;
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

}