package com.example.do_an_ttltweb.controller.product;

import com.example.ltwebnhom23.model.Product;
import com.example.ltwebnhom23.model.ProductImage;
import com.example.ltwebnhom23.model.ProductReview;
import com.example.ltwebnhom23.services.ImageService;
import com.example.ltwebnhom23.services.ProductService;
import com.example.ltwebnhom23.services.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "product", value = "/product")
public class DetailProductServlet extends HttpServlet {

    private ProductService productService = new ProductService();
    private ImageService imageService = new ImageService();
    private ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pid = request.getParameter("pid");
        if(pid == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        int productId = Integer.parseInt(pid);

        Product product = productService.getProduct(productId);
        request.setAttribute("product", product);

        if (product != null) {
            List<Product> relative = productService.getProductsByRelative(product.getCategory_id(), product.getName(), product.getId());
            request.setAttribute("relative", relative);

            List<ProductReview> review = reviewService.getReviewForProduct(productId);
            if(review.size()>0){
                int count = review.size();
            }
            int count = 1;
            int sum = 0;
            for(ProductReview r : review){
                sum += r.getRating();
            }
            double avg = sum/count;
            request.setAttribute("count", review.size());
            request.setAttribute("avg", avg);
            request.setAttribute("review", review);

            List<ProductImage> listImage = imageService.getAllImageById(productId);
            request.setAttribute("listImage", listImage);
        }

        request.getRequestDispatcher("product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}