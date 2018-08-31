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

@WebServlet(name = "FinishGame", urlPatterns = "/FinishGame")
public class FinishGame extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            DBGame.finishGame(Integer.parseInt(request.getParameter("eventId")),
                              Integer.parseInt(request.getParameter("gameId")),
                              Integer.parseInt(request.getParameter("scoreA")),
                              Integer.parseInt(request.getParameter("scoreB")));
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
