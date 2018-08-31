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

@WebServlet(name = "NewMembers", urlPatterns = "/NewMembers")
public class NewMenmbers extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        String[] names = request.getParameter("Names").split(",");
        String[] genders = request.getParameter("Genders").split(",");
        ArrayList<Member> newMembers = new ArrayList<>();
        for(int i = 0; i < names.length; i++)
            newMembers.add(new Member(names[i], i < genders.length ? genders[i] == "M" ? "Male" : "Female" : "Male"));
        try {
            Member.createMembers(newMembers);
            ArrayList<Member> allMembers = new ArrayList<>();
            Member.loadMembers(allMembers, "");
            List<JSONObject> members = new ArrayList<>();
            for(Member member : allMembers) members.add(member.toJSON());
            out.println(new JSONArray(members).toString());
        } catch (SQLException e) {
            out.println(e.toString());
        }
        out.flush();
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return;
    }
}
