package com.example.do_an_ttltweb.controller.adminPage2;

import com.example.do_an_ttltweb.model.Category;
import com.example.do_an_ttltweb.model.Product;
import com.example.do_an_ttltweb.model.ProductImage;
import com.example.do_an_ttltweb.services.CategoryService;
import com.example.do_an_ttltweb.services.ImageService;
import com.example.do_an_ttltweb.services.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminPage2Servlet", value = "/admin/products")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5L * 1024 * 1024,
        maxRequestSize = 20L * 1024 * 1024
)
public class AdminPage2Servlet  extends HttpServlet {
    private ProductService productService = new ProductService();
    private CategoryService categoryService = new CategoryService();
    private ImageService imageService = new ImageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filter = request.getParameter("filter");
        String pageStr = request.getParameter("page");

        int categoryId = 0;
        int page = 1;
        int pageSize = 25;

        if (filter != null && !filter.isEmpty()) {
            try {
                categoryId = Integer.parseInt(filter);
            } catch (NumberFormatException e) {
                categoryId = 0;
            }
        }

        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        List<Product> products = productService.getProductsAdmin(categoryId, page, pageSize);
        int totalPages = productService.getTotalPagesAdmin(categoryId, pageSize);

        Map<Integer, String[]> productImagesMap = new HashMap<>();
        for (Product p : products) {
            String[] urls = new String[3];
            for (ProductImage pi : imageService.getAllImageById(p.getId())) {
                int pos = pi.getPosition();
                if (pos >= 0 && pos < 3) urls[pos] = pi.getImage_url();
            }
            productImagesMap.put(p.getId(), urls);
        }

        request.setAttribute("products", products);
        request.setAttribute("productImagesMap", productImagesMap);
        request.setAttribute("currentFilter", categoryId);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        List<Category> categories = categoryService.getAllCategories();
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("/adminPage2.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) action = "";

        try {
            switch (action) {
                case "add_category":
                    handleAddCategory(request, response);
                    break;
                case "delete_list":
                    handleDeleteList(request, response);
                    break;
                case "delete_category":
                    handleDeleteCategory(request, response);
                    break;
                case "edit_product":
                    handleEditProduct(request, response);
                    break;
                case "add_product":
                default:
                    handleAddProduct(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            doGet(request, response);
        }
    }
    private void handleAddProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        int categoryId = Integer.parseInt(request.getParameter("category_id"));
        int weight = Integer.parseInt(request.getParameter("weight"));

        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setDescription(description);
        newProduct.setPrice(price);
        newProduct.setStock(stock);
        newProduct.setCategory_id(categoryId);
        newProduct.setWeight_grams(weight);
        newProduct.setSold(0);

        Map<Integer, Part> imageParts = readImageParts(request);
        String webappRealPath = getServletContext().getRealPath("");

        boolean isSuccess = productService.addProductWithFiles(newProduct, imageParts, webappRealPath);

        if (isSuccess) {
            request.getSession().setAttribute("success", "Thêm sản phẩm thành công!");
            response.sendRedirect(request.getContextPath() + "/admin/products");
        } else {
            request.setAttribute("error", "Thêm sản phẩm thất bại!");
            doGet(request, response);
        }
    }

    private Map<Integer, Part> readImageParts(HttpServletRequest request) throws ServletException, IOException {
        Map<Integer, Part> parts = new HashMap<>();
        Part main = request.getPart("main_image");
        Part sub1 = request.getPart("sub_image_1");
        Part sub2 = request.getPart("sub_image_2");
        if (main != null && main.getSize() > 0) parts.put(0, main);
        if (sub1 != null && sub1.getSize() > 0) parts.put(1, sub1);
        if (sub2 != null && sub2.getSize() > 0) parts.put(2, sub2);
        return parts;
    }

    private void handleAddCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String categoryName = request.getParameter("category_name");
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            categoryService.insertCategory(categoryName);
        }
        response.sendRedirect(request.getContextPath() + "/admin/products");
    }
    private void handleDeleteList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idsStr = request.getParameter("ids");

        if (idsStr != null && !idsStr.trim().isEmpty()) {
            String[] ids = idsStr.split(",");

            productService.deleteListProducts(ids);

            request.getSession().setAttribute("success", "Xóa sản phẩm thành công!");
        } else {
            request.getSession().setAttribute("error", "Vui lòng chọn sản phẩm để xóa!");
        }
        response.sendRedirect(request.getContextPath() + "/admin/products");
    }

    private void handleDeleteCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            categoryService.deleteCategory(id); // Gọi service để xóa
            request.getSession().setAttribute("success", "Xóa loại sản phẩm thành công!");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "ID loại sản phẩm không hợp lệ!");
        }
        response.sendRedirect(request.getContextPath() + "/admin/products");
    }
    private void handleEditProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            int categoryId = Integer.parseInt(request.getParameter("category_id"));
            int weight = Integer.parseInt(request.getParameter("weight"));
            String state = request.getParameter("state");

            Product p = new Product();
            p.setId(id);
            p.setName(name);
            p.setDescription(description);
            p.setPrice(price);
            p.setStock(stock);
            p.setCategory_id(categoryId);
            p.setWeight_grams(weight);
            p.setState(state);

            boolean isSuccess = productService.updateProduct(p);

            if (isSuccess) {
                Map<Integer, Part> imageParts = readImageParts(request);
                if (!imageParts.isEmpty()) {
                    String webappRealPath = getServletContext().getRealPath("");
                    productService.updateProductImages(id, imageParts, webappRealPath);
                }
                request.getSession().setAttribute("success", "Cập nhật sản phẩm thành công!");
            } else {
                request.getSession().setAttribute("error", "Cập nhật sản phẩm thất bại!");
            }
            System.out.println("STATE = [" + state + "]");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi hệ thống!");
        }
        response.sendRedirect(request.getContextPath() + "/admin/products");
    }
}
