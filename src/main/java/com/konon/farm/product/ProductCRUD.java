package com.konon.farm.product;

import com.konon.farm.db.Connect;
import com.konon.farm.db.Default;
import com.konon.farm.db.Helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductCRUD implements Default<Product> {

    @Override
    public List<Product> getAll() {
        Connection connection = new Connect().getConnection();
        String sqlCommand = "select * from product;";
        List<Product> result = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet != null && resultSet.next()) {
                result.add((Helper.populateProduct(resultSet)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void update(Product product) {
        Connection connection = new Connect().getConnection();
        String sqlCommand = "update product set name = ?, type = ? where id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getType());
            preparedStatement.setInt(3, product.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        Connection connection = new Connect().getConnection();
        String sqlCommand = "delete from product where id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(Product product) {
        Connection connection = new Connect().getConnection();
        String sqlCommand = "insert into product (name, type) values (?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getType());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> searchByDates(Integer firstValue, Integer secondValue) {
        return null;
    }
}
