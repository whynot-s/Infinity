package Test;

import DBtools.MongoDButil;
import Model.Event;
import Model.Member;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.Document;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
            String code = event.createEvent();
            System.out.println(code);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void timeTest(){
        System.out.println(LocalDateTime.now(ZoneId.of("Europe/London")).toString());
    }

    @Test
    public void MongoDBTest(){
        MongoDatabase mongoDatabase = MongoDButil.getDatabase();
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("MemberQueue");
        FindIterable<Document> findIterable = mongoCollection.find(new BasicDBObject("EventId",1)).sort(new BasicDBObject("TimeStamp",1)).limit(1);
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        while(mongoCursor.hasNext())
            System.out.println(mongoCursor.next());
    }

}