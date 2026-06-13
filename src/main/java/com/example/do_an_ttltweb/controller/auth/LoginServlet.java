package com.example.do_an_ttltweb.controller.auth;

import com.example.do_an_ttltweb.dao.CartDao;
import com.example.do_an_ttltweb.helper.util.CaptchaUtil;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String captchaResponse = request.getParameter("g-recaptcha-response");
        if (!CaptchaUtil.verify(captchaResponse)) {
            request.setAttribute("error", "Vui lòng xác nhận bạn không phải robot!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        if (authService.isLocked(email)) {
            request.setAttribute("error", "Tài khoản bị tạm khóa do đăng nhập sai quá 5 lần. Vui lòng thử lại sau 1 tiếng.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        if(authService.isBan(email)){
            request.setAttribute("error", "Tài khoản của bạn đã bị cấm!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        User user = authService.login(email, password);

        if (user == null) {
            authService.recordFailed(email);

            int remaining = authService.getRemainingAttempts(email);

            if (remaining <= 0) {
                request.setAttribute("error", "Bạn đã nhập sai quá 5 lần. Tài khoản bị tạm khóa trong 1 tiếng.");
            } else {
                request.setAttribute("error", "Email hoặc mật khẩu không đúng! Bạn còn " + remaining + " lần thử.");
            }

            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            authService.clearFailed(email);
            authService.loadUserPermissions(user);

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            Cart cart = cartDao.loadCart(user.getId());
            session.setAttribute("cart", cart);

            session.setAttribute("success", "Đăng nhập thành công!");
            if (user.hasPermission("access_admin")) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
        }
    }
}