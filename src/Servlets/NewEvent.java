package Servlets;

import Model.Event;
import Model.Snapshot;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "NewEvent", urlPatterns = "/NewEvent")
public class NewEvent extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        int courtNum = Integer.parseInt(request.getParameter("courts"));
        String chosen = request.getParameter("members");
        Event event = new Event(startTime, endTime, courtNum);
        PrintWriter out = response.getWriter();
        try {
            event.addMembers(chosen);
            String code = event.createEvent();
            out.println(code);
        } catch (SQLException e) {
            e.printStackTrace(out);
        }
        out.flush();
        out.close();
        new Snapshot(event.getEventId()).initFirstSnapshot(event.FirstSnapShot());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return;
    }
}
