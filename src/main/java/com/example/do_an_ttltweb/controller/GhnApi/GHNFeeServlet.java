package com.example.do_an_ttltweb.controller.GhnApi;

import com.example.do_an_ttltweb.services.GHNService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "GHNFeeServlet", value = "/api/ghn/fee")
public class GHNFeeServlet extends HttpServlet {

    private GHNService ghn = new GHNService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int districtId = Integer.parseInt(request.getParameter("district_id"));
        String wardCode = request.getParameter("ward_code");
        int weight = Integer.parseInt(request.getParameter("weight"));
        response.setContentType("application/json;charset=UTF-8");
        try {
            int fee = ghn.calculateFee(districtId, wardCode, weight);
            response.getWriter().write("{\"fee\":" + fee + "}");
        } catch (Exception e) {
            response.getWriter().write("{\"fee\":30000}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}