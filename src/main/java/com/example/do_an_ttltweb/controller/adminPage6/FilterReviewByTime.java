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

        boolean hasStart = start != null && !start.isEmpty();
        boolean hasEnd = end != null && !end.isEmpty();

        if (!hasStart && !hasEnd) {
            listReview = review.getAllReview();
        } else {
            listReview = review.getReviewByTime(
                    hasStart ? start : null,
                    hasEnd ? end : null
            );
        }

        request.setAttribute("listReview", listReview);
        request.setAttribute("startDate", start);
        request.setAttribute("endDate", end);
        request.getRequestDispatcher("adminPage6.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}