package com.konon.farm.db;

import com.konon.farm.employee.Employee;
import com.konon.farm.employee.Work;
import com.konon.farm.product.Harvest;
import com.konon.farm.product.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Helper {

    public static Product populateProduct(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getInt("id"));
        product.setName(resultSet.getString("name"));
        product.setType(resultSet.getString("type"));
        return product;
    }

    public static Employee populateEmployee(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();
        employee.setId(resultSet.getInt("id"));
        employee.setFirstName(resultSet.getString("first_name"));
        employee.setSecondName(resultSet.getString("second_name"));
        employee.setPosition(resultSet.getString("position"));
        employee.setPasport_id(resultSet.getString("pasport_id"));
        return employee;
    }

    public static Harvest populateHarvest(ResultSet resultSet) throws SQLException {
        Harvest harvest = new Harvest();
        harvest.setId(resultSet.getInt("id"));
        harvest.setProduct(Searches.productSearch(resultSet));
        harvest.setWeight(resultSet.getDouble("weight"));
        harvest.setPrice(resultSet.getDouble("price"));
        harvest.setHarvest_date(resultSet.getDate("harvest_date"));
        harvest.setExpenses(resultSet.getDouble("expenses"));
        harvest.setSold(resultSet.getDouble("sold"));
        harvest.setProfit(resultSet.getDouble("profit"));
        return harvest;
    }

    public static Work populateHarvesting(ResultSet resultSet) throws SQLException {
        Work work = new Work();
        work.setId(resultSet.getInt("id"));
        work.setEmployee(Searches.employeeSearch(resultSet));
        work.setProduct(Searches.productSearch(resultSet));
        work.setWork_date(resultSet.getDate("work_date"));
        work.setHarvest_weight(resultSet.getDouble("harvest_weight"));
        work.setSalary(resultSet.getDouble("salary"));
        return work;
    }
}
