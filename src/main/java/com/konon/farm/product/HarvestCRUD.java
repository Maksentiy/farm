package com.konon.farm.product;

import com.konon.farm.db.Connect;
import com.konon.farm.db.Default;
import com.konon.farm.db.Helper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HarvestCRUD implements Default<Harvest> {

    @Override
    public List<Harvest> getAll() {
        Connection connection = new Connect().getConnection();
        String sqlCommand = "select * from harvest;";
        List<Harvest> result = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet != null && resultSet.next()) {
                result.add((Helper.populateHarvest(resultSet)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public void update(Harvest harvest) {
        Connection connection = new Connect().getConnection();
        String sqlCommand = "update harvest set weight = ?, price = ?, harvest_date = ?, expenses = ?, sold = ?, profit = ? where id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand)) {
            preparedStatement.setDouble(1, harvest.getWeight());
            preparedStatement.setDouble(2, harvest.getPrice());
            preparedStatement.setDate(3, harvest.getHarvest_date());
            preparedStatement.setDouble(4, harvest.getExpenses());
            preparedStatement.setDouble(5, harvest.getSold());
            preparedStatement.setDouble(6, harvest.getProfit());
            preparedStatement.setInt(7, harvest.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        Connection connection = new Connect().getConnection();
        String sqlCommand = "delete from harvest where id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(Harvest harvest) {

        Harvest availableHarvest = checkAvailability(harvest);
        if (availableHarvest == null) {
            insertSQLCommand(harvest);
        } else {
            availableHarvest.setExpenses(availableHarvest.getExpenses() + harvest.getExpenses());
            availableHarvest.setWeight(availableHarvest.getWeight() + harvest.getWeight());
            update(availableHarvest);
        }
    }

    private Harvest checkAvailability(Harvest harvest) {
        List<Harvest> all = getAll();
        for (Harvest checkHarvest: all) {
            if (checkHarvest.equals(harvest)) {
                return checkHarvest;
            }
        }

        return null;
    }

    private void insertSQLCommand(Harvest harvest) {
        Connection connection = new Connect().getConnection();
        String sqlCommand = "insert into harvest (product_id, weight, price, harvest_date, expenses, sold, profit) values (?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);) {
            preparedStatement.setInt(1, harvest.getProduct().getId());
            preparedStatement.setDouble(2, harvest.getWeight());
            preparedStatement.setDouble(3, harvest.getPrice());
            preparedStatement.setDate(4, harvest.getHarvest_date());
            preparedStatement.setDouble(5, harvest.getExpenses());
            preparedStatement.setDouble(6, harvest.getSold());
            preparedStatement.setDouble(7, harvest.getProfit());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Harvest> searchByDates(Date startDate, Date lastDate, List<Harvest> harvestList) {
        List<Harvest> finalHarvestList = new ArrayList<>();
        for (Harvest harvest:harvestList) {
            if (harvest.getHarvest_date().before(lastDate) && harvest.getHarvest_date().after(startDate)) {
                finalHarvestList.add(harvest);
            }
        }
        return  finalHarvestList;
    }
}
