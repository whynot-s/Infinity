package DBtools;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDButil {

    public static MongoDatabase getDatabase(){
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("Infinity");
        return mongoDatabase;
    }

}
