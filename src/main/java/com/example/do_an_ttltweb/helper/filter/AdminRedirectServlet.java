package com.example.do_an_ttltweb.helper.filter;
import com.example.do_an_ttltweb.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "AdminRedirectServlet", urlPatterns = {"/admin/redirect"})
public class AdminRedirectServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String contextPath = request.getContextPath();

        if ("admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(contextPath + "/admin/dashboard");
            return;
        }

        if (user.hasPermission("view_dashboard")) {
            response.sendRedirect(contextPath + "/admin/dashboard");
            return;
        }
        if (user.hasPermission("manage_product")) {
            response.sendRedirect(contextPath + "/admin/products");
            return;
        }
        if (user.hasPermission("manage_order")) {
            response.sendRedirect(contextPath + "/admin/orders");
            return;
        }
        if (user.hasPermission("manage_user")) {
            response.sendRedirect(contextPath + "/admin/users");
            return;
        }
        if (user.hasPermission("manage_contact")) {
            response.sendRedirect(contextPath + "/admin/contact");
            return;
        }
        if (user.hasPermission("manage_banner")) {
            response.sendRedirect(contextPath + "/admin/banner");
            return;
        }
        if (user.hasPermission("manage_review")) {
            response.sendRedirect(contextPath + "/admin/reviews");
            return;
        }
        if (user.hasPermission("manage_promotion")) {
            response.sendRedirect(contextPath + "/admin/promotion");
            return;
        }

        // Không có quyền quản trị nào -> không tồn tại trang /403, đưa về trang chủ thay vì 404
        response.sendRedirect(contextPath + "/");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
