package com.example.do_an_ttltweb.controller.GhnApi;
import com.example.do_an_ttltweb.services.GHNService;
import com.google.gson.JsonArray;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "GHNProvincesServlet", value = "/api/ghn/provinces")
public class GHNProvincesServlet extends HttpServlet {

    private GHNService ghn = new GHNService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        try {
            JsonArray provinces = ghn.getProvinces();
            response.getWriter().write(provinces.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}