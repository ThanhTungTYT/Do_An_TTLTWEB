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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SearchUser", value = "/search-user")
public class SearchUser extends HttpServlet {

        private AccountService account = new AccountService();
        private AuthDao authDao = new AuthDao();

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            String key = request.getParameter("keyword");
            List<User> listUsers;

            if (key == null || key.equals("")) {
                listUsers = account.getAllUser();
            } else {
                listUsers = account.getUserByKeyword(key);
            }

            listUsers.forEach(u -> {
                List<String> perms = authDao.getPermissionsByUserId(u.getId());
                if (!perms.contains("shopping")) perms.add("shopping");
                u.setPermissions(perms);
            });

            request.setAttribute("listUsers", listUsers);
            request.setAttribute("listNew", account.getNewUser());
            request.setAttribute("allPermissions", authDao.getAllPermissions());

            request.getRequestDispatcher("adminPage4.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}