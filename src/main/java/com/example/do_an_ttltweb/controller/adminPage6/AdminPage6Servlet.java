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
        String pageStr = request.getParameter("page");
        int page = 1;
        int limit = 10;
        try {
            if (pageStr != null) {
                page = Integer.parseInt(pageStr);
                if (page < 1) page = 1;
            }
        } catch (NumberFormatException ignored) {}

        int totalReview = review.countReviews();
        int totalPages = (int) Math.ceil((double) totalReview / limit);
        if (totalPages == 0) totalPages = 1;
        if (page > totalPages) page = totalPages;

        int offset = (page - 1) * limit;

        List<ProductReview> listReview = review.getPaginated(limit, offset);

        request.setAttribute("listReview", listReview);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/adminPage6.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}