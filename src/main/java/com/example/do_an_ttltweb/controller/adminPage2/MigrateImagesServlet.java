package com.example.do_an_ttltweb.controller.adminPage2;

import com.example.do_an_ttltweb.dao.ImageDao;
import com.example.do_an_ttltweb.helper.upload.FileUploadHelper;
import com.example.do_an_ttltweb.model.ProductImage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/admin/migrate-images")
public class MigrateImagesServlet extends HttpServlet {

    private final ImageDao imageDao = new ImageDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String basePath = req.getParameter("basePath");
        String realPath = (basePath != null && !basePath.isBlank())
                ? basePath
                : getServletContext().getRealPath("");
        List<ProductImage> all = imageDao.getAllImages();

        int total = all.size();
        int skipped = 0;
        int success = 0;
        int failed = 0;

        out.println("=== BẮT ĐẦU MIGRATE ẢNH SẢN PHẨM ===");
        out.println("Ảnh được lưu vào: " + realPath + FileUploadHelper.UPLOAD_SUBDIR);
        out.println("Tổng số ảnh trong DB: " + total);
        out.println();

        for (ProductImage img : all) {
            String url = img.getImage_url();
            if (url == null || url.isBlank()) {
                skipped++;
                continue;
            }
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                skipped++;
                continue;
            }

            try {
                String newUrl = FileUploadHelper.saveFromUrl(url, realPath);
                imageDao.updateImageUrl(img.getId(), newUrl);
                success++;
                out.println("OK   [id=" + img.getId() + "] " + url + " -> " + newUrl);
                out.flush();
            } catch (Exception e) {
                failed++;
                out.println("FAIL [id=" + img.getId() + "] " + url + " : " + e.getMessage());
                out.flush();
            }
        }

        out.println();
        out.println("=== KẾT QUẢ ===");
        out.println("Bỏ qua (đã local hoặc URL rỗng): " + skipped);
        out.println("Migrate thành công:               " + success);
        out.println("Thất bại:                         " + failed);
        out.println();
        out.println("Có thể chạy lại nhiều lần — các ảnh đã migrate sẽ tự động bỏ qua.");
    }
}