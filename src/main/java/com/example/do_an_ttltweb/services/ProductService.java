package com.example.do_an_ttltweb.services;

import com.example.ltwebnhom23.dao.ProductDao;
import com.example.ltwebnhom23.model.Product;

import java.util.List;

public class ProductService {
    private ProductDao productDao = new ProductDao();

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

    public List<Product> getProductsForCatalog(int cid, String sort, int page) {
        if (sort == null) sort = "default";
        int offset = (page - 1) * 25; // Tính vị trí bắt đầu
        return productDao.getFilteredProducts(cid, sort, offset);
    }

    public int getTotalPages(int cid) {
        int totalProducts = productDao.countProducts(cid);
        return (int) Math.ceil((double) totalProducts / 25);
    }
    // ---------------------------

    public boolean addProductWithUrls(Product product, String[] imageUrls) {
        try {
            int newProductId = productDao.insertProduct(product);
            if (newProductId > 0) {
                if (imageUrls != null && imageUrls.length > 0) {
                    for (String url : imageUrls) {
                        if (url != null && !url.trim().isEmpty()) {
                            productDao.insertProductImage(newProductId, url.trim());
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
    public boolean updateProduct(Product p) {
        return productDao.updateProduct(p);
    }

    public void deleteListProducts(String[] ids) {
        if (ids == null || ids.length == 0) return;

        for (String idStr : ids) {
            try {
                int id = Integer.parseInt(idStr.trim());
                int sold = productDao.getSoldCount(id);

                if (sold > 0) {
                    productDao.softDeleteProduct(id);
                } else {
                    try {
                        productDao.hardDeleteProduct(id);
                    } catch (Exception e) {
                        productDao.softDeleteProduct(id);
                    }
                }
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
}