package com.example.do_an_ttltweb.controller.adminPage6;

import com.example.do_an_ttltweb.services.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "DeleteReview", value = "/delete-review")
public class DeleteReview extends HttpServlet {

    private ReviewService review = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int rid = Integer.parseInt(request.getParameter("rid"));

        if(review.deleteReview(rid)){
            request.getSession().setAttribute("success", "Xóa đánh giá thành công!");
        } else {
            request.getSession().setAttribute("error", "Xóa đánh giá thất bại!");
        }
        response.sendRedirect(request.getContextPath() + "/admin/reviews");
    }
}