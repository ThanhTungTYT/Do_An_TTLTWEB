package com.example.do_an_ttltweb.controller.GhnApi;

import com.example.do_an_ttltweb.services.GHNService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "GHNWardsServlet", value = "/api/ghn/wards")
public class GHNWardsServlet extends HttpServlet {

    private GHNService ghn = new GHNService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int districtId = Integer.parseInt(request.getParameter("district_id"));
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(ghn.getWards(districtId).toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}