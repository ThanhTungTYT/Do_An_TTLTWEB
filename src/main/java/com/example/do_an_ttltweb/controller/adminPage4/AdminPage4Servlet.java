package com.example.do_an_ttltweb.controller.adminPage4;

import com.example.do_an_ttltweb.dao.AuthDao;
import com.example.do_an_ttltweb.model.Banner;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageStr = request.getParameter("page");
        int page = 1;
        int limit = 25;
        try {
            if (pageStr != null) {
                page = Integer.parseInt(pageStr);
                if (page < 1) page = 1;
            }
        } catch (NumberFormatException ignored) {}

        int totalUsers = a.countUsers();
        int totalPages = (int) Math.ceil((double) totalUsers / limit);
        if (totalPages == 0) totalPages = 1;

        if (page > totalPages) page = totalPages;

        int offset = (page - 1) * limit;
        List<User> listUsers = a.getUsersPaginated(limit, offset);

        listUsers.forEach(u -> {
            List<String> perms = authDao.getPermissionsByUserId(u.getId());
            if (!perms.contains("shopping")) perms.add("shopping");
            u.setPermissions(perms);
        });

        request.setAttribute("listUsers", listUsers);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("listNew", a.getNewUser());
        request.setAttribute("allPermissions", authDao.getAllPermissions());

        request.getRequestDispatcher("/adminPage4.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}