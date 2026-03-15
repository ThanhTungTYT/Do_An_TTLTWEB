package com.example.do_an_ttltweb.controller.adminPage4;

import com.example.ltwebnhom23.model.User;
import com.example.ltwebnhom23.services.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "DeleteUser", value = "/delete-user")
public class DeleteUser extends HttpServlet {

    private AccountService account = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int uid = Integer.parseInt(request.getParameter("uid"));
        User user = account.getAccountInfo(uid);

        if(user.getStatus().equals("active")){
            if(account.banUser(uid)){
                response.sendRedirect(request.getContextPath() + "/admin/users");
            }
        }else {
            if(account.unBanUser(uid)){
                response.sendRedirect(request.getContextPath() + "/admin/users");
            }
        }
    }
}