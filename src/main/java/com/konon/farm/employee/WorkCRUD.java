package com.konon.farm.employee;

import com.konon.farm.db.Connect;
import com.konon.farm.db.Default;
import com.konon.farm.db.Helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkCRUD implements Default<Work> {

    @Override
    public List<Work> getAll() {
        Connection connection = new Connect().getConnection();
        String sqlCommand = "select * from harvesting;";
        List<Work> result = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet != null && resultSet.next()) {
                result.add((Helper.populateHarvesting(resultSet)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void update(Work work) {
        Connection connection = new Connect().getConnection();
        String sqlCommand = "update harvesting set employee_id = ?, product_id = ?, work_date = ?, harvest_weight = ?, salary = ? where id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand)) {
            preparedStatement.setInt(1, work.getEmployee().getId());
            preparedStatement.setInt(2, work.getProduct().getId());
            preparedStatement.setDate(3, work.getWork_date());
            preparedStatement.setDouble(4, work.getHarvest_weight());
            preparedStatement.setDouble(5, work.getSalary());
            preparedStatement.setInt(6, work.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        Connection connection = new Connect().getConnection();
        String sqlCommand = "delete from harvesting where id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(Work work) {
        Connection connection = new Connect().getConnection();
        String sqlCommand = "insert into harvesting (employee_id, product_id, work_date, harvest_weight, salary) values (?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);) {
            preparedStatement.setInt(1, work.getEmployee().getId());
            preparedStatement.setInt(2, work.getProduct().getId());
            preparedStatement.setDate(3, work.getWork_date());
            preparedStatement.setDouble(4, work.getHarvest_weight());
            preparedStatement.setDouble(5, work.getSalary());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Work> searchByDates(Integer firstValue, Integer secondValue) {
        return null;
    }
}
