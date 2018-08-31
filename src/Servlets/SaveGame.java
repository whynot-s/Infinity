package Servlets;

import DBtools.DBGame;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "SaveGame", urlPatterns = "/SaveGame")
public class SaveGame extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            DBGame.saveGame(Integer.parseInt(request.getParameter("eventId")),
                    Integer.parseInt(request.getParameter("courtId")),
                    Integer.parseInt(request.getParameter("playerA1")),
                    Integer.parseInt(request.getParameter("playerA2")),
                    Integer.parseInt(request.getParameter("playerB1")),
                    Integer.parseInt(request.getParameter("playerB2")));
        } catch (SQLException e) {
            out.println(e.toString());
        }
        out.flush();
        out.println();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return;
    }
}
