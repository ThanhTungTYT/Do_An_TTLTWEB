package com.example.do_an_ttltweb.controller.adminPage6;

import com.example.do_an_ttltweb.model.ProductReview;
import com.example.do_an_ttltweb.services.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminPage6Servlet", value = "/admin/reviews")
public class AdminPage6Servlet extends HttpServlet {

    private ReviewService review = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ProductReview> listReview = review.getAllReview();

        request.setAttribute("listReview", listReview);

        request.getRequestDispatcher("/adminPage6.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}