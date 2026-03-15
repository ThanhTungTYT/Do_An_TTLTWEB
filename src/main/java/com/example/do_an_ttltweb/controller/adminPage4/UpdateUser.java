package com.example.do_an_ttltweb.controller.adminPage4;

import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.services.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "UpdateUser", value = "/update-user")
public class UpdateUser extends HttpServlet {

    private AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        int uid = Integer.parseInt(request.getParameter("uid"));

        User u = new User();
        u.setRole(request.getParameter("up_role"));

        if(accountService.updateUser(uid, u)){
            response.sendRedirect(request.getContextPath() + "/admin/users");
        }

    }
}