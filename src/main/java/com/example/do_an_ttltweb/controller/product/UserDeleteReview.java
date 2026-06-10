package com.example.do_an_ttltweb.controller.product;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.services.ReviewService;

import java.io.IOException;

@WebServlet(name = "UserDeleteReview", value = "/deleteReview")
public class UserDeleteReview extends HttpServlet {

    private ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String ridParam = request.getParameter("rid");
        String pidParam = request.getParameter("pid");

        if (ridParam == null || ridParam.isEmpty()) {
            System.out.println("rid is null!");
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        int rid = Integer.parseInt(ridParam);
        int pid = Integer.parseInt(pidParam);

        boolean result = reviewService.deleteReview(rid);
        if (result) {
            request.getSession().setAttribute("success", "Đã xóa đánh giá");
        } else {
            request.getSession().setAttribute("error", "Xóa đánh giá thất bại!");
        }

        response.sendRedirect(request.getContextPath() + "/product?pid=" + pid);
    }
}