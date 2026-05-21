package com.example.do_an_ttltweb.controller.GhnApi;

import com.example.do_an_ttltweb.services.GHNService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "GHNTrackingServlet", value = "/api/ghn/tracking")
public class GHNTrackingServlet extends HttpServlet {

    private GHNService ghn = new GHNService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        response.setContentType("application/json;charset=UTF-8");
        if (code == null || code.isEmpty()) {
            response.getWriter().write("{\"error\":\"missing code\"}");
            return;
        }
        try {
            response.getWriter().write(ghn.getOrderDetail(code));
        } catch (Exception e) {
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}