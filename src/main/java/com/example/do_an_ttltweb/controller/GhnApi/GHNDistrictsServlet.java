package com.example.do_an_ttltweb.controller.GhnApi;

import com.example.do_an_ttltweb.services.GHNService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "GHNDistrictsServlet", value = "/api/ghn/districts")
public class GHNDistrictsServlet extends HttpServlet {

    private GHNService ghn = new GHNService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int provinceId = Integer.parseInt(request.getParameter("province_id"));
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(ghn.getDistricts(provinceId).toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}