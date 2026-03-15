package com.example.do_an_ttltweb.controller.adminPage8;

import com.example.do_an_ttltweb.services.PromotionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AdminPage8Servlet", urlPatterns = {"/admin/promotion"})
public class AdminPage8Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PromotionService.getInstance().autoUpdateStatus();
        request.setAttribute("listPromotions", PromotionService.getInstance().getAllPromotions());
        request.getRequestDispatcher("/adminPage8.jsp").forward(request, response);
    }
}