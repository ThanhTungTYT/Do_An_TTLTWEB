package com.example.do_an_ttltweb.controller.auth;

import com.example.do_an_ttltweb.dao.CartDao;
import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.model.cart.Cart;
import com.example.do_an_ttltweb.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "login", value = "/login")
public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthService();
    private final CartDao cartDao = new CartDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = authService.login(email, password);

        if (user == null) {
            request.setAttribute("error", "Sai email hoặc mật khẩu!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            authService.loadUserPermissions(user);

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            Cart cart = cartDao.loadCart(user.getId());
            session.setAttribute("cart", cart);

            if (user.hasPermission("access_admin")) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
        }
    }
}