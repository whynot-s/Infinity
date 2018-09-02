package Servlets;

import Model.Event;
import Model.Snapshot;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;

@WebServlet(name = "LoadEvent", urlPatterns = "/LoadEvent")
public class LoadEvent extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        int eventId = Integer.parseInt(request.getParameter("eventId"));
        String code = request.getParameter("code");
        boolean v = Event.verify(eventId, code);
        PrintWriter out = response.getWriter();
        if(!v) out.println(-1);
        else{
            Snapshot snapshot = new Snapshot(eventId);
            out.println(snapshot.loadAll().toString());
        }
        out.flush();
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
