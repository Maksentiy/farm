package com.konon.farm.employee;

import com.konon.farm.product.Product;

import java.sql.Date;
import java.util.Objects;

public class Work {

    private Integer id;

    private Employee employee;

    private Product product;

    private Date work_date;

    private Double harvest_weight;

    private Double salary;

    public Work() {
    }

    public Work(Date work_date, Double harvest_weight, Double salary) {
        this.work_date = work_date;
        this.harvest_weight = harvest_weight;
        this.salary = salary;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getWork_date() {
        return work_date;
    }

    public void setWork_date(Date work_date) {
        this.work_date = work_date;
    }

    public Double getHarvest_weight() {
        return harvest_weight;
    }

    public void setHarvest_weight(Double harvest_weight) {
        this.harvest_weight = harvest_weight;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Work that = (Work) o;
        return employee.equals(that.employee) && product.equals(that.product) && work_date.equals(that.work_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, product, work_date);
    }

    @Override
    public String toString() {
        return "Harvesting{" +
                "employee=" + employee +
                ", product=" + product +
                ", work_date=" + work_date +
                ", harvest_weight=" + harvest_weight +
                ", salary=" + salary +
                '}';
    }
}
