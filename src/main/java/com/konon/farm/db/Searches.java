package com.konon.farm.db;

import com.konon.farm.employee.Employee;
import com.konon.farm.employee.EmployeeCRUD;
import com.konon.farm.employee.Work;
import com.konon.farm.employee.WorkCRUD;
import com.konon.farm.product.Harvest;
import com.konon.farm.product.HarvestCRUD;
import com.konon.farm.product.Product;
import com.konon.farm.product.ProductCRUD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Searches {

    public static Employee employeeSearch(ResultSet resultSet) throws SQLException {
        EmployeeCRUD employeeCRUD = new EmployeeCRUD();
        List<Employee> employees = employeeCRUD.getAll();
        for (Employee employee: employees) {
            if (employee.getId() == resultSet.getInt("employee_id")) {
                return employee;
            }
        }

        return null;
    }

    public static Product productSearch(ResultSet resultSet) throws SQLException {
        ProductCRUD productCRUD = new ProductCRUD();
        List<Product> employees = productCRUD.getAll();
        for (Product product: employees) {
            if (product.getId() == resultSet.getInt("product_id")) {
                return product;
            }
        }

        return null;
    }

    public Work searchWorkEqual(Work chosenWork) {
        WorkCRUD workCRUD = new WorkCRUD();
        Optional<Work> first = workCRUD.getAll().stream()
                .filter(harvesting -> harvesting.getWork_date().equals(chosenWork.getWork_date()))
                .filter(harvesting -> harvesting.getEmployee().equals(chosenWork.getEmployee()))
                .filter(harvesting -> harvesting.getProduct().equals(chosenWork.getProduct()))
                .findFirst();
        return first.orElse(null);

    }

    public Harvest searchHarvestEqual(Harvest chosenHarvest) {
        HarvestCRUD harvestCRUD = new HarvestCRUD();
        Optional<Harvest> first = harvestCRUD.getAll().stream()
                .filter(harvest -> harvest.getHarvest_date().equals(chosenHarvest.getHarvest_date()))
                .filter(harvest -> harvest.getProduct().equals(chosenHarvest.getProduct()))
                .findFirst();
        return first.orElse(null);
    }
}
