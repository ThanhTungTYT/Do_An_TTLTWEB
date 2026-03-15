package com.example.do_an_ttltweb.controller.account;

import com.example.do_an_ttltweb.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "AccountHomeServlet", urlPatterns = {"/account"})
public class AccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User authUser = (User) session.getAttribute("user");

        if (authUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        request.getRequestDispatcher("/account.jsp").forward(request, response);
    }
}