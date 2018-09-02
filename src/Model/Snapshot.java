package Model;

import DBtools.MongoDButil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.*;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class Snapshot {

    final private int EventId;
    final private String TimeStamp;

    public Snapshot(int eventId){
        EventId = eventId;
        TimeStamp = LocalDateTime.now(ZoneId.of("Europe/London")).toString();
    }

    public void initFirstSnapshot(Map<String, Document> documentMap){
        MongoDatabase mongoDatabase = MongoDButil.getDatabase();
        for(Map.Entry<String, Document> documentEntry : documentMap.entrySet())
            mongoDatabase.getCollection(documentEntry.getKey()).insertOne(documentEntry.getValue().append("TimeStamp", TimeStamp));
    }

    public JSONObject loadAll(){
        Map<String, Object> snapshot = new HashMap<>();
        MongoDatabase mongoDatabase = MongoDButil.getDatabase();
        snapshot.put("MemberQueue", queryLatest(mongoDatabase,"MemberQueue"));
        snapshot.put("Upcoming", queryLatest(mongoDatabase, "Upcoming"));
        snapshot.put("Games", queryLatest(mongoDatabase,"Games"));
        return new JSONObject(snapshot);
    }

    public JSONObject queryLatest(MongoDatabase mongoDatabase, String key){
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(key);
        FindIterable<Document> findIterable = mongoCollection.find(new BasicDBObject("EventId",EventId)).sort(new BasicDBObject("TimeStamp",-1)).limit(1);
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        if(mongoCursor.hasNext())
            return new JSONObject(mongoCursor.next().toJson());
        else
            return new JSONObject();
    }

    public void updateAll(String snapshot){
        JSONObject jsonObject = new JSONObject(snapshot);
        MongoDatabase mongoDatabase = MongoDButil.getDatabase();
        DBObject MemberQueue = (DBObject) JSON.parse(jsonObject.get("MemberQueue").toString());
        MemberQueue.put("TimeStamp", TimeStamp);
        MemberQueue.put("EventId", EventId);
        mongoDatabase.getCollection("MemberQueue", DBObject.class).insertOne(MemberQueue);
        DBObject Upcoming = (DBObject) JSON.parse(jsonObject.get("Upcoming").toString());
        Upcoming.put("TimeStamp", TimeStamp);
        Upcoming.put("EventId", EventId);
        mongoDatabase.getCollection("Upcoming", DBObject.class).insertOne(Upcoming);
        DBObject Games = (DBObject) JSON.parse(jsonObject.get("Games").toString());
        Games.put("TimeStamp", TimeStamp);
        Games.put("EventId", EventId);
        mongoDatabase.getCollection("Games", DBObject.class).insertOne(Games);
    }

}
