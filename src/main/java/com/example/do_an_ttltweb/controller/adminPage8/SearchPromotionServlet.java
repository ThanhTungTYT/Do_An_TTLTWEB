package com.example.do_an_ttltweb.controller.adminPage8;

import com.example.do_an_ttltweb.model.Promotion;
import com.example.do_an_ttltweb.services.PromotionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchPromotionServlet", urlPatterns = {"/admin/promotion/search"})
public class SearchPromotionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("search");
        List<Promotion> listPromotions;

        if (keyword != null && !keyword.trim().isEmpty()) {
            listPromotions = PromotionService.getInstance().searchPromotions(keyword.trim());
        } else {
            listPromotions = PromotionService.getInstance().getAllPromotions();
        }

        request.setAttribute("listPromotions", listPromotions);
        request.setAttribute("searchKeyword", keyword);
        request.setAttribute("currentPage", 1);
        request.setAttribute("totalPages", 1);

        request.getRequestDispatcher("/adminPage8.jsp").forward(request, response);
    }
}