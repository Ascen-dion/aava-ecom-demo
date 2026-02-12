package com.ecommerce.app.repository;

import com.ecommerce.app.model.Product;
import com.ecommerce.app.util.SqlFileReader;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SqlFileReader sqlFileReader;

    public ProductDao(JdbcTemplate jdbcTemplate,
                      SqlFileReader sqlFileReader) {
        this.jdbcTemplate = jdbcTemplate;
        this.sqlFileReader = sqlFileReader;
    }

    public List<Product> findAll() {
        String sql = sqlFileReader.loadSql("sql/product/find-all.sql");
        return jdbcTemplate.query(sql, this::mapRow);
    }

    public Product findById(Long id) {
        String sql = sqlFileReader.loadSql("sql/product/find-by-id.sql");
        return jdbcTemplate.queryForObject(sql, this::mapRow, id);
    }

    public Product save(Product product) {

    String sql = sqlFileReader.loadSql("sql/product/insert.sql");

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, product.getName());
        ps.setString(2, product.getDescription());
        ps.setDouble(3, product.getPrice());
        ps.setInt(4, product.getStock());
        ps.setString(5, product.getImageUrl());
        ps.setString(6, product.getCategory());
        return ps;
    }, keyHolder);

    // Set generated ID back to object
    if (keyHolder.getKey() != null) {
        product.setId(keyHolder.getKey().longValue());
    }

    return product;
    }

    public Product update(Product product) {

    String sql = sqlFileReader.loadSql("sql/product/update.sql");

    int rowsAffected = jdbcTemplate.update(sql,
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock(),
            product.getImageUrl(),
            product.getCategory(),
            product.getId()
    );

    if (rowsAffected == 0) {
        throw new RuntimeException("Product not found with id: " + product.getId());
    }

    return product;
    }

    public int delete(Long id) {
        String sql = sqlFileReader.loadSql("sql/product/delete.sql");
        return jdbcTemplate.update(sql, id);
    }


    // Find products by category
    public List<Product> findByCategory(String category) {
        String sql = sqlFileReader.loadSql("sql/product/find-by-category.sql");
        return jdbcTemplate.query(sql, this::mapRow, category);
    }

    // Find products by name containing (case-insensitive search)
    public List<Product> findByNameContainingIgnoreCase(String name) {
        String sql = sqlFileReader.loadSql("sql/product/find-by-name-containing.sql");
        return jdbcTemplate.query(sql, this::mapRow, name);
    }

    // Find products in stock
    public List<Product> findByStockGreaterThan(Integer stock) {
        String sql = sqlFileReader.loadSql("sql/product/find-by-stock-greater-than.sql");
        return jdbcTemplate.query(sql, this::mapRow, stock);
    }

    private Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getDouble("price"));
        product.setStock(rs.getInt("stock"));
        product.setImageUrl(rs.getString("image_url"));
        product.setCategory(rs.getString("category"));
        return product;
    }
}
