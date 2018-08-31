package DBtools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBEvent {

    private Connection conn;
    final private String[] columns = {"eventId","startTime","endTime","courts","code","status"};

    public DBEvent(){
        conn = DButil.getConnection();
    }

    public DBEvent(Connection connection){
        conn = connection;
    }

    public Connection getConn() {
        return conn;
    }

    public void closeConn(){
        try {
            conn.close();
        } catch (SQLException e) {
//            e.printStackTrace();
        }
    }

    public int getEventList(String status, List<Map<String, Object>> Result){
        int r = 0;
        try {
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(String.format("SELECT %s,%s,%s,%s FROM Event WHERE %s=\'%s\'",
                    columns[0], columns[1], columns[2], columns[3], columns[5], status));
            while(resultSet.next()){
                Map<String, Object> tmp = new HashMap<>();
                tmp.put(columns[0], resultSet.getInt(columns[0]));
                tmp.put(columns[1], resultSet.getString(columns[1]));
                tmp.put(columns[2], resultSet.getString(columns[2]));
                tmp.put(columns[3], resultSet.getInt(columns[3]));
                Result.add(tmp);
            }
        } catch (SQLException e) {
            r = -1;
        }
        return r;
    }
}
