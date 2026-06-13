package com.example.do_an_ttltweb.services;

import com.example.do_an_ttltweb.dao.ImageDao;
import com.example.do_an_ttltweb.dao.ProductDao;
import com.example.do_an_ttltweb.helper.upload.FileUploadHelper;
import com.example.do_an_ttltweb.model.Product;
import com.example.do_an_ttltweb.model.ProductImage;
import jakarta.servlet.http.Part;

import java.util.List;
import java.util.Map;

public class ProductService {
    private ProductDao productDao = new ProductDao();
    private ImageDao imageDao = new ImageDao();

    public List<Product> getProductsBySold(){
        return productDao.getProductsBySold();
    }

    public List<Product> getAllProduct(){
        return productDao.getAllProduct();
    }

    public List<Product> getProductForCategory(int cid){
        return productDao.getProductForCategory(cid);
    }

    public Product getProduct(int pid){
        return productDao.getProductById(pid);
    }

    public List<Product> getProductsByRelative(int cid, String name, int pid){
        return productDao.getProductsByRelative(cid, name, pid);
    }

    public List<Product> getProductsForCatalog(int cid, String sort, int page, double minPrice, double maxPrice) {
        if (sort == null) sort = "default";
        int offset = (page - 1) * 27;
        return productDao.getFilteredProducts(cid, sort, offset, minPrice, maxPrice);
    }

    public int getTotalPages(int cid, double minPrice, double maxPrice) {
        int totalProducts = productDao.countProducts(cid, minPrice, maxPrice);
        return (int) Math.ceil((double) totalProducts / 27);
    }
    // ---------------------------

    public boolean addProductWithUrls(Product product, String[] imageUrls) {
        try {
            int newProductId = productDao.insertProduct(product);
            if (newProductId > 0) {
                if (imageUrls != null && imageUrls.length > 0) {
                    int position = 0;
                    for (String url : imageUrls) {
                        if (url != null && !url.trim().isEmpty()) {
                            productDao.insertProductImage(newProductId, url.trim(), position);
                            position++;
                        }
                    }
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean addProductWithFiles(Product product, Map<Integer, Part> filesByPosition, String webappRealPath) {
        try {
            int newProductId = productDao.insertProduct(product);
            if (newProductId <= 0) return false;

            for (Map.Entry<Integer, Part> entry : filesByPosition.entrySet()) {
                Part part = entry.getValue();
                if (!FileUploadHelper.isValid(part)) continue;

                String url = FileUploadHelper.save(part, webappRealPath);
                productDao.insertProductImage(newProductId, url, entry.getKey());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProduct(Product p) {
        return productDao.updateProduct(p);
    }

    public void updateProductImages(int productId, Map<Integer, Part> filesByPosition, String webappRealPath) {
        for (Map.Entry<Integer, Part> entry : filesByPosition.entrySet()) {
            int position = entry.getKey();
            Part part = entry.getValue();
            if (!FileUploadHelper.isValid(part)) continue;

            try {
                String newUrl = FileUploadHelper.save(part, webappRealPath);
                ProductImage existing = imageDao.getImageByPosition(productId, position);
                if (existing != null) {
                    FileUploadHelper.delete(existing.getImage_url(), webappRealPath);
                    imageDao.updateImageUrl(existing.getId(), newUrl);
                } else {
                    productDao.insertProductImage(productId, newUrl, position);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteListProducts(String[] ids) {
        if (ids == null || ids.length == 0) return;

        for (String idStr : ids) {
            try {
                int id = Integer.parseInt(idStr.trim());
                productDao.softDeleteProduct(id);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
    public List<Product> searchProducts(String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return productDao.searchProductsPaginated(keyword, pageSize, offset);
    }

    public int getTotalPagesSearch(String keyword, int pageSize) {
        int totalProducts = productDao.countSearchProducts(keyword);
        if (totalProducts == 0) return 1;
        return (int) Math.ceil((double) totalProducts / pageSize);
    }

    public List<Product> getProductsAdmin(int categoryId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return productDao.getProductsPaginatedForAdmin(categoryId, pageSize, offset);
    }

    public int getTotalPagesAdmin(int categoryId, int pageSize) {
        int totalProducts = productDao.countProductsForAdmin(categoryId);
        if (totalProducts == 0) return 1;
        return (int) Math.ceil((double) totalProducts / pageSize);
    }

    public List<Product> getProductByKey(String key){
        return productDao.getProductByKey(key);
    }
    public List<Product> searchProductsByPrice(String keyword, int cid, String sort, int page, int pageSize, double minPrice, double maxPrice) {
        int offset = (page - 1) * pageSize;
        if (sort == null) sort = "default";
        return productDao.searchProductsPaginatedByPrice(keyword, cid, sort, pageSize, offset, minPrice, maxPrice);
    }

    public int getTotalPagesSearchByPrice(String keyword,int cid, int pageSize, double minPrice, double maxPrice) {
        int totalProducts = productDao.countSearchProductsByPrice(keyword, cid, minPrice, maxPrice);
        if (totalProducts == 0) return 1;
        return (int) Math.ceil((double) totalProducts / pageSize);
    }
}