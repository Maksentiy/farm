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

public class EmployeeCRUD implements Default<Employee> {

    @Override
    public List<Employee> getAll() {
        Connection connection = new Connect().getConnection();
        String sqlCommand = "select * from employee;";
        List<Employee> result = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet != null && resultSet.next()) {
                result.add((Helper.populateEmployee(resultSet)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void update(Employee employee) {
        Connection connection = new Connect().getConnection();
        String sqlCommand = "update employee set first_name = ?, second_name = ?, position = ?, pasport_id = ? where id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand)) {
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getSecondName());
            preparedStatement.setString(3, employee.getPosition());
            preparedStatement.setString(4, employee.getPasport_id());
            preparedStatement.setInt(5, employee.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        Connection connection = new Connect().getConnection();
        String sqlCommand = "delete from employee where id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(Employee employee) {
        Connection connection = new Connect().getConnection();
        String sqlCommand = "insert into employee (first_name, second_name, position, pasport_id) values (?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);) {
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getSecondName());
            preparedStatement.setString(3, employee.getPosition());
            preparedStatement.setString(4, employee.getPasport_id());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean search(String s) {
        List<Employee> all = getAll();
        for (Employee employee:all) {
            if (employee.getPasport_id().equals(s)) {
                return true;
            }
        }
        return false;
    }
}
