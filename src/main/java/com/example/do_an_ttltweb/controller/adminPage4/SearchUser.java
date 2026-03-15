package com.example.do_an_ttltweb.controller.adminPage4;

import com.example.ltwebnhom23.model.User;
import com.example.ltwebnhom23.services.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SearchUser", value = "/search-user")
public class SearchUser extends HttpServlet {

    private AccountService account = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> listUsers = new ArrayList<>();
        List<User> newUsers = account.getNewUser();

        String key = request.getParameter("keyword");
        request.setAttribute("listNew", newUsers);

        if(key.equals("") || key == null){
            listUsers = account.getAllUser();
        }else {
            listUsers = account.getUserByKeyword(key);
        }

        request.setAttribute("listUsers", listUsers);

        request.getRequestDispatcher("adminPage4.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}