package com.example.do_an_ttltweb.controller.account;

import com.example.do_an_ttltweb.helper.base.Validation;
import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.services.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "ChangePasswordServlet", value = "/change-password")
public class ChangePasswordServlet extends HttpServlet {

    private AuthService authService = new AuthService();
    private Validation validation = new Validation();
    private static final int LENGTH = 8;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Kiểm tra session xem user đã đăng nhập chưa
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 2. Lấy dữ liệu từ form
        String oldPass = request.getParameter("old_pass");
        String newPass = request.getParameter("new_pass");
        String confirmPass = request.getParameter("confirm_pass");

        // 3. Validate cơ bản
        if(!validation.containChar(newPass)){
            request.setAttribute("error", "Mật khẩu phải chứa ít nhất 1 kí tự đặc biệt, chữ in hoa và số!");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
            return;
        }

        if (newPass == null || !validation.rePass(newPass, confirmPass)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
            return;
        }

        if (!validation.passLength(newPass, LENGTH)) {
            request.setAttribute("error", "Mật khẩu mới phải có ít nhất" + LENGTH + "ký tự!");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
            return;
        }

        if (oldPass != null && oldPass.equals(newPass)) {
            request.setAttribute("error", "Mật khẩu mới không được trùng với mật khẩu hiện tại!");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
            return;
        }

        boolean isChanged = authService.changePassword(user.getId(), oldPass, newPass);

        if (isChanged) {
            request.setAttribute("success", "Đổi mật khẩu thành công!");
        } else {
            request.setAttribute("error", "Mật khẩu hiện tại không đúng. Vui lòng thử lại.");
        }

        // 5. Trả về kết quả
        request.getRequestDispatcher("changePassword.jsp").forward(request, response);
    }
}