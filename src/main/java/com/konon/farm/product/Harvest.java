package com.konon.farm.product;

import java.sql.Date;
import java.util.Objects;

public class Harvest {

    private Integer id;

    private Product product;

    private Double weight;

    private Double price;

    private Date harvest_date;

    private Double expenses;

    private Double sold;

    private Double profit;

    public Harvest() {
        this.price = 0.0;
        this.expenses = 0.0;
        this.sold = 0.0;
        this.profit = 0.0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getHarvest_date() {
        return harvest_date;
    }

    public void setHarvest_date(Date harvest_date) {
        this.harvest_date = harvest_date;
    }

    public Double getExpenses() {
        return expenses;
    }

    public void setExpenses(Double expenses) {
        this.expenses = expenses;
    }

    public Double getSold() {
        return sold;
    }

    public void setSold(Double sold) {
        this.sold = sold;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Harvest harvest = (Harvest) o;
        return product.equals(harvest.product) && harvest_date.equals(harvest.harvest_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, harvest_date);
    }

    @Override
    public String toString() {
        return "Harvest{" +
                "id=" + id +
                ", product=" + product +
                ", weight=" + weight +
                ", price=" + price +
                ", harvest_date=" + harvest_date +
                ", expenses=" + expenses +
                ", sold=" + sold +
                ", profit=" + profit +
                '}';
    }
}
