package com.example.do_an_ttltweb.controller.cart;

import com.example.ltwebnhom23.cart.Cart;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "UpdateCart", value = "/update-cart")
public class UpdateCart extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int pid = Integer.parseInt(request.getParameter("pid"));
            int quantity = Integer.parseInt(request.getParameter("q"));

            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");

            if (cart != null) {
                cart.updateQuantity(pid, quantity);
                session.setAttribute("cart", cart);
            }
        } catch (NumberFormatException e) { }

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}