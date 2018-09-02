package Servlets;

import Model.Member;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SearchMember", urlPatterns = "/SearchMember")
public class SearchMember extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        String name = request.getParameter("name");
        if(name == null) return;
        PrintWriter out = response.getWriter();
        ArrayList<Member> allMembers = new ArrayList<>();
        try {
            Member.loadMembers(allMembers, name);
            List<JSONObject> members = new ArrayList<>();
            for(Member member : allMembers) members.add(member.toJSON());
            out.println(new JSONArray(members).toString());
        } catch (SQLException e) {
            out.println(e.toString());
        }
        out.flush();
        out.close();
    }
}
