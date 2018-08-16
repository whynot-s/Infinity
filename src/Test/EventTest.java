package Test;

import Model.Event;
import Model.Member;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;


public class EventTest {

    @Test
    public void createEventTest(){
        Event event = new Event("2018-08-17 19:00:00", "2018-08-17 22:00:00", 4);
        ArrayList<Member> allMembers = new ArrayList<>();
        try {
            Member.loadMembers(allMembers, "");
            ArrayList<Member> newMembers = new ArrayList<>();
            newMembers.add(new Member("Keith", "Male"));
            newMembers.add(new Member("Kitty", "Female"));
            Member.createMembers(newMembers);
            allMembers.addAll(newMembers);
            String chosen = "(";
            for(int i = 0; i < allMembers.size(); i+=2) chosen += String.format("%d,", allMembers.get(i).getMemberId());
            chosen = chosen.substring(0, chosen.length() - 1) + ")";
            event.addMembers(chosen);
            event.createEvent();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}