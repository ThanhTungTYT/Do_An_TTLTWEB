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

@WebServlet(name = "AddUser", value = "/add-user")
public class AddUser extends HttpServlet {

    private AccountService account = new AccountService();
    private AuthDao authDao = new AuthDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = new User();
        user.setFull_name(request.getParameter("name"));
        user.setEmail(request.getParameter("email"));
        user.setPassword_hash(request.getParameter("pass"));
        user.setPhone(request.getParameter("phone"));
        user.setRole("customer");

        if (account.addUser(user)) {
            User newUser = authDao.findByEmail(user.getEmail());
            if (newUser != null) {
                String[] permissionIds = request.getParameterValues("permissions");
                authDao.updateUserPermissions(newUser.getId(), permissionIds);
            }
            response.sendRedirect(request.getContextPath() + "/admin/users");
        }
    }
}