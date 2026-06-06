package com.example.do_an_ttltweb.controller.GhnApi;

import com.example.do_an_ttltweb.services.GHNService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "GHNLeadTimeServlet ", value = "/api/ghn/leadtime")
public class GHNLeadTimeServlet extends HttpServlet {

    private GHNService ghn = new GHNService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int districtId = Integer.parseInt(request.getParameter("district_id"));
        String wardCode = request.getParameter("ward_code");
        int serviceId = 53321;

        response.setContentType("application/json;charset=UTF-8");
        try {
            String leadtime = ghn.getLeadTime(districtId, wardCode, serviceId);
            response.getWriter().write("{\"leadtime\":\"" + leadtime + "\"}");
        } catch (Exception e) {
            response.getWriter().write("{\"leadtime\":\"Không xác định\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}