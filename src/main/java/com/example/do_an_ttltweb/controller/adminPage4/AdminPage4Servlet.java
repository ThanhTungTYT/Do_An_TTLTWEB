package com.example.do_an_ttltweb.controller.adminPage4;

import com.example.do_an_ttltweb.dao.AuthDao;
import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.services.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminPage4Servlet", value = "/admin/users")
public class AdminPage4Servlet extends HttpServlet {

    private AccountService a = new AccountService();
    private AuthDao authDao = new AuthDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<User> listUsers = a.getAllUser();

        listUsers.forEach(u -> {
            List<String> perms = authDao.getPermissionsByUserId(u.getId());
            if (!perms.contains("shopping")) perms.add("shopping");
            u.setPermissions(perms);
        });

        request.setAttribute("listUsers", listUsers);
        request.setAttribute("listNew", a.getNewUser());
        request.setAttribute("allPermissions", authDao.getAllPermissions());

        request.getRequestDispatcher("/adminPage4.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}