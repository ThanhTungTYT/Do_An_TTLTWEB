package com.example.do_an_ttltweb.controller.adminPage6;

import com.example.do_an_ttltweb.model.ProductReview;
import com.example.do_an_ttltweb.services.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "FilterReviewByTime", value = "/filter-review")
public class FilterReviewByTime extends HttpServlet {

    private ReviewService review = new ReviewService();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ProductReview> listReview = new ArrayList<>();

        String start = request.getParameter("start");
        String end = request.getParameter("end");

        if(start == null && end == null){
            listReview = review.getAllReview();
        }else {
            listReview = review.getReviewByTime(start, end);
        }

        request.setAttribute("listReview", listReview);

        request.getRequestDispatcher("adminPage6.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}