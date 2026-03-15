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

@WebServlet(name = "SearchReview", value = "/search-review")
public class SearchReview extends HttpServlet {

    private ReviewService review = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ProductReview> listReview = new ArrayList<>();

        String key = request.getParameter("key");

        if(key == null || key.equals("")){
            listReview = review.getAllReview();
        }else{
            listReview = review.getReviewByKey(key);
        }

        request.setAttribute("listReview", listReview);

        request.getRequestDispatcher("adminPage6.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}