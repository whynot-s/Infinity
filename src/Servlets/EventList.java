package Servlets;


import DBtools.DBEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "EventList", urlPatterns = "/EventList")
public class EventList extends javax.servlet.http.HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        DBEvent dbEvent = new DBEvent();
        List<Map<String, Object>> readyEvents = new ArrayList<>();
        List<Map<String, Object>> finishEvents = new ArrayList<>();
        dbEvent.getEventList("READY", readyEvents);
        dbEvent.getEventList("FINISHED", finishEvents);
        dbEvent.closeConn();
        JSONObject events = new JSONObject();
        events.put("READY", new JSONArray(readyEvents));
        events.put("FINISHED", new JSONArray(finishEvents));
        out.println(events.toString());
        out.flush();
        out.close();
    }
}
