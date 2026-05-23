package com.example.do_an_ttltweb.dao;

import com.example.do_an_ttltweb.model.Product;
import com.example.do_an_ttltweb.helper.base.BaseDao;

import java.util.List;

public class ProductDao extends BaseDao {

    public List<Product> getProductsBySold() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT \n" +
                                "    p.id,\n" +
                                "    p.name,\n" +
                                "    p.price,\n" +
                                "    p.sold,\n" +
                                "    p.stock,\n" +
                                "    p.weight_grams,\n" +
                                "    p.description,\n" +
                                "    (\n" +
                                "        SELECT image_url \n" +
                                "        FROM product_images i \n" +
                                "        WHERE i.product_id = p.id AND i.position = 0 \n" +
                                "        LIMIT 1\n" +
                                "    ) AS image_url,\n" +
                                "    c.name AS category_name,\n" +
                                "    IFNULL(AVG(r.rating), 0) AS avg_rating\n" +
                                "FROM products p\n" +
                                "JOIN categories c ON p.category_id = c.id\n" +
                                "LEFT JOIN products_review r ON p.id = r.product_id\n" +
                                "WHERE p.state = 'active'\n" +
                                "GROUP BY \n" +
                                "    p.id,\n" +
                                "    p.name,\n" +
                                "    p.price,\n" +
                                "    p.sold,\n" +
                                "    p.stock,\n" +
                                "    p.weight_grams,\n" +
                                "    p.description,\n" +
                                "    c.name\n" +
                                "ORDER BY p.sold DESC\n" +
                                "LIMIT 10;\n")
                        .mapToBean(Product.class)
                        .list()
        );
    }

    public List<Product> getAllProduct() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT p.id, p.category_id, p.name, p.price, p.description, p.stock, p.sold, p.weight_grams, p.state, " +
                                "(SELECT image_url FROM product_images i WHERE i.product_id = p.id AND i.position = 0 LIMIT 1) AS image_url, " +
                                "c.name AS category_name, " +
                                "IFNULL(AVG(r.rating), 0) AS avg_rating " +
                                "FROM products p " +
                                "JOIN categories c ON p.category_id = c.id " +
                                "LEFT JOIN products_review r ON p.id = r.product_id " +
                                "GROUP BY p.id, p.name, p.price, c.name")
                        .mapToBean(Product.class)
                        .list()
        );
    }

    public List<Product> getProductForCategory(int cid) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT p.id, p.name, p.price, p.sold, " +
                                "(SELECT image_url FROM product_images i WHERE i.product_id = p.id AND i.position = 0 LIMIT 1) AS image_url, " +
                                "c.name AS category_name " +
                                "FROM products p " +
                                "JOIN categories c ON p.category_id = c.id " +
                                "WHERE p.state ='active' And p.category_id = :c")
                        .bind("c", cid)
                        .mapToBean(Product.class)
                        .list()
        );
    }

    public Product getProductById(int pid) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT p.*, " +
                                "c.name AS category_name, " +
                                "(SELECT image_url FROM product_images i WHERE i.product_id = p.id AND i.position = 0 LIMIT 1) AS image_url, " +
                                "IFNULL(AVG(r.rating), 0) AS avg_rating " +
                                "FROM products p " +
                                "JOIN categories c ON p.category_id = c.id " +
                                "LEFT JOIN products_review r ON p.id = r.product_id " +
                                "WHERE p.state ='active' And p.id = :pid " +
                                "GROUP BY p.id, c.name")
                        .bind("pid", pid)
                        .mapToBean(Product.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public List<Product> getProductsByRelative(int cid, String name, int pid) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT p.id, p.name, p.price, p.sold, " +
                                "(SELECT image_url FROM product_images i WHERE i.product_id = p.id AND i.position = 0 LIMIT 1) AS image_url, " +
                                "c.name AS category_name " +
                                "FROM products p " +
                                "JOIN categories c ON p.category_id = c.id " +
                                "WHERE ( p.state ='active' And p.name LIKE CONCAT('%', :name, '%') OR p.category_id = :category_id) " +
                                "AND p.id != :product_id " +
                                "ORDER BY p.sold DESC LIMIT 10")
                        .bind("name", name)
                        .bind("category_id", cid)
                        .bind("product_id", pid)
                        .mapToBean(Product.class)
                        .list()
        );
    }


    public int countProducts(int cid, double minPrice, double maxPrice) {
        return getJdbi().withHandle(handle -> {
            StringBuilder sql = new StringBuilder(
                    "SELECT COUNT(*) FROM products p WHERE p.state = 'active' AND p.stock > 0 " +
                            "AND p.price >= :minPrice AND p.price <= :maxPrice "
            );
            if (cid > 0) sql.append("AND p.category_id = :cid");
            var query = handle.createQuery(sql.toString())
                    .bind("minPrice", minPrice)
                    .bind("maxPrice", maxPrice);
            if (cid > 0) query.bind("cid", cid);
            return query.mapTo(Integer.class).one();
        });
    }

    public List<Product> getFilteredProducts(int cid, String sortType, int offset, double minPrice, double maxPrice) {
        return getJdbi().withHandle(handle -> {
            StringBuilder sql = new StringBuilder(
                    "SELECT p.id, p.category_id, p.name, p.price, p.sold, p.stock, p.weight_grams, " +
                            "(SELECT image_url FROM product_images i WHERE i.product_id = p.id AND i.position = 0 LIMIT 1) AS image_url, " +
                            "c.name AS category_name, " +
                            "IFNULL(AVG(r.rating), 0) AS avg_rating " +
                            "FROM products p " +
                            "JOIN categories c ON p.category_id = c.id " +
                            "LEFT JOIN products_review r ON p.id = r.product_id " +
                            "WHERE p.state = 'active' AND p.stock > 0 " +
                            "AND p.price >= :minPrice AND p.price <= :maxPrice "
            );

            if (cid > 0) sql.append("AND p.category_id = :cid ");

            sql.append("GROUP BY p.id, p.name, p.price, c.name, p.sold, p.stock, p.weight_grams, p.category_id ");

            if (sortType != null) {
                switch (sortType) {
                    case "price-desc": sql.append("ORDER BY p.price DESC "); break;
                    case "price-asc":  sql.append("ORDER BY p.price ASC ");  break;
                    case "sold":       sql.append("ORDER BY p.sold DESC ");  break;
                    case "rating":     sql.append("ORDER BY avg_rating DESC "); break;
                    default:           sql.append("ORDER BY p.name ASC ");   break;
                }
            } else {
                sql.append("ORDER BY p.name ASC ");
            }
            sql.append("LIMIT 25 OFFSET :offset");

            var query = handle.createQuery(sql.toString())
                    .bind("minPrice", minPrice)
                    .bind("maxPrice", maxPrice)
                    .bind("offset", offset);
            if (cid > 0) query.bind("cid", cid);
            return query.mapToBean(Product.class).list();
        });
    }

    public int insertProduct(Product p) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("INSERT INTO products (category_id, name, description, stock, sold, weight_grams, price) " +
                                "VALUES (:category_id, :name, :description, :stock, :sold, :weight_grams, :price)")
                        .bind("category_id", p.getCategory_id())
                        .bind("name", p.getName())
                        .bind("description", p.getDescription())
                        .bind("stock", p.getStock())
                        .bind("sold", 0)
                        .bind("weight_grams", p.getWeight_grams())
                        .bind("price", p.getPrice())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public void insertProductImage(int productId, String imageUrl, int position) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("INSERT INTO product_images (product_id, image_url, position) VALUES (:pid, :url, :pos)")
                        .bind("pid", productId)
                        .bind("url", imageUrl)
                        .bind("pos", position)
                        .execute()
        );
    }

    public int getSoldCount(int id) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT sold FROM products WHERE id = :id")
                        .bind("id", id)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(0)
        );
    }

    public boolean softDeleteProduct(int id) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE products SET state = 'inactive' WHERE id = :id")
                        .bind("id", id)
                        .execute() > 0
        );
    }

    public boolean hardDeleteProduct(int id) {
        return getJdbi().withHandle(handle -> {
            handle.createUpdate("DELETE FROM product_images WHERE product_id = :id")
                    .bind("id", id)
                    .execute();

            return handle.createUpdate("DELETE FROM products WHERE id = :id")
                    .bind("id", id)
                    .execute() > 0;
        });
    }


    public boolean updateProduct(Product p) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE products SET " +
                                "name = :name, " +
                                "category_id = :catId, " +
                                "price = :price, " +
                                "stock = :stock, " +
                                "weight_grams = :weight, " +
                                "description = :desc, " +
                                "state = :state " +
                                "WHERE id = :id")
                        .bind("name", p.getName())
                        .bind("catId", p.getCategory_id())
                        .bind("price", p.getPrice())
                        .bind("stock", p.getStock())
                        .bind("weight", p.getWeight_grams())
                        .bind("desc", p.getDescription())
                        .bind("state", p.getState())
                        .bind("id", p.getId())
                        .execute() > 0
        );
    }
    public List<Product> searchProducts(String keyword) {
        String sql = "SELECT p.* " +
                "FROM products p " +
                "WHERE p.id LIKE :key " +
                "OR p.name LIKE :key " ;

        return getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .bind("key", "%" + keyword + "%")
                        .mapToBean(Product.class)
                        .list()
        );
    }

    public int countSearchProducts(String keyword) {
        String sql = "SELECT COUNT(*) FROM products p " +
                "WHERE CAST(p.id AS CHAR) LIKE :key OR p.name LIKE :key";
        return getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .bind("key", "%" + keyword + "%")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public List<Product> searchProductsPaginated(String keyword, int limit, int offset) {
        String sql = "SELECT p.id, p.category_id, p.name, p.price, p.description, p.stock, p.sold, p.weight_grams, p.state, " +
                "(SELECT image_url FROM product_images i WHERE i.product_id = p.id AND i.position = 0 LIMIT 1) AS image_url, " +
                "c.name AS category_name " +
                "FROM products p " +
                "JOIN categories c ON p.category_id = c.id " +
                "WHERE p.id LIKE :key OR p.name LIKE :key " +
                "ORDER BY p.id DESC " +
                "LIMIT :limit OFFSET :offset";

        return getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .bind("key", "%" + keyword + "%")
                        .bind("limit", limit)
                        .bind("offset", offset)
                        .mapToBean(Product.class)
                        .list()
        );
    }

    public int countProductsForAdmin(int categoryId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM products p ");
        if (categoryId > 0) {
            sql.append("WHERE p.category_id = :cid");
        }

        return getJdbi().withHandle(handle -> {
            var query = handle.createQuery(sql.toString());
            if (categoryId > 0) query.bind("cid", categoryId);
            return query.mapTo(Integer.class).one();
        });
    }

    public List<Product> getProductsPaginatedForAdmin(int categoryId, int limit, int offset) {
        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.category_id, p.name, p.price, p.description, p.stock, p.sold, p.weight_grams, p.state, " +
                        "(SELECT image_url FROM product_images i WHERE i.product_id = p.id AND i.position = 0 LIMIT 1) AS image_url, " +
                        "c.name AS category_name " +
                        "FROM products p " +
                        "JOIN categories c ON p.category_id = c.id "
        );

        if (categoryId > 0) {
            sql.append("WHERE p.category_id = :cid ");
        }
        sql.append("ORDER BY p.id DESC ");
        sql.append("LIMIT :limit OFFSET :offset");

        return getJdbi().withHandle(handle -> {
            var query = handle.createQuery(sql.toString());
            if (categoryId > 0) query.bind("cid", categoryId);
            query.bind("limit", limit);
            query.bind("offset", offset);
            return query.mapToBean(Product.class).list();
        });
    }

    public List<Product> getProductByKey(String key){
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT p.id, p.name, p.price, p.sold, " +
                                "(SELECT image_url FROM product_images i WHERE i.product_id = p.id AND i.position = 0 LIMIT 1) AS image_url, " +
                                "c.name AS category_name " +
                                "FROM products p " +
                                "JOIN categories c ON p.category_id = c.id " +
                                "WHERE p.state ='active' And p.name LIKE :key")
                        .bind("key", "%"+key+"%")
                        .mapToBean(Product.class)
                        .list()
        );
    }
    public List<Product> searchProductsPaginatedByPrice(String keyword, int cid, String sort, int limit, int offset, double minPrice, double maxPrice) {
        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.name, p.price, p.sold, p.stock, p.weight_grams, p.category_id, p.description, p.state, " +
                        "(SELECT image_url FROM product_images i WHERE i.product_id = p.id AND i.position = 0 LIMIT 1) AS image_url, " +
                        "c.name AS category_name, " +
                        "IFNULL(AVG(r.rating), 0) AS avg_rating " +
                        "FROM products p " +
                        "JOIN categories c ON p.category_id = c.id " +
                        "LEFT JOIN products_review r ON p.id = r.product_id " +
                        "WHERE p.name LIKE :key " +
                        "AND p.price >= :minPrice AND p.price <= :maxPrice " +
                        "AND p.state = 'active' AND p.stock > 0 "
        );
        if (cid > 0) sql.append("AND p.category_id = :cid ");
        sql.append("GROUP BY p.id, p.name, p.price, c.name, p.sold, p.stock, p.weight_grams, p.category_id, p.description, p.state ");

        if ("price-asc".equals(sort)) {
            sql.append("ORDER BY p.price ASC ");
        } else if ("price-desc".equals(sort)) {
            sql.append("ORDER BY p.price DESC ");
        } else if ("sold".equals(sort)) {
            sql.append("ORDER BY p.sold DESC ");
        } else {
            sql.append("ORDER BY p.id DESC ");
        }

        sql.append("LIMIT :limit OFFSET :offset");

        return getJdbi().withHandle(h -> {
            var q = h.createQuery(sql.toString())
                    .bind("key", "%" + keyword + "%")
                    .bind("minPrice", minPrice)
                    .bind("maxPrice", maxPrice)
                    .bind("limit", limit)
                    .bind("offset", offset);
            if (cid > 0) q.bind("cid", cid);
            return q.mapToBean(Product.class).list();
        });
    }
    public int countSearchProductsByPrice(String keyword,int cid, double minPrice, double maxPrice) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM products p " +
                        "WHERE p.name LIKE :key " +
                        "AND p.price >= :minPrice AND p.price <= :maxPrice " +
                        "AND p.state = 'active' AND p.stock > 0 "
        );
        if (cid > 0) sql.append("AND p.category_id = :cid");

        return getJdbi().withHandle(h -> {
            var q = h.createQuery(sql.toString())
                    .bind("key", "%" + keyword + "%")
                    .bind("minPrice", minPrice)
                    .bind("maxPrice", maxPrice);
            if (cid > 0) q.bind("cid", cid);
            return q.mapTo(Integer.class).one();
        });
    }
}