package com.example.do_an_ttltweb.controller.adminPage4;

import com.example.do_an_ttltweb.dao.AuthDao;
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
    private AuthDao authDao = new AuthDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int uid = Integer.parseInt(request.getParameter("uid"));
        String[] permissionIds = request.getParameterValues("up_permissions");
        String role = request.getParameter("up_role");

        User u = new User();
        u.setRole(role);
        accountService.updateUser(uid, u);

        authDao.updateUserPermissions(uid, permissionIds);

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}